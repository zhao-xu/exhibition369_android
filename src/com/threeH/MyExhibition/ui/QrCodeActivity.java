package com.threeH.MyExhibition.ui;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
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
    private ImageView imageViewQrcode;
    private Bitmap bitmap;
    private ImageView imageviewTelephone;
    private char charSingupStatus;
    private String strExhibitionKey;
    private TextView textViewTitle;
    private TextView textViewAddress;
    private TextView textViewTime;
    private TextView textView;
    private ImageView imageViewSignup,imageViewIcon,imageViewSingup2;
    Typeface typeface;
    private String strExAddress,strExDate;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.qrcode);
        initdata();
        findView();
        addAction();
    }
    @Override
    public void findView(){
        imageViewQrcode = (ImageView) this.findViewById(R.id.qrcode_imageview);
        imageviewTelephone = (ImageView) this.findViewById(R.id.exhibition_titlebar_button_telephone);
        imageViewSignup = (ImageView) this.findViewById(R.id.exhibition_titlebar_signup);
        imageViewSingup2 = (ImageView) this.findViewById(R.id.imageview_signup);
        textView = (TextView) this.findViewById(R.id.qrcode_textview_prompt);
        textViewTitle = (TextView) this.findViewById(R.id.exhibition_titlebar_textview_title);
        imageViewIcon = (ImageView) this.findViewById(R.id.imageview_icon);
        textViewAddress = (TextView) this.findViewById(R.id.qrcode_textview_exaddress);
        textViewTime = (TextView) this.findViewById(R.id.qrcode_textview_extime);
    }

    @Override
    public void initdata(){
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/msyh.ttf");
        charSingupStatus = getIntent().getCharExtra("singupStatus",' ');
        strExhibitionKey = getIntent().getStringExtra("exhibitionKey");
        strExAddress = getIntent().getStringExtra("exAddress");
        strExDate = getIntent().getStringExtra("exTime");
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
                ImageURLUtil.loadImage(Tool.makeQrcodeURL(strExhibitionKey,token), imageViewQrcode);
                break;
            case 'D':
                textView.setText("对不起，您的个人信息未能通过审核，您可以拨打客服热线进行咨询。");
                break;
        }
        textViewTitle.setTypeface(typeface);
        textViewTitle.setText("二维码");
        imageviewTelephone.setOnClickListener(new TelephoneClickListener(this,tel_nummber));
        ImageURLUtil.loadImage(Tool.makeExhibitionIconURL(strExhibitionKey),imageViewIcon);
        textViewAddress.setText(strExAddress);
        textViewTime.setText(strExDate);
        imageViewSignup.setVisibility(View.GONE);
        imageViewSingup2.setVisibility(View.GONE);
    }
}