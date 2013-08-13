package com.threeH.MyExhibition.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.google.gson.Gson;
import com.google.zxing.client.android.CaptureActivity;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.cache.XmlDB;
import com.threeH.MyExhibition.common.StringPools;
import com.threeH.MyExhibition.entities.OverAllConfig;
import com.threeH.MyExhibition.listener.TelephoneClickListener;
import com.threeH.MyExhibition.tools.ByteUtil;
import com.threeH.MyExhibition.tools.Tool;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-7-29
 * Time: 下午3:11
 */
public class HomeActivity extends TabActivity implements ActivityInterface {
    private TabHost mTabhost;
    private RadioGroup mRadiogroup;
    private static final String MYEXHIBITION_TAB = "myexhibition";
    private static final String SEARCH_TAB = "search";
    private static final String SCAN_TAB = "scan";
    private static final String RECOMMOND_TAB = "recommond";
    private static final String ABOUT_TAB = "about";
    private ImageView mImgviewTelephone,mImgviewReturn;
    private TextView mTxvTitle;
    private String mStrScan;
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
        mTxvTitle = (TextView) this.findViewById(R.id.exhibition_titlebar_txt_title);
        mRadiogroup = (RadioGroup) this.findViewById(R.id.home_radiogroup);
        mImgviewTelephone = (ImageView) this.findViewById(R.id.exhibition_titlebar_btn_telephone);
        mImgviewReturn = (ImageView) this.findViewById(R.id.exhibition_titlebar_return);
    }

    @Override
    public void initdata() {
        initTabhost();
        mStrScan = getIntent().getStringExtra("result");
    }

    @Override
    public void addAction() {
        mImgviewReturn.setVisibility(View.GONE);
        mImgviewTelephone.setOnClickListener(
                new TelephoneClickListener(this, Tool.getTelephone(getApplicationContext())));
        mTxvTitle.setText(R.string.myexhibition);
        mRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.home_rdobtn_myexhibition:
                        changeTab(MYEXHIBITION_TAB, R.string.myexhibition,R.id.home_rdobtn_myexhibition);
                        break;
                    case R.id.home_rdobtn_search:
                        changeTab(SEARCH_TAB, R.string.search,R.id.home_rdobtn_search);
                        break;
                    case R.id.home_rdobtn_scan:
                        changeTab(SCAN_TAB,R.string.scan,R.id.home_rdobtn_scan);
                        break;
                    case R.id.home_rdobtn_recommond:
                        changeTab(RECOMMOND_TAB, R.string.recommond,R.id.home_rdobtn_recommond);
                        break;
                    case R.id.home_rdobtn_config:
                        changeTab(ABOUT_TAB, R.string.about,R.id.home_rdobtn_config);
                        break;
                }
            }
        });
        setTabAfterScan();
    }

    /**
     * 扫描后切换到搜一搜页面
     */
    private void setTabAfterScan(){
        if(mStrScan != null && mStrScan.startsWith("MEK://")){
            ExhibitionListActivity.mStrScanExKey = decodeExhibitionKey(mStrScan.substring(6));
            changeTab(SEARCH_TAB, R.string.search,R.id.home_rdobtn_search);
        }else{
            mTabhost.setCurrentTabByTag(MYEXHIBITION_TAB);
        }
    }

    /**
     * 初始化tabhost中的tab
     */
    private void initTabhost(){
        mTabhost = this.getTabHost();
        mTabhost.addTab(mTabhost.newTabSpec(MYEXHIBITION_TAB).setIndicator(MYEXHIBITION_TAB)
                .setContent(new Intent(this, SignupExhiListActivity.class)));
        mTabhost.addTab(mTabhost.newTabSpec(SEARCH_TAB).setIndicator(SEARCH_TAB)
                .setContent(new Intent(this, ExhibitionListActivity.class)));
        mTabhost.addTab(mTabhost.newTabSpec(SCAN_TAB).setIndicator(SCAN_TAB)
                .setContent(new Intent(this,CaptureActivity.class)));
        mTabhost.addTab(mTabhost.newTabSpec(RECOMMOND_TAB).setIndicator(RECOMMOND_TAB)
                .setContent(new Intent(this, RecommondActivity.class)));
        mTabhost.addTab(mTabhost.newTabSpec(ABOUT_TAB).setIndicator(ABOUT_TAB)
                .setContent(new Intent(this, AboutActivity.class)));
    }

    /**
     * tab切换时需要做的动作
     * 设置当前的tab
     * 设置标题
     */
    private void changeTab(String tab, int title,int id){
        mTabhost.setCurrentTabByTag(tab);
        mTxvTitle.setText(title);
        mRadiogroup.check(id);
    }

    /**
     * 将二维码扫描的字符串解码成展会标识
     * @param qrcode
     * @return
     */
    private String decodeExhibitionKey(String qrcode){
        String exKey = "";
        ByteBuffer buffer = ByteBuffer.allocate(200);
        buffer.put(ByteUtil.ascii2byte(qrcode));
        buffer.flip();
        short size = buffer.getShort();
        byte[] bs = new byte[size];
        buffer.get(bs);
        try {
            exKey = new String(bs,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return exKey;
    }
}
