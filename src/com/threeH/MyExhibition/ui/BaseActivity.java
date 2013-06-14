package com.threeH.MyExhibition.ui;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;
import com.threeH.MyExhibition.cache.XmlDB;
import com.threeH.MyExhibition.widget.MyDialog;

/**
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 13-6-8
 * Time: 上午10:52
 * To change this template use File | Settings | File Templates.
 */
public class BaseActivity extends Activity {
    protected MyApplication context;
    protected Resources resources;
    protected XmlDB xmlDB;
    public MyDialog mProDialog;
    public ImageView mAnimView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (MyApplication) getApplication();

        resources = getResources();
        xmlDB = XmlDB.getInstance(this);
        mProDialog = new MyDialog(this,false);
        mProDialog.setCanceledOnTouchOutside(false);
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

    public void getConacts(){};

    public void hideBaseDialog() {
        try {
            if (mProDialog.isShowing()) {
                mProDialog.dismiss();
            }

        } catch (Exception e) {
            ;
        }
    }

    public void showBaseDialog(){
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