package com.threeH.MyExhibition.ui;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.listener.SignupClickListener;
import com.threeH.MyExhibition.listener.TelephoneClickListener;
import com.threeH.MyExhibition.tools.ImageURLUtil;
import com.threeH.MyExhibition.tools.MSYH;
import com.threeH.MyExhibition.tools.Tool;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-6-20
 * Time: 上午10:40
 * To change this template use File | Settings | File Templates.
 */
public class ShowHtmlActivity extends BaseActivity implements ActivityInterface  {
    private WebView webView;
    private String url;
    private ImageView imageviewTelephone,imageViewSignup;
    private TextView textViewTitle;
    private String strTitle;
    private char charSingupStatus;
    private String exKey;
    Typeface typeface;
    FrameLayout webContainer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.webview);

        webContainer = (FrameLayout) findViewById(R.id.webContainer);
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
        imageviewTelephone = (ImageView) this.findViewById(R.id.exhibition_titlebar_button_telephone);
        textViewTitle = (TextView) this.findViewById(R.id.exhibition_titlebar_textview_title);
        imageViewSignup = (ImageView) this.findViewById(R.id.exhibition_titlebar_signup);
    }

    @Override
    public void initdata() {
        url = getIntent().getStringExtra("url");
        strTitle = getIntent().getStringExtra("title");
        typeface = MSYH.getInstance(context.getApplicationContext()).getNormal();
        charSingupStatus = getIntent().getCharExtra("singupStatus", ' ');
        exKey = getIntent().getStringExtra("exKey");
    }

    @Override
    public void addAction() {
        imageviewTelephone.setOnClickListener(new TelephoneClickListener(this,tel_nummber));
        textViewTitle.setTypeface(typeface);
        textViewTitle.setText(strTitle);
        switch (charSingupStatus){
            case 'D':
            case 'N':
                break;
            case 'P':
            case 'A':
                imageViewSignup.setVisibility(View.GONE);
                break;
        }
        imageViewSignup.setOnClickListener(new SignupClickListener(this,exKey));
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
