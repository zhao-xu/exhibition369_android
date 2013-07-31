package com.threeH.MyExhibition.ui;

import android.os.Bundle;
import com.threeH.MyExhibition.R;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-7-31
 * Time: 上午9:22
 * To change this template use File | Settings | File Templates.
 */
public class AboutActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.about);
    }
}
