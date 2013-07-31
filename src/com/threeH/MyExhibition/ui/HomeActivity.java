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
    private ImageView  mImgviewSignup,mImgviewTelephone;
    private TextView mTxvTitle;
    private RadioButton mRdobtnScan;
    private int mIDRdobtn = R.id.home_rdobtn_myexhibition;
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
        mTxvTitle = (TextView) this.findViewById(R.id.exhibition_titlebar_txt_title);
        mRadiogroup = (RadioGroup) this.findViewById(R.id.home_radiogroup);
        mImgviewTelephone = (ImageView) this.findViewById(R.id.exhibition_titlebar_btn_telephone);
        mRdobtnScan = (RadioButton) this.findViewById(R.id.home_rdobtn_scan);
    }

    @Override
    public void initdata() {
        initTabhost();
    }

    @Override
    public void addAction() {
        mImgviewSignup.setVisibility(View.GONE);
        mImgviewTelephone.setOnClickListener(new TelephoneClickListener(this,getTelephone()));
        mTxvTitle.setText(R.string.myexhibition);
        mTabhost.setCurrentTabByTag(MYEXHIBITION_TAB);
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
                        Intent intent = new Intent(HomeActivity.this, CaptureActivity.class);
                        startActivityForResult(intent,1);
                        mRadiogroup.check(mIDRdobtn);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            String result = data.getStringExtra("result");
            if(result != null && result.startsWith("MEK://")){
                ExhibitionListActivity.mStrScanExKey = decodeExhibitionKey(result.substring(6));
                changeTab(SEARCH_TAB, R.string.search,R.id.home_rdobtn_search);
                mRadiogroup.check(R.id.home_rdobtn_search);
            }
        }
    }

    /**
     * 获取保存在全局配置中的电话号码
     * @return
     */
    private String getTelephone(){
        Gson gson = new Gson();
        OverAllConfig mOverAllConfig = gson.fromJson(XmlDB.getInstance(this).
                getKeyStringValue(StringPools.OVERALL_CONFIG, ""),OverAllConfig.class);
        if(null != mOverAllConfig){
             return mOverAllConfig.getTel();
        }
        return null;
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
                .setContent(new Intent(this,AboutActivity.class)));
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
        mIDRdobtn = id;
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
