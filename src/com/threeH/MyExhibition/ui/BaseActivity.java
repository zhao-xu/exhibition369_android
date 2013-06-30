package com.threeH.MyExhibition.ui;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.threeH.MyExhibition.cache.XmlDB;
import com.threeH.MyExhibition.common.StringPools;
import com.threeH.MyExhibition.entities.OverAllConfig;
import com.threeH.MyExhibition.service.ClientController;
import com.threeH.MyExhibition.widget.MyDialog;

/**
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 13-6-8
 * Time: 上午10:52
 * To change this template use File | Settings | File Templates.
 */
public class BaseActivity extends Activity {
    protected Gson mGson = new Gson();
    protected MyApplication context;
    protected Resources resources;
    protected XmlDB xmlDB;
    public MyDialog mProDialog;
    public ImageView mAnimView;
    public OverAllConfig mOverAllConfig;
    public String token;
    public String tel_nummber;
    public String assetServer;
    public ClientController mController;
    public Handler mHandler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (MyApplication) getApplication();
        mHandler = new Handler();
        resources = getResources();
        xmlDB = XmlDB.getInstance(this);
        mProDialog = new MyDialog(this, false);
        mProDialog.setCanceledOnTouchOutside(false);
        if(null!=mGson.fromJson(xmlDB.getKeyStringValue(StringPools.OVERALL_CONFIG,""),OverAllConfig.class)){
            mOverAllConfig = mGson.fromJson(xmlDB.getKeyStringValue(StringPools.OVERALL_CONFIG,""),OverAllConfig.class);
            tel_nummber = mOverAllConfig.getTel();
            token = mOverAllConfig.getToken();
            assetServer = mOverAllConfig.getAssetServer();
        }
        mController = ClientController.getController(this);
    }

    public void setContentViewWithNoTitle(int contentresid) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(contentresid);
    }

    protected void showShortText(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

    public void showShortText(CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    public void showLongText(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();
    }

    public void showLongText(CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void getConacts() {
    }

    public void hideBaseDialog() {
        try {
            if (mProDialog.isShowing()) {
                mProDialog.dismiss();
            }
        } catch (Exception e) {
        }
    }

    public void showBaseDialog() {
        try {
            if (mProDialog.isShowing()) {
                mProDialog.dismiss();
                mProDialog.show();
            } else {
                mProDialog.show();
            }
        } catch (Exception e) {
            ;
        }
    }
}