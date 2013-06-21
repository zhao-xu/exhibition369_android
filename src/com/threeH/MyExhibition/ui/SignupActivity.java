package com.threeH.MyExhibition.ui;

import android.os.Bundle;
import android.view.Window;
import com.threeH.MyExhibition.R;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-6-20
 * Time: 下午1:45
 * To change this template use File | Settings | File Templates.
 */
public class SignupActivity extends  BaseActivity  {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.signup_page);
    }
}
