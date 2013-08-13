package com.threeH.MyExhibition.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.*;
import cn.mobiledaily.module.android.module.mobilepush.service.helper.AndroidMessageClient;
import cn.mobiledaily.module.android.module.mobilepush.service.helper.OnMessageListener;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.SignExhiListAdapter;
import com.threeH.MyExhibition.cache.XmlDB;
import com.threeH.MyExhibition.common.StringPools;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.tools.SharedPreferencesUtil;
import com.threeH.MyExhibition.widget.XListView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-6-19
 * Time: 下午2:41
 * To change this template use File | Settings | File Templates.
 */
public class SignupExhiListActivity extends BaseActivity implements ActivityInterface,
            AdapterView.OnItemClickListener,XListView.IXListViewListener {
    private XListView mListView;
    private List<Exhibition> mDataes = new LinkedList<Exhibition>();
    private SignExhiListAdapter mSignExhiListAdapter;
    private Exhibition[] mMyExhibitions;
    private LoadDataTask loadDataTask;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    setAdapter();
                    break;
            }
        }
    };

    /**
     * 设置我的展会列表的数据
     */
    private void setAdapter(){
        mSignExhiListAdapter = new SignExhiListAdapter(SignupExhiListActivity.this, mDataes,token);
        mListView.setAdapter(mSignExhiListAdapter);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.signup_exhibition_page);
        findView();
        initdata();
        addAction();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void initdata() {
        loadDataTask = new LoadDataTask();
        loadDataTask.execute();
    }

    @Override
    public void findView(){
        mListView = (XListView)findViewById(R.id.signup_exhi_listview);
    }
    @Override
    public void addAction() {
        try{
            AndroidMessageClient client = new AndroidMessageClient();
            client.init(token,new MyMessageListener());
        }catch (Exception e){
            e.printStackTrace();
        }
        mListView.setOnItemClickListener(this);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,ExhibitionActivity.class);
        intent.putExtra("exhibition", mDataes.get(position - 1));
        intent.putExtra("token",token);
        startActivity(intent);
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime("");
    }
    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDataes.clear();
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
                onLoad();
            }
        }, 2000);
    }

    /**
     * 用于加载已报名列表的异步任务栈。
     * 有网络时从网络上拉数据并做存储，无网络时从本地取数据。
     * 获得数据后发送消息到消息队列。
     */
    class LoadDataTask extends AsyncTask<Void,Integer,Integer>{

        @Override
        protected Integer doInBackground(Void... params) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String jsonData = mController.getService().ErollExList(token);
                        if (null != jsonData && !"".equals(jsonData)) {
                            XmlDB.getInstance(context).saveKey(StringPools.SIGNUP_EXHIBITION_DATA, jsonData);
                        } else {
                            jsonData = XmlDB.getInstance(context).getKeyStringValue(StringPools.SIGNUP_EXHIBITION_DATA, "");
                        }
                        List<Object> list  =
                                SharedPreferencesUtil.getObject(context, StringPools.SCAN_EXHIBITION_DATA);
                        if(list != null){
                            for(Object object : list){
                                ((LinkedList) mDataes).addFirst(object);
                            }
                        }
                        mMyExhibitions =  mGson.fromJson(jsonData, Exhibition[].class);
                        for(Exhibition exhibition : mMyExhibitions){
                            mDataes.add(exhibition);
                        }
                    } catch (Exception e) {
                    }
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }).start();
            return null;
        }
    }
    class MyMessageListener implements OnMessageListener {

        @Override
        public void onMessageReceived(String message) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification  = new Notification(R.drawable.appicon,message,System.currentTimeMillis());
            Intent intent = new Intent(context,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
            notification.setLatestEventInfo(context,"展会消息通知",message,pendingIntent);
            notification.defaults = Notification.DEFAULT_SOUND;
            notificationManager.notify(202,notification);
        }
    }
}
