package com.threeH.MyExhibition.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageButton;
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
    private ImageButton button_telephone;
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
        button_telephone = (ImageButton) this.findViewById(R.id.exhibition_titlebar_button_telephone);
    }

    @Override
    public void initdata() {
        url = getIntent().getStringExtra("url");
    }

    @Override
    public void addAction() {
         webView.loadUrl(url);
         button_telephone.setOnClickListener(new TelephoneClickListener(this));
    }
}
