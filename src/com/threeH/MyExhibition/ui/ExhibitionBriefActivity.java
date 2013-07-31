package com.threeH.MyExhibition.ui;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.*;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.listener.SignupClickListener;
import com.threeH.MyExhibition.listener.TelephoneClickListener;
import com.threeH.MyExhibition.tools.MSYH;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-6-20
 * Time: 上午10:40
 * To change this template use File | Settings | File Templates.
 */
public class ExhibitionBriefActivity extends BaseActivity implements ActivityInterface  {
    private WebView webView;
    private String url;
    private ImageView imageviewTelephone,imageViewSignup;
    private TextView mTxtTitle,mTxtTheme;
    private String mStrTitle,mStrTheme;
    private char charSingupStatus;
    private String exKey;
    private Button mBtnSignup;
    Typeface typeface;
    FrameLayout webContainer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.exhibition_brief);

        webContainer = (FrameLayout) findViewById(R.id.exhibition_brief_webContainer);
        webView = new WebView(getApplicationContext());
        webContainer.addView(webView);

        initdata();
        findView();
        addAction();
    }

    @Override
    protected void onDestroy() {
        webContainer.removeAllViews();
        webView.destroy();
        super.onDestroy();
    }

    @Override
    public void findView() {
        imageviewTelephone = (ImageView) this.findViewById(R.id.exhibition_titlebar_btn_telephone);
        mTxtTitle = (TextView) this.findViewById(R.id.exhibition_titlebar_txt_title);
        imageViewSignup = (ImageView) this.findViewById(R.id.exhibition_titlebar_signup);
        mTxtTheme = (TextView) this.findViewById(R.id.exhibition_brief_txt_theme);
        mBtnSignup = (Button) this.findViewById(R.id.exhibition_brief_btn_signup);
    }

    @Override
    public void initdata() {
        url = getIntent().getStringExtra("url");
        mStrTitle = getIntent().getStringExtra("title");
        typeface = MSYH.getInstance(context.getApplicationContext()).getNormal();
        charSingupStatus = getIntent().getCharExtra("singupStatus", ' ');
        exKey = getIntent().getStringExtra("exKey");
        mStrTheme = getIntent().getStringExtra("theme");
    }

    @Override
    public void addAction() {
        imageviewTelephone.setOnClickListener(new TelephoneClickListener(this,tel_nummber));
        mTxtTitle.setTypeface(typeface);
        mTxtTitle.setText(mStrTitle);
        imageViewSignup.setVisibility(View.GONE);
        switch (charSingupStatus){
            case ' ':
            case 'D':
            case 'N':
                mBtnSignup.setOnClickListener(new SignupClickListener(this,exKey));
                break;
            case 'P':
            case 'A':
                mBtnSignup.setVisibility(View.GONE);
                break;
        }
        mTxtTheme.setText(mStrTheme);
        LoadHtmlTask loadHtmlTask = new LoadHtmlTask();
        loadHtmlTask.execute();
    }
    class LoadHtmlTask extends AsyncTask<Void,Integer,Integer>{
        @Override
        protected Integer doInBackground(Void... params) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(url);
                }
            }).start();
            return null;
        }
    }

}
