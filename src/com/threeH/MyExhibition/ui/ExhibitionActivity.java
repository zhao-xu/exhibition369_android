package com.threeH.MyExhibition.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.tools.MobileConfig;
import com.threeH.MyExhibition.tools.PixelDpHelper;
import com.threeH.MyExhibition.tools.Tool;

/**
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 13-6-19
 * Time: 上午10:38
 * To change this template use File | Settings | File Templates.
 */
public class ExhibitionActivity extends TabActivity implements ActivityInterface{
    private static final String NEWS = "news";
    private static final String SCHEDULE = "schedule";
    private static final String BRIEF = "brief";
    private static final String MESSAGE = "message";
    private static final String QRCODE = "qrcode";
    private TabHost mTabhost;
    private RadioGroup mRadiogroup;
    private ImageView mImgviewNewMessage;
    private Exhibition mExhibiton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.exhibition_tab_page);
        initdata();
        findView();
        addAction();
    }

    @Override
    public void findView() {
        mRadiogroup = (RadioGroup) this.findViewById(R.id.rg_tabs_btns);
        mImgviewNewMessage = (ImageView) this.findViewById(R.id.imageview_newmessage);
    }

    @Override
    public void initdata() {
        mTabhost = this.getTabHost();
        mExhibiton = (Exhibition) getIntent().getExtras().get("exhibition");
        initTabhost();
    }

    @Override
    public void addAction() {
        mTabhost.setCurrentTabByTag(BRIEF);
        mRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_news:
                        mTabhost.setCurrentTabByTag(NEWS);
                        break;
                    case R.id.rb_show:
                        mTabhost.setCurrentTabByTag(BRIEF);
                        break;
                    case R.id.rb_store:
                        mTabhost.setCurrentTabByTag(SCHEDULE);
                        break;
                    case R.id.rb_member:
                        mTabhost.setCurrentTabByTag(MESSAGE);
                        break;
                    case R.id.rb_more:
                        mTabhost.setCurrentTabByTag(QRCODE);
                        break;
                    default:
                        break;
                }
            }
        });

        if(mExhibiton.getCount() > 0){
            mImgviewNewMessage.setVisibility(View.VISIBLE);
            setPointPosition(mImgviewNewMessage,4);
        }
    }

    /**
     * 初始化tabhost中的tab
     */
    private void initTabhost(){
        mTabhost.addTab(mTabhost.newTabSpec(BRIEF).setIndicator(BRIEF)
                .setContent(new Intent (this, ExhibitionBriefActivity.class)
                        .putExtra("url", Tool.ASSET_SERVER + mExhibiton.getExKey() + "/brief.html")
                        .putExtra("exhibition", mExhibiton)));
        mTabhost.addTab(mTabhost.newTabSpec(SCHEDULE).setIndicator(SCHEDULE)
                .setContent(new Intent(this, ShowHtmlActivity.class)
                        .putExtra("url",Tool.ASSET_SERVER + mExhibiton.getExKey() + "/schedule.html")
                        .putExtra("exhibition", mExhibiton)));
        mTabhost.addTab(mTabhost.newTabSpec(NEWS).setIndicator(NEWS)
                .setContent(new Intent(this, NewsPageActivity.class)
                        .putExtra("exhibition",mExhibiton)));
        mTabhost.addTab(mTabhost.newTabSpec(MESSAGE).setIndicator(MESSAGE)
                .setContent(new Intent(this, MessageActivity.class)
                        .putExtra("exhibition",mExhibiton)));
        mTabhost.addTab(mTabhost.newTabSpec(QRCODE).setIndicator(QRCODE)
                .setContent(new Intent(this, QrCodeActivity.class)
                        .putExtra("exhibition",mExhibiton)));
    }

    /** 导航栏显示红点的位置设置. */
    public void setPointPosition(ImageView imageView, int mIndex) {
        int mWidth = MobileConfig.getMobileConfig(getApplicationContext()).getWidth();
        RelativeLayout.LayoutParams mParam = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        mParam.setMargins(
                mWidth / 5 * mIndex - PixelDpHelper.dip2px(getApplicationContext(),25),
                PixelDpHelper.dip2px(getApplicationContext(), 10), 0, 0);
        imageView.setLayoutParams(mParam);
    }


}