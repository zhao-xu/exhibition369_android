package com.threeH.MyExhibition.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.*;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.HomePageEnrollListAdapter;
import com.threeH.MyExhibition.entities.AuditingStatus;
import com.threeH.MyExhibition.entities.UnEnrollExhibition;
import com.threeH.MyExhibition.listener.TelephoneClickListener;
import com.threeH.MyExhibition.service.ClientController;
import com.threeH.MyExhibition.tools.Tool;

import java.util.HashMap;
import java.util.List;

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
    private ClientController clientController;
    private char  singupStatus;
    private String strExAddress;
    private String strExDate;
    private RadioButton radioButtonNews;
    private String token;
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
        radioButtonNews = (RadioButton) this.findViewById(R.id.rb_news);
    }

    @Override
    public void initdata() {
        tabhost  = this.getTabHost();
        clientController = ClientController.getController(this);
        exKey = getIntent().getStringExtra("exKey");
        strExAddress = getIntent().getStringExtra("exAddress");
        strExDate = getIntent().getStringExtra("exTime");
        token = getIntent().getStringExtra("token");
        singupStatus = getSignupStatus(exKey,token);
        TabHost.TabSpec newSpec = tabhost.newTabSpec(NEWS_TAB).setIndicator(NEWS_TAB)
                .setContent(new Intent(this, NewsPageActivity.class).putExtra("exKey",exKey));
        TabHost.TabSpec showSpec = tabhost.newTabSpec(SUMMARY_TAB).setIndicator(SUMMARY_TAB)
                .setContent(new Intent (this, ShowHtmlActivity.class)
                        .putExtra("url","http://180.168.35.37:8080/e369_asset/"+ exKey + "/brief.html")
                        .putExtra("title","会展介绍"));
        TabHost.TabSpec homeSpec = tabhost.newTabSpec(SCHEDULE_TAB)
                .setIndicator(SCHEDULE_TAB)
                .setContent(new Intent(this, ShowHtmlActivity.class)
                        .putExtra("url","http://180.168.35.37:8080/e369_asset/"+ exKey + "/schedule.html")
                        .putExtra("title","日程"));
        TabHost.TabSpec memberSpec = tabhost.newTabSpec(MESSAGE_TAB)
                .setIndicator(MESSAGE_TAB)
                .setContent(new Intent(this, MessageActivity.class)
                        .putExtra("exhibitionKey",exKey));
        TabHost.TabSpec moreSpec = tabhost.newTabSpec(TWODCODE_TAB).setIndicator(TWODCODE_TAB)
                .setContent(new Intent(this, QrCodeActivity.class)
                        .putExtra("exhibitionKey",exKey)
                        .putExtra("singupStatus",singupStatus)
                        .putExtra("exAddress",strExAddress)
                        .putExtra("exTime",strExDate));
        tabhost.addTab(showSpec);
        tabhost.addTab(newSpec);
        tabhost.addTab(homeSpec);
        tabhost.addTab(memberSpec);
        tabhost.addTab(moreSpec);
    }

    @Override
    public void addAction() {
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
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

    private char getSignupStatus(String exKey, String token) {
        try {
            String strSignupStatusData = clientController.getService().getSignupStatus(exKey, token);
            AuditingStatus jsonSignupStatusData = new Gson().fromJson(strSignupStatusData,AuditingStatus.class);
            if(null != jsonSignupStatusData){
                 return jsonSignupStatusData.getStatus().charAt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ' ';
    }

}
