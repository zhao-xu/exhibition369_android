package com.threeH.MyExhibition.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.listener.SignupClickListener;
import com.threeH.MyExhibition.listener.TelephoneClickListener;
import com.threeH.MyExhibition.service.FileService;
import com.threeH.MyExhibition.tools.ImageURLUtil;
import com.threeH.MyExhibition.tools.MSYH;
import com.threeH.MyExhibition.tools.NetworkHelper;
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
    private TextView textViewTitle;
    private TextView textViewAddress,textViewTime;
    private TextView textViewTheme, textViewDate,textViewAddressUp,textViewSponsor;
    private TextView textView;
    private ImageView imageViewIcon, imageViewSingup,imageviewSignup2;
    Typeface typeface;

    private String strExhibitionKey,strExAddress,strExDate,strExTheme,strExSponser;
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
        imageViewSingup = (ImageView) this.findViewById(R.id.imageview_attention);
        imageviewSignup2 = (ImageView) this.findViewById(R.id.exhibition_titlebar_signup);
        textView = (TextView) this.findViewById(R.id.qrcode_textview_prompt);
        textViewTitle = (TextView) this.findViewById(R.id.exhibition_titlebar_textview_title);
        imageViewIcon = (ImageView) this.findViewById(R.id.imageview_icon);
        textViewAddress = (TextView) this.findViewById(R.id.qrcode_textview_exaddress);
        textViewTime = (TextView) this.findViewById(R.id.qrcode_textview_extime);
        textViewTheme = (TextView) this.findViewById(R.id.exhibition_theme);
        textViewDate = (TextView) this.findViewById(R.id.exhibition_date);
        textViewAddressUp = (TextView) this.findViewById(R.id.exhibition_address);
        textViewSponsor = (TextView) this.findViewById(R.id.exhibition_sponsor);
    }

    @Override
    public void initdata(){
//        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/msyh.ttf");
        typeface = MSYH.getInstance(context.getApplicationContext()).getNormal();
        charSingupStatus = getIntent().getCharExtra("singupStatus", ' ');
        strExhibitionKey = getIntent().getStringExtra("exhibitionKey");
        strExAddress = getIntent().getStringExtra("exAddress");
        strExDate = getIntent().getStringExtra("exTime");
        strExTheme = getIntent().getStringExtra("exTheme");
        strExSponser = getIntent().getStringExtra("exSponser");
    }

    @Override
    public void addAction() {
        textViewTitle.setTypeface(typeface);
        textViewTitle.setText("二维码");
        imageviewTelephone.setOnClickListener(new TelephoneClickListener(this,tel_nummber));
        ImageURLUtil.loadImage(Tool.makeExhibitionIconURL(strExhibitionKey),imageViewIcon);
        textViewAddress.setText(strExAddress);
        textViewTime.setText(strExDate);
        imageViewSingup.setVisibility(View.GONE);
        switch (charSingupStatus){
            case ' ':
            case 'N':
                textView.setText("对不起您还没有报名参加此展会，请报名！");
                break;
            case 'P':
                textView.setText("您的个人信息还在审核中，请耐心等待。。。");
                imageviewSignup2.setVisibility(View.GONE);
                break;
            case 'A':
                loadQrcode();
                imageviewSignup2.setVisibility(View.GONE);
                break;
            case 'D':
                textView.setText("对不起，您的个人信息未能通过审核，您可以拨打客服热线进行咨询。");
                break;
        }
        textViewTheme.setText(strExTheme);
        textViewDate.setText(strExDate);
        textViewAddressUp.setText(strExAddress);
        textViewSponsor.setText(strExSponser);
        imageviewSignup2.setOnClickListener(new SignupClickListener(QrCodeActivity.this,strExhibitionKey));
    }

    private void loadQrcode() {
         if(NetworkHelper.getInstance(context).isConnected()){
           ImageURLUtil.loadImage(Tool.makeQrcodeURL(strExhibitionKey,token), imageViewQrcode);
        }else{
            FileService service = new FileService(context);
            byte[] data;
            try {
                String filename = Environment.getExternalStorageDirectory() + "/" + strExhibitionKey + "qrcode.png";
                data = service.read(filename);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                imageViewQrcode.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}