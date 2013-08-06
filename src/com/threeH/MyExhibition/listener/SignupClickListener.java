package com.threeH.MyExhibition.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.threeH.MyExhibition.ui.SignupActivity;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-6-27
 * Time: 下午4:06
 * To change this template use File | Settings | File Templates.
 */
public class SignupClickListener implements View.OnClickListener {
    private Context context;
    private String exKey;
    public SignupClickListener(Context context,String exKey) {
        this.context = context;
        this.exKey = exKey;
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, SignupActivity.class);
        intent.putExtra("exKey",exKey);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
