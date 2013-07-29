package com.threeH.MyExhibition.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import com.google.zxing.client.android.CaptureActivity;
import com.threeH.MyExhibition.R;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-7-29
 * Time: 下午3:11
 * To change this template use File | Settings | File Templates.
 */
public class HomeActivity extends TabActivity implements ActivityInterface {
    private TabHost mTabhost;
    private RadioGroup mRadiogroup;
    private static final String MYEXHIBITION_TAB = "myexhibition";
    private static final String SEARCH_TAB = "search";
    private static final String SCAN_TAB = "scan";
    private static final String RECOMMOND_TAB = "recommond";
    private static final String CONFIG_TAB = "config";
    private ImageView  mImgviewSignup;
    private TextView mTxvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home);
        findView();
        initdata();
        addAction();
    }

    @Override
    public void findView() {
        mImgviewSignup = (ImageView) this.findViewById(R.id.exhibition_titlebar_signup);
        mTxvTitle = (TextView) this.findViewById(R.id.exhibition_titlebar_textview_title);
        mRadiogroup = (RadioGroup) this.findViewById(R.id.home_radiogroup);

    }

    @Override
    public void initdata() {
        initTabhost();
    }


    @Override
    public void addAction() {
        mImgviewSignup.setVisibility(View.GONE);
        mTxvTitle.setText(R.string.myexhibition);
        mTabhost.setCurrentTabByTag(MYEXHIBITION_TAB);
        mRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.home_rdobtn_myexhibition:
                        mTabhost.setCurrentTabByTag(MYEXHIBITION_TAB);
                        break;
                    case R.id.home_rdobtn_search:
                        mTabhost.setCurrentTabByTag(SEARCH_TAB);
                        break;
                    case R.id.home_rdobtn_scan:
                        mTabhost.setCurrentTabByTag(SCAN_TAB);
                        Intent intent = new Intent(HomeActivity.this,CaptureActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.home_rdobtn_recommond:
                        mTabhost.setCurrentTabByTag(RECOMMOND_TAB);
                        break;
                    case R.id.home_rdobtn_config:
                        mTabhost.setCurrentTabByTag(CONFIG_TAB);
                        break;
                }
            }
        });
    }

    /**
     * 初始化tabhost中的tab
     */
    private void initTabhost(){
        mTabhost = this.getTabHost();
        mTabhost.addTab(mTabhost.newTabSpec(MYEXHIBITION_TAB).setIndicator(MYEXHIBITION_TAB)
                .setContent(new Intent(this, SignupExhiListActivity.class)));
        mTabhost.addTab(mTabhost.newTabSpec(SEARCH_TAB).setIndicator(SEARCH_TAB)
                .setContent(new Intent(this, NoSignupExhiListActivity.class)));
        mTabhost.addTab(mTabhost.newTabSpec(SCAN_TAB).setIndicator(SCAN_TAB)
                .setContent(new Intent(this,RecommondActivity.class)));
        mTabhost.addTab(mTabhost.newTabSpec(RECOMMOND_TAB).setIndicator(RECOMMOND_TAB)
                .setContent(new Intent(this, RecommondActivity.class)));
        mTabhost.addTab(mTabhost.newTabSpec(CONFIG_TAB).setIndicator(CONFIG_TAB)
                .setContent(new Intent(this, RecommondActivity.class)));
    }
}
