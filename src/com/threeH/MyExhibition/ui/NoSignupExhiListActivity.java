package com.threeH.MyExhibition.ui;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import cn.mobiledaily.module.android.module.mobilepush.service.helper.AndroidMessageClient;
import cn.mobiledaily.module.android.module.mobilepush.service.helper.OnMessageListener;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.HomePageEnrollListAdapter;
import com.threeH.MyExhibition.common.StringPools;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.entities.UnEnrollExhibition;
import com.threeH.MyExhibition.tools.Tool;
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
    private String name = "";
    private MyAsyncTask myAsyncTask;
    private Button buttonSearch;
    private long createdAt = -1;
    private static final int SIZE = 5;
    private ImageView imageviewCancel;
    private PullupLoadAsyncTask mPullupLoadAsyncTask;
    List<HashMap<String,String>> data = new ArrayList<HashMap<String, String>>();
    private List<HashMap<String,String>> mItemClickDataes = new ArrayList<HashMap<String,String>>();
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
        //initdata();
        addAction();
    }
    @Override
    protected void onResume() {
        imageviewCancel.setVisibility(View.GONE);

        super.onResume();
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

        imageviewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                imageviewCancel.setVisibility(View.GONE);
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
        try{
            name = editText.getText().toString().trim();
            String str;
            str = mController.getService().UnErollExList(token,SIZE,-1,name);
            UnEnrollExhibition allExhibitionData = new Gson().fromJson(str,UnEnrollExhibition.class);
            setCreateAt(allExhibitionData);
            data = Tool.makeAllExhibitionListAdapterData(allExhibitionData);
            adapter = new HomePageEnrollListAdapter(context,data,token);
            listView.setAdapter(adapter);
            setItemClickdataes(data);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setCreateAt(UnEnrollExhibition allExhibitionData) {
        int last = allExhibitionData.getList().size() - 1;
        if(last >= 0){
            createdAt = allExhibitionData.getList().get(last).getCreatedAt();
        }
    }

    private void setItemClickdataes(List<HashMap<String,String>> dataes){
        mItemClickDataes.clear();
        for (HashMap<String, String> hashMap : dataes) {
            mItemClickDataes.add(hashMap);
        }
        data.clear();
        for (HashMap<String, String> hashMap : mItemClickDataes) {
            data.add(hashMap);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,ExhibitionActivity.class);
        intent.putExtra("exKey",mItemClickDataes.get(position-1).get("exhibitionExkey"));
        intent.putExtra("exAddress",mItemClickDataes.get(position - 1).get("exhibitionAddress"));
        intent.putExtra("exTime",mItemClickDataes.get(position - 1).get("exhibitionDate"));
        intent.putExtra("exTheme",mItemClickDataes.get(position - 1).get("exhibitionName"));
        intent.putExtra("exSponser",mItemClickDataes.get(position - 1).get("exhibitionSponser"));
        intent.putExtra("token",token);
        intent.putExtra("count",Integer.valueOf(mItemClickDataes.get(position - 1).get("count")));
        intent.putExtra("singupStatus", (mItemClickDataes.get(position - 1).get("status") + " ").charAt(0));
        startActivity(intent);
    }

    private void onLoad() {
        listView.stopRefresh();
        listView.stopLoadMore();
        listView.setRefreshTime("");
    }

    @Override
    public void onRefresh() {
        name = editText.getText().toString().trim();
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

    private void loadNextPageData(){
        try {
            String str = mController.getService().UnErollExList(token,SIZE,createdAt,name);
            allExhibitionData = new Gson().fromJson(str,UnEnrollExhibition.class);
            setCreateAt(allExhibitionData);
            makeAllExhibitionListAdapterData(allExhibitionData);
            setItemClickdataes(data);
        } catch (Exception e) {
        }
    }



    class MyAsyncTask extends AsyncTask<Void,Integer,Integer>{

        @Override
        protected Integer doInBackground(Void... voids) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String str = mController.getService().UnErollExList(token,SIZE,-1,name);
                        allExhibitionData = new Gson().fromJson(str,UnEnrollExhibition.class);
                        setCreateAt(allExhibitionData);
                        makeAllExhibitionListAdapterData(allExhibitionData);
                        setItemClickdataes(data);
                        adapter = new HomePageEnrollListAdapter(context,data,token);
                        Message message = handler.obtainMessage();
                        message.what = 1;
                        handler.sendMessage(message);
                    } catch (Exception e) {

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
                        String str = mController.getService().UnErollExList(token,SIZE,createdAt,name);
                        allExhibitionData = new Gson().fromJson(str,UnEnrollExhibition.class);
                        setCreateAt(allExhibitionData);
                        makeAllExhibitionListAdapterData(allExhibitionData);
                    } catch (Exception e) {

                    }
                }
            }).start();
            return null;
        }
    }

}
