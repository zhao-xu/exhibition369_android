package com.threeH.MyExhibition.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.cache.ImageCache;
import com.threeH.MyExhibition.listener.TelephoneClickListener;
import com.threeH.MyExhibition.tools.CustomerHttpClient;
import com.threeH.MyExhibition.tools.ImageURLUtil;

/**
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 13-6-19
 * Time: 上午11:40
 * To change this template use File | Settings | File Templates.
 */
public class QrCodeActivity extends Activity implements  ActivityInterface{
    private ImageView imageView;
    private Bitmap bitmap;
    private ImageButton button_telephone;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.qrcode);
        initdata();
        findView();
        addAction();
    }

    @Override
    public void findView(){
         imageView = (ImageView) this.findViewById(R.id.qrcode_imageview);
         button_telephone = (ImageButton) this.findViewById(R.id.exhibition_titlebar_button_telephone);
    }

    @Override
    public void initdata(){

    }

    @Override
    public void addAction() {
        /*String url = "http://180.168.35.37:8080/e369_asset/1103/qrcode/pjqandroid.png";
        ImageURLUtil.loadImage(url,imageView);*/
        button_telephone.setOnClickListener(new TelephoneClickListener(this));
    }
}