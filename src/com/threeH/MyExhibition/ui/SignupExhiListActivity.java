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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import cn.mobiledaily.module.android.module.mobilepush.service.helper.AndroidMessageClient;
import cn.mobiledaily.module.android.module.mobilepush.service.helper.OnMessageListener;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.SignExhiListAdapter;
import com.threeH.MyExhibition.cache.XmlDB;
import com.threeH.MyExhibition.common.StringPools;
import com.threeH.MyExhibition.entities.EnrollExhibition;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.entities.UnEnrollExhibition;
import com.threeH.MyExhibition.tools.SharedPreferencesUtil;
import com.threeH.MyExhibition.widget.XListView;

import java.util.ArrayList;
import java.util.HashMap;
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
    private List<HashMap<String,String>> mdataes = new LinkedList<HashMap<String, String>>();
    private List<HashMap<String,String>> searchDataes = new ArrayList<HashMap<String,String>>();
    private List<HashMap<String,String>> mItemClickDataes = new ArrayList<HashMap<String,String>>();
    private SignExhiListAdapter mSignExhiListAdapter;
    private EnrollExhibition.EnrollStatus[] enrollStatuses;
    private ImageView imageviewCancel,imageviewPrompt;
    private EditText editText;
    private LoadDataTask loadDataTask;
    private String name = "";
    private Button buttonSearch;
    public static String mStrScanExKey = "";
    private String mStrExKey;
    private UnEnrollExhibition mExhibitionDataByQrcode;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                if(mdataes.size() == 0){
                    imageviewPrompt.setVisibility(View.VISIBLE);
                }else{
                    //imageviewPrompt.setVisibility(View.GONE);
                    name = editText.getText().toString();
                    if(!"".equals(name)){
                        searchExhibition();
                    }else{
                        setAdapter();
                    }
                }
                    break;
                case 2:
                    setAdapter();
                    break;
            }
        }
    };

    /**
     * 设置我的展会列表的数据
     */
    private void setAdapter(){
        mSignExhiListAdapter = new SignExhiListAdapter(SignupExhiListActivity.this,mdataes,token);
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
    protected void onResume(){
        imageviewCancel.setVisibility(View.GONE);
        if(!mStrScanExKey.equals("")){
            try {
                String str = mController.getService().UnErollExListByExKey(token, 1, -1, mStrScanExKey);
                mExhibitionDataByQrcode = new Gson().fromJson(str,UnEnrollExhibition.class);
                ArrayList<Exhibition> list = mExhibitionDataByQrcode.getList();
                if(list != null){
                    if(isExist(list.get(0).getExKey())){
                        editText.setText(name);
                    }else{
                        HashMap<String,String> map = new HashMap<String, String>();
                        for(Exhibition exhibition : list){
                            setMapData(map, exhibition);
                            SharedPreferencesUtil.saveObject(exhibition,context,StringPools.SCAN_EXHIBITION_DATA);
                        }
                        ((LinkedList)mdataes).addFirst(map);
                        setItemClickdataes(mdataes);
                        editText.setText(list.get(0).getName());
                        Message message = handler.obtainMessage();
                        message.what = 2;
                        handler.sendMessage(message);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        mStrScanExKey = "";
        super.onPause();
    }

    @Override
    public void initdata() {
        loadDataTask = new LoadDataTask();
        loadDataTask.execute();
    }

    @Override
    public void findView() {
        mListView = (XListView)findViewById(R.id.signup_exhi_listview);
        editText = (EditText) this.findViewById(R.id.titlebar_et);
        imageviewCancel = (ImageView) this.findViewById(R.id.titlebar_imageview_cancel);
        //imageviewPrompt = (ImageView) this.findViewById(R.id.prompt_imageview);
        buttonSearch = (Button) this.findViewById(R.id.search_btn);
    }
    @Override
    public void addAction() {
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
                imageviewCancel.setVisibility(View.GONE);
            }
        });
        mListView.setOnItemClickListener(this);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageviewCancel.setVisibility(View.VISIBLE);
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                imageviewCancel.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        searchDataes.clear();
        name = editText.getText().toString().trim();
        if (name != null && "".equals(name)) {
            for (HashMap<String, String> hashMap : mdataes) {
                searchDataes.add(hashMap);
            }
        } else {
            for (HashMap<String, String> hashMap : mdataes) {
                if (hashMap.get("name").contains(name)) {
                    searchDataes.add(hashMap);
                }
            }
        }
        SignExhiListAdapter adapter = new SignExhiListAdapter(SignupExhiListActivity.this, searchDataes);
        mListView.setAdapter(adapter);
        setItemClickdataes(searchDataes);
    }

    /**
     * 将展会对象的属性存入map中
     * @param map
     * @param exhibition
     */
    private void setMapData(HashMap<String, String> map, Exhibition exhibition) {
        map.put("exhibitionExkey",exhibition.getExKey());
        map.put("name",exhibition.getName());
        map.put("date", exhibition.getDate());
        map.put("address",exhibition.getAddress());
        map.put("organizer",exhibition.getOrganizer());
        map.put("status", exhibition.getStatus() + " ");
        map.put("count", String.valueOf(exhibition.getCount()));
    }

    private void setItemClickdataes(List<HashMap<String,String>> dataes){
        mItemClickDataes.clear();
        for (HashMap<String, String> hashMap : dataes) {
            mItemClickDataes.add(hashMap);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,ExhibitionActivity.class);
        intent.putExtra("exKey",mItemClickDataes.get(position-1).get("exhibitionExkey"));
        intent.putExtra("exAddress",mItemClickDataes.get(position-1).get("address"));
        intent.putExtra("exTime",mItemClickDataes.get(position-1).get("date"));
        intent.putExtra("exTheme",mItemClickDataes.get(position-1).get("name"));
        intent.putExtra("exSponser",mItemClickDataes.get(position-1).get("organizer"));
        intent.putExtra("token",token);
        intent.putExtra("singupStatus",mItemClickDataes.get(position-1).get("status").charAt(0));
        intent.putExtra("count",Integer.valueOf(mItemClickDataes.get(position - 1).get("count")));
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
                mdataes.clear();
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
                /*loadNextPageData();
                mSignExhiListAdapter.notifyDataSetChanged();*/
                onLoad();
            }
        }, 2000);
    }

    private boolean isExist(String exkey){
        for (HashMap<String, String> hashMap : mdataes) {
            if (hashMap.get("exhibitionExkey").contains(exkey)) {
                name = hashMap.get("name");
                return true;
            }
        }
        return false;
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
                                HashMap<String,String> map =new HashMap<String,String>();
                                setMapData(map,((Exhibition)object));
                                ((LinkedList)mdataes).addFirst(map);
                            }
                        }
                        enrollStatuses =  mGson.fromJson(jsonData, EnrollExhibition.EnrollStatus[].class);
                        for(EnrollExhibition.EnrollStatus mEnrollStatus : enrollStatuses){
                            HashMap<String,String> map =new HashMap<String,String>();
                            map.put("exhibitionExkey",mEnrollStatus.getExKey());
                            map.put("name",mEnrollStatus.getName());
                            map.put("date",mEnrollStatus.getDate());
                            map.put("address",mEnrollStatus.getAddress());
                            map.put("organizer",mEnrollStatus.getOrganizer());
                            map.put("status",mEnrollStatus.getStatus());
                            map.put("count",String.valueOf(mEnrollStatus.getCount()));
                            mdataes.add(map);
                        }
                        setItemClickdataes(mdataes);
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
            Intent intent = new Intent(context,HomeOfTabActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
            notification.setLatestEventInfo(context,"展会消息通知",message,pendingIntent);
            notification.defaults = Notification.DEFAULT_SOUND;
            notificationManager.notify(202,notification);
        }
    }
}
