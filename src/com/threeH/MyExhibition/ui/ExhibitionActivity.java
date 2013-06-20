package com.threeH.MyExhibition.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.*;
import com.threeH.MyExhibition.R;

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
    private TextView titleTV;
    private SharedPreferences pres;
    private final static int RESULT_CODE = 1;
    private String exKey;
    private RadioGroup radiogroup;
    //消息，二维码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
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
    }

    @Override
    public void initdata() {
        tabhost  = this.getTabHost();
        exKey = getIntent().getStringExtra("exKey");
        TabHost.TabSpec newSpec = tabhost.newTabSpec(NEWS_TAB).setIndicator(NEWS_TAB)
                .setContent(new Intent(this, NewsPageActivity.class).putExtra("exKey",exKey));
        TabHost.TabSpec showSpec = tabhost.newTabSpec(SUMMARY_TAB).setIndicator(SUMMARY_TAB)
                .setContent(new Intent (this, ShowHtmlActivity.class)
                        .putExtra("url","http://180.168.35.37:8080/e369_asset/"+ exKey + "/brief.html"));
        TabHost.TabSpec homeSpec = tabhost.newTabSpec(SCHEDULE_TAB)
                .setIndicator(SCHEDULE_TAB)
                .setContent(new Intent(this, ShowHtmlActivity.class)
                        .putExtra("url","http://180.168.35.37:8080/e369_asset/"+ exKey + "/schedule.html"));
        TabHost.TabSpec memberSpec = tabhost.newTabSpec(MESSAGE_TAB)
                .setIndicator(MESSAGE_TAB)
                .setContent(new Intent(this, MessageActivity.class));
        TabHost.TabSpec moreSpec = tabhost.newTabSpec(TWODCODE_TAB).setIndicator(TWODCODE_TAB)
                .setContent(new Intent(this, QrDCodeActivity.class));
        tabhost.addTab(newSpec);
        tabhost.addTab(showSpec);
        tabhost.addTab(homeSpec);
        tabhost.addTab(memberSpec);
        tabhost.addTab(moreSpec);
    }

    @Override
    public void addAction() {
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
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
    }
}