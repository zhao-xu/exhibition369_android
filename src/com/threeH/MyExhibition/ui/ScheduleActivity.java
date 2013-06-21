package com.threeH.MyExhibition.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import com.threeH.MyExhibition.R;

/**
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 13-6-19
 * Time: 上午11:40
 * To change this template use File | Settings | File Templates.
 */
public class ScheduleActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.webview);
    }
}