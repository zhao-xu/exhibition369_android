package com.threeH.MyExhibition.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.listener.TelephoneClickListener;
import com.threeH.MyExhibition.tools.MSYH;
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
    private ImageView mImgviewNewMessage,mImgviewReturn;
    private Exhibition mExhibiton;
    private Typeface mTypeface;
    private ImageView mImgviewTelephone;
    private TextView mTxtTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        mImgviewTelephone = (ImageView) this.findViewById(R.id.exhibition_titlebar_btn_telephone);
        mTxtTitle = (TextView) this.findViewById(R.id.exhibition_titlebar_txt_title);
        mImgviewReturn = (ImageView) this.findViewById(R.id.exhibition_titlebar_return);
    }

    @Override
    public void initdata() {
        mTabhost = this.getTabHost();
        mTypeface = MSYH.getInstance(this).getNormal();
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
                        changeTab(NEWS,R.string.news);
                        break;
                    case R.id.rb_show:
                        changeTab(BRIEF,R.string.exhibition_brief);
                        break;
                    case R.id.rb_store:
                        changeTab(SCHEDULE,R.string.schedule);
                        break;
                    case R.id.rb_member:
                        changeTab(MESSAGE,R.string.message);
                        break;
                    case R.id.rb_more:
                        changeTab(QRCODE,R.string.qrcode);
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
        mTxtTitle.setText(R.string.exhibition_brief);
        mTxtTitle.setTypeface(mTypeface);
        mImgviewTelephone.setOnClickListener(
                new TelephoneClickListener(this,Tool.getTelephone(getApplicationContext())));
        mImgviewReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                        .putExtra("exhibition", mExhibiton)
                        .putExtra("isHiddenTitleBar",true)));
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
    /**
     * tab切换时需要做的动作
     * 设置当前的tab
     * 设置标题
     */
    private void changeTab(String tab, int title){
        mTabhost.setCurrentTabByTag(tab);
        mTxtTitle.setText(title);
    }

    /** 导航栏显示红点的位置设置.*/
    public void setPointPosition(ImageView imageView, int mIndex) {
        int mWidth = MobileConfig.getMobileConfig(getApplicationContext()).getWidth();
        RelativeLayout.LayoutParams mParam = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        mParam.setMargins(
                mWidth / 5 * mIndex - PixelDpHelper.dip2px(getApplicationContext(),25),
                PixelDpHelper.dip2px(getApplicationContext(), 10), 0, 0);
        imageView.setLayoutParams(mParam);
    }

}