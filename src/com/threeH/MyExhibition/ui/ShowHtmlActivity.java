package com.threeH.MyExhibition.ui;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.listener.TelephoneClickListener;

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
    private ImageView imageviewTelephone;
    private TextView textViewTitle;
    private String strTitle;
    Typeface typeface;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.webview);
        initdata();
        findView();
        addAction();
    }

    @Override
    public void findView() {
        webView = (WebView) this.findViewById(R.id.webview);
        imageviewTelephone = (ImageView) this.findViewById(R.id.exhibition_titlebar_button_telephone);
        textViewTitle = (TextView) this.findViewById(R.id.exhibition_titlebar_textview_title);
    }

    @Override
    public void initdata() {
        url = getIntent().getStringExtra("url");
        strTitle = getIntent().getStringExtra("title");
        typeface = Typeface.createFromAsset(context.getAssets(),"fonts/msyh.ttf");
    }

    @Override
    public void addAction() {
        imageviewTelephone.setOnClickListener(new TelephoneClickListener(this,tel_nummber));
        textViewTitle.setTypeface(typeface);
        textViewTitle.setText(strTitle);
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
