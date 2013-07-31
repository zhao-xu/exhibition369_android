package com.threeH.MyExhibition.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.threeH.MyExhibition.common.StringPools;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.tools.SharedPreferencesUtil;
import com.threeH.MyExhibition.ui.HomeActivity;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-7-31
 * Time: 下午2:23
 * To change this template use File | Settings | File Templates.
 */
public class AttentionClickListener implements View.OnClickListener{
    private Exhibition mExhibition;
    private Context mContext;

    public AttentionClickListener(Context mContext, Exhibition mExhibition) {
        this.mContext = mContext;
        this.mExhibition = mExhibition;
    }

    @Override
    public void onClick(View v) {
        mExhibition.setAttention(true);
        SharedPreferencesUtil.saveObject(mExhibition, mContext, StringPools.SCAN_EXHIBITION_DATA);
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
