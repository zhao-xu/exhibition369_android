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
import android.view.*;
import android.view.inputmethod.EditorInfo;
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
import com.threeH.MyExhibition.widget.XListView;

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
public class NoSignupExhiListActivity extends BaseActivity implements ActivityInterface,
        AdapterView.OnItemClickListener,XListView.IXListViewListener {
    private XListView listView;
    private HomePageEnrollListAdapter adapter;
    private UnEnrollExhibition allExhibitionData;
    private EditText editText;
    private String name;
    private MyAsyncTask myAsyncTask;
    private LayoutInflater mInflater;
    private Button buttonSearch;
    private long createdAt = -1;
    private static final int SIZE = 5;
    private ImageView imageviewCancel;
    private PullupLoadAsyncTask mPullupLoadAsyncTask;
    List<HashMap<String,String>> data = new ArrayList<HashMap<String, String>>();

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
        mInflater = LayoutInflater.from(context);
        findView();
        initdata();
        addAction();
    }

    @Override
    public void findView() {
        listView = (XListView) this.findViewById(R.id.unsingup_exhibition_listview);
        editText = (EditText) this.findViewById(R.id.titlebar_et);
        imageviewCancel = (ImageView) this.findViewById(R.id.titlebar_imageview_cancel);
        buttonSearch = (Button) this.findViewById(R.id.search_btn);
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
        listView.setOnItemClickListener(this);
        listView.setPullLoadEnable(true);
        listView.setXListViewListener(this);
        try{
            AndroidMessageClient client = new AndroidMessageClient();
            client.init(token,new MyMessageListener());
        }catch (Exception e){
            e.printStackTrace();
        }

        imageviewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageviewCancel.setVisibility(View.VISIBLE);
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean  handled = false;
                if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == 0){
                    searchExhibition();
                    handled = true;
                }
                return handled;
            }
        });
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchExhibition();
            }
        });
    }

    private void searchExhibition() {
        try{
            name = editText.getText().toString().trim();
            String str;
            if(null != name && "".equals(name)){
                str = mController.getService().UnErollExList(token,SIZE,-1,name);
            }
            str = mController.getService().UnErollExList(token,-1,-1,name);
            UnEnrollExhibition allExhibitionData = new Gson().fromJson(str,UnEnrollExhibition.class);
            List<HashMap<String,String>> searchData = Tool.makeAllExhibitionListAdapterData(allExhibitionData);
            HomePageEnrollListAdapter adapter = new HomePageEnrollListAdapter(NoSignupExhiListActivity.this,searchData,token);
            listView.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,ExhibitionActivity.class);
        intent.putExtra("exKey",allExhibitionData.getList().get(position-1).getExKey());
        intent.putExtra("exAddress",allExhibitionData.getList().get(position-1).getAddress());
        intent.putExtra("exTime",allExhibitionData.getList().get(position-1).getDate());
        intent.putExtra("exTheme",allExhibitionData.getList().get(position-1).getName());
        intent.putExtra("exSponser",allExhibitionData.getList().get(position-1).getOrganizer());
        intent.putExtra("token",token);
        intent.putExtra("count",allExhibitionData.getList().get(position-1).getCount());
        intent.putExtra("singupStatus", (allExhibitionData.getList().get(position).getStatus() + " ").charAt(0));
        startActivity(intent);
    }

    private void onLoad() {
        listView.stopRefresh();
        listView.stopLoadMore();
        listView.setRefreshTime("...");
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                data.clear();
                initdata();

                onLoad();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadNextPageData();
                adapter.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);
    }
    public  void makeAllExhibitionListAdapterData(UnEnrollExhibition allExhibitionData){
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
                map.put("count",String.valueOf(exhibition.getCount()));
                data.add(map);
            }
        }
    }
    @Override
    protected void onDestroy() {
        myAsyncTask = null;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        imageviewCancel.setVisibility(View.GONE);
        super.onResume();
    }

    private void loadNextPageData(){
        try {
            String str = mController.getService().UnErollExList(token,5,createdAt,"");
            allExhibitionData = new Gson().fromJson(str,UnEnrollExhibition.class);
            int last = allExhibitionData.getList().size() - 1;
            if(last >= 0){
                createdAt = allExhibitionData.getList().get(last).getCreatedAt();
            }
            makeAllExhibitionListAdapterData(allExhibitionData);
        } catch (Exception e) {

        }
    }

    class MyMessageListener implements OnMessageListener {

        @Override
        public void onMessageReceived(String message) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification  = new Notification(R.drawable.appicon,message,System.currentTimeMillis());
            Intent intent = new Intent(context,HomeOfTabActivity.class);
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
                        String str = mController.getService().UnErollExList(token,SIZE,-1,"");
                        if(null != str && !"".equals(str)){
                            XmlDB.getInstance(context).saveKey(StringPools.ALL_EXHIBITION_DATA,str);
                        }else{
                            str = XmlDB.getInstance(context).getKeyStringValue(StringPools.ALL_EXHIBITION_DATA,"");
                        }
                        allExhibitionData = new Gson().fromJson(str,UnEnrollExhibition.class);
                        int last = allExhibitionData.getList().size() - 1;
                        if(last >= 0){
                            createdAt = allExhibitionData.getList().get(last).getCreatedAt();
                        }
                        makeAllExhibitionListAdapterData(allExhibitionData);
                        adapter = new HomePageEnrollListAdapter(context,data,token);
                        Message message = handler.obtainMessage();
                        message.what = 1;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        Log.e("data", e.getMessage());
                    }
                }
            }).start();
            return null;
        }
    }

    class PullupLoadAsyncTask extends AsyncTask<Void,Integer,Integer>{

        @Override
        protected Integer doInBackground(Void... voids) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        String str = mController.getService().UnErollExList(token,5,createdAt,"");
                        allExhibitionData = new Gson().fromJson(str,UnEnrollExhibition.class);
                        int last = allExhibitionData.getList().size() - 1;
                        if(last >= 0){
                            createdAt = allExhibitionData.getList().get(last).getCreatedAt();
                        }
                        makeAllExhibitionListAdapterData(allExhibitionData);
                        //adapter.notifyDataSetChanged();
                        //listView.setAdapter(adapter);
                        /*Message message = handler.obtainMessage();
                        message.what = 1;
                        handler.sendMessage(message);*/
                    } catch (Exception e) {

                    }
                }
            }).start();
            return null;
        }
    }





}
