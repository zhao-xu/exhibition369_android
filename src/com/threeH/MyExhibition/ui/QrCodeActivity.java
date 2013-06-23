package com.threeH.MyExhibition.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.TextView;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.listener.TelephoneClickListener;
import com.threeH.MyExhibition.tools.ImageURLUtil;
import com.threeH.MyExhibition.tools.Tool;

/**
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 13-6-19
 * Time: 上午11:40
 * To change this template use File | Settings | File Templates.
 */
public class QrCodeActivity extends BaseActivity implements  ActivityInterface{
    private ImageView imageView;
    private Bitmap bitmap;
    private ImageButton buttonTelephone;
    private char charSingupStatus;
    private String strExhibitionKey;
    private TextView textView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.qrcode);
        initdata();
        findView();
        addAction();
    }

    @Override
    public void findView(){
        imageView = (ImageView) this.findViewById(R.id.qrcode_imageview);
        buttonTelephone = (ImageButton) this.findViewById(R.id.exhibition_titlebar_button_telephone);
        textView = (TextView) this.findViewById(R.id.qrcode_textview_prompt);
    }

    @Override
    public void initdata(){
        charSingupStatus = getIntent().getCharExtra("singupStatus",' ');
        strExhibitionKey = getIntent().getStringExtra("exhibitionKey");
    }

    @Override
    public void addAction() {

        switch (charSingupStatus){
            case 'N':
                textView.setText("对不起您还没有报名参加此展会，请报名！");
                break;
            case 'P':
                textView.setText("您的个人信息还在审核中，请耐心等待。。。");
                break;
            case 'A':
                ImageURLUtil.loadImage(Tool.makeQrcodeURL(strExhibitionKey,"pjqandroid"),imageView);
                break;
            case 'D':
                textView.setText("对不起，您的个人信息未能通过审核，您可以拨打客服热线进行咨询。");
                break;
        }


        buttonTelephone.setOnClickListener(new TelephoneClickListener(this));
    }
}