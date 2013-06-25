package com.threeH.MyExhibition.listener;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-6-21
 * Time: 上午10:06
 * To change this template use File | Settings | File Templates.
 */
public class TelephoneClickListener implements View.OnClickListener {
    private Context context;
    private String telephone;
    public TelephoneClickListener(Context context,String telephone) {
        this.context = context;
        this.telephone = telephone;
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telephone));
        context.startActivity(intent);
    }
}
