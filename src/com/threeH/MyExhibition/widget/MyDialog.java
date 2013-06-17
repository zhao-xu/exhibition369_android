package com.threeH.MyExhibition.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MyDialog extends ProgressDialog {

    private boolean isShowTxt = false;

    public MyDialog(Context context, boolean isShowTxt) {
        super(context);
        this.isShowTxt = isShowTxt;
    }

    public MyDialog(Context context, int theme) {
        super(context, theme);
    }

    AnimationDrawable mRefreshHeaderAnim;
    String mMsg;

    public void setMessage(String mMsg) {
        this.mMsg = mMsg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress);
        setScreenBrightness();
        this.setOnShowListener(new OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                try {
                    ImageView mLoading = (ImageView) MyDialog.this
                            .findViewById(R.id.mAnimView);
                    mRefreshHeaderAnim = (AnimationDrawable) mLoading
                            .getDrawable();
                    mRefreshHeaderAnim.start();
                    if (isShowTxt) {
                        TextView mLoadingTxt = (TextView) MyDialog.this
                                .findViewById(R.id.mLoadingTxt);
                        mLoadingTxt.setVisibility(View.VISIBLE);
                        if (mMsg != null) {
                            mLoadingTxt.setText(mMsg);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setScreenBrightness() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        /**
         * 此处设置亮度值。dimAmount代表黑暗数量，也就是昏暗的多少，设置为0则代表完全明亮。 范围是0.0到1.0
         */
        lp.dimAmount = 0;
        window.setAttributes(lp);
    }
}
