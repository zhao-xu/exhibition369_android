package com.threeH.MyExhibition.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.cache.XmlDB;
import com.threeH.MyExhibition.common.StringPools;
import com.threeH.MyExhibition.entities.AuditingStatus;
import com.threeH.MyExhibition.entities.OverAllConfig;
import com.threeH.MyExhibition.service.ClientController;
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
    private static final String NEWS_TAB = "news";
    private static final String SCHEDULE_TAB = "schedule";
    private static final String SUMMARY_TAB = "summary";
    private static final String MESSAGE_TAB = "message";
    private static final String TWODCODE_TAB = "2dcode";
    private TabHost tabhost;
    private String exKey;
    private RadioGroup radiogroup;
    private char singupStatus;
    private String strExAddress,strExDate,strTheme,strSponser,strSing;
    private int count;
    private ImageView imageViewNewMessage;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void findView() {
        radiogroup = (RadioGroup) this.findViewById(R.id.rg_tabs_btns);
        imageViewNewMessage = (ImageView) this.findViewById(R.id.imageview_newmessage);
    }

    @Override
    public void initdata() {
        tabhost  = this.getTabHost();
        exKey = getIntent().getStringExtra("exKey");
        strExAddress = getIntent().getStringExtra("exAddress");
        strExDate = getIntent().getStringExtra("exTime");
        strTheme  = getIntent().getStringExtra("exTheme");
        strSponser  = getIntent().getStringExtra("exSponser");
        singupStatus = getIntent().getCharExtra("singupStatus",' ');
        count = getIntent().getIntExtra("count",0);
        TabHost.TabSpec newSpec = tabhost.newTabSpec(NEWS_TAB).setIndicator(NEWS_TAB)
                .setContent(new Intent(this, NewsPageActivity.class)
                        .putExtra("exKey", exKey)
                        .putExtra("singupStatus", singupStatus));
        TabHost.TabSpec showSpec = tabhost.newTabSpec(SUMMARY_TAB).setIndicator(SUMMARY_TAB)
                .setContent(new Intent (this, ExhibitionBriefActivity.class)
                        .putExtra("url", Tool.ASSET_SERVER + exKey + "/brief.html")
                        .putExtra("title", "展会介绍")
                        .putExtra("exKey", exKey)
                        .putExtra("singupStatus", singupStatus)
                        .putExtra("theme",strTheme));
        TabHost.TabSpec homeSpec = tabhost.newTabSpec(SCHEDULE_TAB)
                .setIndicator(SCHEDULE_TAB)
                .setContent(new Intent(this, ShowHtmlActivity.class)
                        .putExtra("url",Tool.ASSET_SERVER + exKey + "/schedule.html")
                        .putExtra("title", "日程")
                        .putExtra("exKey", exKey)
                        .putExtra("singupStatus", singupStatus));
        TabHost.TabSpec memberSpec = tabhost.newTabSpec(MESSAGE_TAB)
                .setIndicator(MESSAGE_TAB)
                .setContent(new Intent(this, MessageActivity.class)
                        .putExtra("exhibitionKey",exKey)
                        .putExtra("singupStatus", singupStatus));
        TabHost.TabSpec moreSpec = tabhost.newTabSpec(TWODCODE_TAB).setIndicator(TWODCODE_TAB)
                .setContent(new Intent(this, QrCodeActivity.class)
                        .putExtra("exhibitionKey",exKey)
                        .putExtra("singupStatus", singupStatus)
                        .putExtra("exAddress",strExAddress)
                        .putExtra("exTime",strExDate)
                        .putExtra("exTheme",strTheme)
                        .putExtra("exSponser",strSponser));
        tabhost.addTab(showSpec);
        tabhost.addTab(newSpec);
        tabhost.addTab(homeSpec);
        tabhost.addTab(memberSpec);
        tabhost.addTab(moreSpec);
    }

    @Override
    public void addAction() {
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId){
                switch (checkedId) {
                    case R.id.rb_news:
                        tabhost.setCurrentTabByTag(NEWS_TAB);
                        break;
                    case R.id.rb_show:
                        tabhost.setCurrentTabByTag(SUMMARY_TAB);
                        break;
                    case R.id.rb_store:
                        tabhost.setCurrentTabByTag(SCHEDULE_TAB);
                        break;
                    case R.id.rb_member:
                        tabhost.setCurrentTabByTag(MESSAGE_TAB);
                        break;
                    case R.id.rb_more:
                        tabhost.setCurrentTabByTag(TWODCODE_TAB);
                        break;
                    default:
                        break;
                }
            }
        });

        if(count > 0){
            imageViewNewMessage.setVisibility(View.VISIBLE);
            setPointPosition(imageViewNewMessage,4);
        }
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

    /**
     * 获取后台资源服务器的URL地址
     * @return
     */
    private String getAssetServer(){
        Gson gson = new Gson();
        OverAllConfig mOverAllConfig = gson.fromJson(XmlDB.getInstance(this).
                getKeyStringValue(StringPools.OVERALL_CONFIG, ""),OverAllConfig.class);
        if(null != mOverAllConfig){
            return mOverAllConfig.getAssetServer();
        }
        return null;
    }
}