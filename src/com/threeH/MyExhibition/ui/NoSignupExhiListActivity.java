package com.threeH.MyExhibition.ui;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.LabeledIntent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.widget.*;
import cn.mobiledaily.module.android.module.mobilepush.service.helper.AndroidMessageClient;
import cn.mobiledaily.module.android.module.mobilepush.service.helper.OnMessageListener;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.HomePageEnrollListAdapter;
import com.threeH.MyExhibition.cache.XmlDB;
import com.threeH.MyExhibition.common.StringPools;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.entities.UnEnrollExhibition;
import com.threeH.MyExhibition.service.ClientController;
import com.threeH.MyExhibition.tools.MobileConfig;
import com.threeH.MyExhibition.tools.Tool;
import com.threeH.MyExhibition.widget.PullToRefreshView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-6-19
 * Time: 下午2:29
 * To change this template use File | Settings | File Templates.
 */
public class NoSignupExhiListActivity extends BaseActivity implements ActivityInterface,AdapterView.OnItemClickListener {
    public static ListView listView;
    private HomePageEnrollListAdapter adapter;
    private UnEnrollExhibition allExhibitionData;
    private Button buttonSearch;
    private EditText editText;
    private String name;
    private MyAsyncTask myAsyncTask;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(1 == msg.what){
                listView.setAdapter(adapter);
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.unsingup_exhibitionlist);
        findView();
        initdata();
        addAction();
    }

    @Override
    public void findView() {
        listView = (ListView) this.findViewById(R.id.unsingup_exhibition_listview);
        buttonSearch = (Button) this.findViewById(R.id.search_btn);
        editText = (EditText) this.findViewById(R.id.titlebar_et);
    }

    @Override
    public void initdata() {
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }

    private void saveExhibitionData(String str) {
        SharedPreferences sharedPreferences =   getSharedPreferences(StringPools.ALL_EXHIBITION_DATA, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("exhibitionData",str);
        editor.commit();
    }

    @Override
    public void addAction() {
        listView.setDividerHeight(0);
        listView.setOnItemClickListener(this);
        try{
            AndroidMessageClient client = new AndroidMessageClient();
            client.init(token,new MyMessageListener());
        }catch (Exception e){
            e.printStackTrace();
        }
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    name = editText.getText().toString().trim();
                    String str = mController.getService().UnErollExList(token,-1,-1,name);
                    UnEnrollExhibition allExhibitionData = new Gson().fromJson(str,UnEnrollExhibition.class);
                    List<HashMap<String,String>> data = makeAllExhibitionListAdapterData(allExhibitionData);
                    HomePageEnrollListAdapter adapter = new HomePageEnrollListAdapter(NoSignupExhiListActivity.this,data);
                    listView.setAdapter(adapter);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,ExhibitionActivity.class);
        intent.putExtra("exKey",allExhibitionData.getList().get(position).getExKey());
        intent.putExtra("exAddress",allExhibitionData.getList().get(position).getAddress());
        intent.putExtra("exTime",allExhibitionData.getList().get(position).getDate());
        intent.putExtra("exTheme",allExhibitionData.getList().get(position).getName());
        intent.putExtra("exSponser",allExhibitionData.getList().get(position).getOrganizer());
        intent.putExtra("token",token);
        startActivity(intent);
    }

    class MyMessageListener implements OnMessageListener {

        @Override
        public void onMessageReceived(String message) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification  = new Notification(R.drawable.appicon,message,System.currentTimeMillis());
            Intent intent = new Intent(context,MessageActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
            notification.setLatestEventInfo(context,"展会消息通知",message,pendingIntent);
            notification.defaults = Notification.DEFAULT_SOUND;
            notificationManager.notify(202,notification);
        }
    }

    class MyAsyncTask extends AsyncTask<Void,Integer,Integer>{

        @Override
        protected Integer doInBackground(Void... voids) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String str = mController.getService().UnErollExList(token,-1,-1,"");
                        if(null != str && !"".equals(str)){
                            XmlDB.getInstance(context).saveKey(StringPools.ALL_EXHIBITION_DATA,str);
                        }else{
                            str = XmlDB.getInstance(context).getKeyStringValue(StringPools.ALL_EXHIBITION_DATA,"");
                        }
                        allExhibitionData = new Gson().fromJson(str,UnEnrollExhibition.class);
                        List<HashMap<String,String>> data = makeAllExhibitionListAdapterData(allExhibitionData);
                        adapter = new HomePageEnrollListAdapter(context,data);
                        Message message = handler.obtainMessage();
                        message.what = 1;
                        handler.sendMessage(message);
                        /*MobileConfig mobileConfig = MobileConfig.getMobileConfig(context);
                        XmlDB.getInstance(NoSignupExhiListActivity.this).saveKey(StringPools.OVERALL_CONFIG,
                                mController.getService().OverAllData(StringPools.PHONE_TYPE,
                                        "369exhibition", "1.0", mobileConfig.getLocalMacAddress()));*/
                    } catch (Exception e) {
                        Log.e("data", e.getMessage());
                    }
                }
            }).start();
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        myAsyncTask = null;
        super.onDestroy();
    }

    public  List<HashMap<String,String>> makeAllExhibitionListAdapterData(UnEnrollExhibition allExhibitionData){

        List<HashMap<String,String>> data = new ArrayList<HashMap<String, String>>();
        if(null != allExhibitionData){
            for(Exhibition exhibition : allExhibitionData.getList()){
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("exhibitionName",exhibition.getName());
                map.put("exhibitionDate",exhibition.getDate());
                map.put("exhibitionAddress",exhibition.getAddress());
                map.put("exhibitionSponser",exhibition.getOrganizer());
                map.put("exhibitionApplied",exhibition.getApplied());
                map.put("exhibitionExkey",exhibition.getExKey());
                map.put("status",exhibition.getStatus());
                data.add(map);
            }
        }
        return data;
    }
}
