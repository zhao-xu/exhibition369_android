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
import com.threeH.MyExhibition.entities.AuditingStatus;
import com.threeH.MyExhibition.service.ClientController;

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
    private char singupStatus;
    private String strExAddress,strExDate,strTheme,strSponser,strSing;
    private RadioButton radioButtonNews,radioButtonQrcode,radioButtonMessage;
    private String token;
    private Gson mGson = new Gson();
    private int id = R.id.rb_show;
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
        radioButtonNews = (RadioButton) this.findViewById(R.id.rb_news);
        radioButtonQrcode = (RadioButton) this.findViewById(R.id.rb_more);
        radioButtonMessage = (RadioButton) this.findViewById(R.id.rb_member);
        imageViewNewMessage = (ImageView) this.findViewById(R.id.imageview_newmessage);
    }

    @Override
    public void initdata() {
        tabhost  = this.getTabHost();
        clientController = ClientController.getController(this);
        exKey = getIntent().getStringExtra("exKey");
        strExAddress = getIntent().getStringExtra("exAddress");
        strExDate = getIntent().getStringExtra("exTime");
        strTheme  = getIntent().getStringExtra("exTheme");
        strSponser  = getIntent().getStringExtra("exSponser");
        token = getIntent().getStringExtra("token");
        singupStatus = getIntent().getCharExtra("singupStatus",' ');
        count = getIntent().getIntExtra("count",0);
        TabHost.TabSpec newSpec = tabhost.newTabSpec(NEWS_TAB).setIndicator(NEWS_TAB)
                .setContent(new Intent(this, NewsPageActivity.class)
                        .putExtra("exKey", exKey)
                        .putExtra("singupStatus", singupStatus));
        TabHost.TabSpec showSpec = tabhost.newTabSpec(SUMMARY_TAB).setIndicator(SUMMARY_TAB)
                .setContent(new Intent (this, ShowHtmlActivity.class)
                        .putExtra("url","http://180.168.35.37:8080/e369_asset/"+ exKey + "/brief.html")
                        .putExtra("title", "会展介绍")
                        .putExtra("exKey", exKey)
                        .putExtra("singupStatus", singupStatus));
        TabHost.TabSpec homeSpec = tabhost.newTabSpec(SCHEDULE_TAB)
                .setIndicator(SCHEDULE_TAB)
                .setContent(new Intent(this, ShowHtmlActivity.class)
                        .putExtra("url","http://180.168.35.37:8080/e369_asset/"+ exKey + "/schedule.html")
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
                        id = R.id.rb_news;
                        break;
                    case R.id.rb_show:
                        tabhost.setCurrentTabByTag(SUMMARY_TAB);
                        id = R.id.rb_show;
                        break;
                    case R.id.rb_store:
                        tabhost.setCurrentTabByTag(SCHEDULE_TAB);
                        id = R.id.rb_store;
                        break;
                    case R.id.rb_member:
                        if( 'N' == singupStatus){
                            //radioButtonMessage.setChecked(false);
                           // radiogroup.check(id);
                        }
                        tabhost.setCurrentTabByTag(MESSAGE_TAB);
                        break;
                    case R.id.rb_more:
                        if ('N' == singupStatus) {
                            //radioButtonQrcode.setChecked(false);
                            //radiogroup.check(id);
                            //tabhost.setCurrentTabByTag(tabhost.getCurrentTabTag());
                        }
                        tabhost.setCurrentTabByTag(TWODCODE_TAB);
                        break;
                    default:
                        break;
                }
            }
        });

        if(count > 0){
            imageViewNewMessage.setVisibility(View.VISIBLE);
        }

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

    private void showRemindSignDialog(){

            Dialog dialog = new AlertDialog.Builder(ExhibitionActivity.this)
                    .setMessage("您还未报名，请先报名").create();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Intent intent = new Intent(ExhibitionActivity.this, SignupActivity.class);
                    intent.putExtra("exKey", exKey);
                    startActivity(intent);
                }
            });
            dialog.show();
    }
}
