package com.threeH.MyExhibition.ui;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.MessageListAdapter;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.entities.MessageList;
import com.threeH.MyExhibition.listener.TelephoneClickListener;
import com.threeH.MyExhibition.tools.MSYH;
import com.threeH.MyExhibition.widget.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 13-6-19
 * Time: 上午11:38
 * To change this template use File | Settings | File Templates.
 */
public class MessageActivity extends BaseActivity implements
        ActivityInterface,AdapterView.OnItemClickListener,XListView.IXListViewListener{
    private List<MessageList.Message> mDataes = new ArrayList<MessageList.Message>();
    private XListView mXLvi;
    private MessageListAdapter mListAdapter;
    private LoadTask mLoadTask;
    private Exhibition mExhibition;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            if(1 == msg.what){
                if(mDataes.size() == 0){
                    mXLvi.setVisibility(View.GONE);
                }else{
                    mListAdapter = new MessageListAdapter(context, mDataes);
                    mXLvi.setAdapter(mListAdapter);
                }
            }
        }
    };
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.message_page);
        mExhibition = (Exhibition) getIntent().getExtras().get("exhibition");
        findView();
        initdata();
        addAction();
    }

    @Override
    public void findView() {
        mXLvi = (XListView)findViewById(R.id.message_list_view);
    }
    @Override
    public void initdata() {
        mLoadTask = new LoadTask();
        mLoadTask.execute();
    }

    @Override
    public void addAction() {
        mXLvi.setDividerHeight(0);
        mXLvi.setOnItemClickListener(this);
        mXLvi.setPullLoadEnable(true);
        mXLvi.setXListViewListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MessageListAdapter.selectedID = position-1;
        mListAdapter.notifyDataSetChanged();
    }

    private void onLoad() {
        mXLvi.stopRefresh();
        mXLvi.stopLoadMore();
        mXLvi.setRefreshTime("...");
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

    class LoadTask extends AsyncTask<Void,Integer,Integer>{
        @Override
        protected Integer doInBackground(Void... params) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String mJsonData = mController.getService().ExMessage(mExhibition.getExKey(),token);
                        MessageList mMessage = mGson.fromJson(mJsonData,MessageList.class);
                        for (MessageList.Message message : mMessage.getList()){
                            mDataes.add(message);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    android.os.Message message = handler.obtainMessage();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }).start();
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        mLoadTask = null;
        super.onDestroy();
    }
}