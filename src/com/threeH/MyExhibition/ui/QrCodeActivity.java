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
import com.threeH.MyExhibition.entities.Exhibition;
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
    private ImageView mImgviewQrcode;
    private TextView mTxtAddress, mTxtTime;
    private TextView mTxtTheme, mTxtDate, mTxtAddressUp, mTxtSponsor;
    private TextView mTxtPrompt;
    private ImageView mImgviewIcon;
    private Exhibition mExhibition;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.qrcode);
        initdata();
        findView();
        addAction();
    }
    @Override
    public void findView(){
        mImgviewQrcode = (ImageView) this.findViewById(R.id.qrcode_imageview);
        mTxtPrompt = (TextView) this.findViewById(R.id.qrcode_textview_prompt);
        mImgviewIcon = (ImageView) this.findViewById(R.id.imageview_icon);
        mTxtAddress = (TextView) this.findViewById(R.id.qrcode_textview_exaddress);
        mTxtTime = (TextView) this.findViewById(R.id.qrcode_textview_extime);
        mTxtTheme = (TextView) this.findViewById(R.id.exhibition_theme);
        mTxtDate = (TextView) this.findViewById(R.id.exhibition_date);
        mTxtAddressUp = (TextView) this.findViewById(R.id.exhibition_address);
        mTxtSponsor = (TextView) this.findViewById(R.id.exhibition_sponsor);
    }

    @Override
    public void initdata(){
        mExhibition = (Exhibition) getIntent().getExtras().get("exhibition");
    }

    @Override
    public void addAction() {
        ImageURLUtil.loadImage(Tool.makeExhibitionIconURL(mExhibition.getExKey()), mImgviewIcon);
        mTxtAddress.setText(mExhibition.getAddress());
        mTxtTime.setText(mExhibition.getDate());
        String status = mExhibition.getStatus() + " ";
        char c = status.charAt(0);
        switch (c){
            case ' ':
            case 'N':
                mTxtPrompt.setText("对不起您还没有报名参加此展会，请报名！");
                break;
            case 'P':
                mTxtPrompt.setText("您的个人信息还在审核中，请耐心等待。。。");
                break;
            case 'A':
                loadQrcode();
                break;
            case 'D':
                mTxtPrompt.setText("对不起，您的个人信息未能通过审核，您可以拨打客服热线进行咨询。");
                break;
        }
        mTxtTheme.setText(mExhibition.getName());
        mTxtDate.setText(mExhibition.getDate());
        mTxtAddressUp.setText(mExhibition.getAddress());
        mTxtSponsor.setText(mExhibition.getOrganizer());
    }

    private void loadQrcode() {
         if(NetworkHelper.getInstance(context).isConnected()){
           ImageURLUtil.loadImage(Tool.makeQrcodeURL(mExhibition.getExKey(),token), mImgviewQrcode);
        }else{
            FileService service = new FileService(context);
            byte[] data;
            try {
                String filename = Environment.getExternalStorageDirectory() +
                        "/" + mExhibition.getExKey() + "qrcode.png";
                data = service.read(filename);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                mImgviewQrcode.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}