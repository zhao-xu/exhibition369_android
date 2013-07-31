package com.threeH.MyExhibition.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.*;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.ExhibitionListAdapter;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.entities.ExhibitionList;
import com.threeH.MyExhibition.widget.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-7-29
 * Time: 下午3:49
 * To change this template use File | Settings | File Templates.
 */
public class RecommondActivity extends BaseActivity implements ActivityInterface,
        AdapterView.OnItemClickListener,XListView.IXListViewListener {
    private XListView mLvi;
    private ExhibitionListAdapter mAdapter;
    private ExhibitionList mJsonData;
    private LoadAsyncTask mLoadAsyncTask;
    private long mOrderNo = -1;
    private static final int SIZE = 5;
    private List<Exhibition> mData = new ArrayList<Exhibition>();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(1 == msg.what){
                mAdapter = new ExhibitionListAdapter(context,mData,token);
                mLvi.setAdapter(mAdapter);
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.signup_exhibition_page);
        findView();
        initdata();
        addAction();
    }


    @Override
    protected void onDestroy() {
        mLoadAsyncTask = null;
        super.onDestroy();
    }

    @Override
    public void findView() {
        mLvi = (XListView) this.findViewById(R.id.signup_exhi_listview);
    }
    @Override
    public void initdata() {
        mLoadAsyncTask = new LoadAsyncTask();
        mLoadAsyncTask.execute();
    }

    @Override
    public void addAction() {
        mLvi.setOnItemClickListener(this);
        mLvi.setPullLoadEnable(true);
        mLvi.setXListViewListener(this);
    }

    /**
     * 读取最后一条展会在后台的排序顺序
     * @param jsonData
     */
    private void setOrderNo(ExhibitionList jsonData) {
        int last = jsonData.getList().size() - 1;
        if(last >= 0){
            mOrderNo = jsonData.getList().get(last).getOrderNo();
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,ExhibitionActivity.class);
        intent.putExtra("token",token);
        intent.putExtra("exhibition",mData.get(position-1));
        startActivity(intent);
    }

    private void onLoad() {
        mLvi.stopRefresh();
        mLvi.stopLoadMore();
        mLvi.setRefreshTime("");
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mData.clear();
                mOrderNo = -1;
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
                makeListAdapterData();
                mAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);
    }
    public  void makeListAdapterData(){
        String str = null;
        try {
            str = mController.getService().reommondExhibitionList(token, SIZE, mOrderNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mJsonData = new Gson().fromJson(str,ExhibitionList.class);
        setOrderNo(mJsonData);
        if(null != mJsonData){
            for(Exhibition exhibition :mJsonData.getList()){
                mData.add(exhibition);
            }
        }
    }

    /**
     * 发送handler消息
     * @param what 消息标志
     */
    private void sendHandlerMessage(int what){
        Message message = handler.obtainMessage();
        message.what = what;
        handler.sendMessage(message);
    }

    class LoadAsyncTask extends AsyncTask<Void,Integer,Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        makeListAdapterData();
                        sendHandlerMessage(1);
                    } catch (Exception e) {
                    }
                }
            }).start();
            return null;
        }
    }
}
