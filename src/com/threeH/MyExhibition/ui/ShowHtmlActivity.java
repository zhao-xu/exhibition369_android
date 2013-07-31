package com.threeH.MyExhibition.ui;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
public class ShowHtmlActivity extends BaseActivity implements ActivityInterface  {
    private WebView mWebVi;
    private String mUrl;
    private ImageView mImgviewTelephone, mImgviewSignup;
    private TextView mTxtitle;
    private String mStrTitle;
    private char mChrSingupStatus;
    private String mExKey;
    private Typeface mTypeface;
    private FrameLayout mWebContainer;
    private RelativeLayout mRelativeLayout;
    private Boolean mIsHiddenTitleBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.webview);

        mWebContainer = (FrameLayout) findViewById(R.id.webContainer);
        mWebVi = new WebView(getApplicationContext());
        mWebContainer.addView(mWebVi);

        initdata();
        findView();
        addAction();
    }

    @Override
    protected void onDestroy() {
        mWebContainer.removeAllViews();
        mWebVi.destroy();
        super.onDestroy();
    }

    @Override
    public void findView() {
        mRelativeLayout = (RelativeLayout) this.findViewById(R.id.webview_include);
        mImgviewTelephone = (ImageView) this.findViewById(R.id.exhibition_titlebar_btn_telephone);
        mTxtitle = (TextView) this.findViewById(R.id.exhibition_titlebar_txt_title);
        mImgviewSignup = (ImageView) this.findViewById(R.id.exhibition_titlebar_signup);
    }

    @Override
    public void initdata() {
        mUrl = getIntent().getStringExtra("url");
        mStrTitle = getIntent().getStringExtra("title");
        mTypeface = MSYH.getInstance(context.getApplicationContext()).getNormal();
        mChrSingupStatus = getIntent().getCharExtra("singupStatus", ' ');
        mExKey = getIntent().getStringExtra("exKey");
        mIsHiddenTitleBar = getIntent().getBooleanExtra("isHiddenTitleBar",false);
    }

    @Override
    public void addAction() {
        mImgviewTelephone.setOnClickListener(new TelephoneClickListener(this, tel_nummber));
        mTxtitle.setTypeface(mTypeface);
        mTxtitle.setText(mStrTitle);
        switch (mChrSingupStatus){
            case 'D':
            case 'N':
                break;
            case 'P':
            case 'A':
                mImgviewSignup.setVisibility(View.GONE);
                break;
        }
        mImgviewSignup.setOnClickListener(new SignupClickListener(this, mExKey));
        if(mIsHiddenTitleBar){
            mRelativeLayout.setVisibility(View.GONE);
        }
        LoadHtmlTask loadHtmlTask = new LoadHtmlTask();
        loadHtmlTask.execute();
    }
    class LoadHtmlTask extends AsyncTask<Void,Integer,Integer>{
        @Override
        protected Integer doInBackground(Void... params) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mWebVi.loadUrl(mUrl);
                }
            }).start();
            return null;
        }
    }

}
