package com.threeH.MyExhibition.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import com.threeH.MyExhibition.R;

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
    }

    @Override
    public void initdata() {
        url = getIntent().getStringExtra("url");
    }

    @Override
    public void addAction() {
         webView.loadUrl(url);
    }
}
