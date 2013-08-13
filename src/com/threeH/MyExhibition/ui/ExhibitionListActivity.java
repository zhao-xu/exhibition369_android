package com.threeH.MyExhibition.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.ExhibitionListAdapter;
import com.threeH.MyExhibition.common.StringPools;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.entities.ExhibitionList;
import com.threeH.MyExhibition.tools.Tool;
import com.threeH.MyExhibition.widget.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-6-19
 * Time: 下午2:29
 * To change this template use File | Settings | File Templates.
 */
public class ExhibitionListActivity extends BaseActivity implements ActivityInterface,
        AdapterView.OnItemClickListener,XListView.IXListViewListener {
    private XListView mLvi;
    private ExhibitionListAdapter mAdapter;
    private ExhibitionList mJsonData;
    private EditText mEdttxtTheme;
    private String mStrTheme = "";
    public static String mStrScanExKey = "";
    private LoadAsyncTask mLoadAsyncTask;
    private Button mBtnSearch;
    private long mLngCreatedAt = -1;
    private static final int SIZE = 5;
    private ImageView mImgviewCancel;
    private PullupLoadAsyncTask mPullupLoadAsyncTask;
    private List<Exhibition> mData = new ArrayList<Exhibition>();
    private List<Exhibition> mItemClickDataes = new ArrayList<Exhibition>();
    private ExhibitionList mDataByQrcode;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(1 == msg.what){
                setItemClickdataes(mData);
                mAdapter = new ExhibitionListAdapter(context, mData,token);
                mLvi.setAdapter(mAdapter);
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.unsingup_exhibitionlist);
        findView();
        addAction();
    }
    @Override
    protected void onResume() {
        mImgviewCancel.setVisibility(View.GONE);
        if(!mStrScanExKey.equals("")){
            try {
                String str = mController.getService().UnErollExListByExKey(token, 1, -1, mStrScanExKey);
                mDataByQrcode = new Gson().fromJson(str,ExhibitionList.class);
                ArrayList<Exhibition> list = mDataByQrcode.getList();
                if(list != null){
                    mData.clear();
                    makeAllExhibitionListAdapterData(mDataByQrcode);
                    mEdttxtTheme.setText(list.get(0).getName());
                    sendHandlerMessage(1);
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
    protected void onDestroy() {
        mLoadAsyncTask = null;
        super.onDestroy();
    }

    @Override
    public void findView() {
        mLvi = (XListView) this.findViewById(R.id.unsingup_exhibition_listview);
        mEdttxtTheme = (EditText) this.findViewById(R.id.titlebar_et);
        mImgviewCancel = (ImageView) this.findViewById(R.id.titlebar_imageview_cancel);
        mBtnSearch = (Button) this.findViewById(R.id.search_btn);
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

        mImgviewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEdttxtTheme.setText("");
                mImgviewCancel.setVisibility(View.GONE);
            }
        });

        mEdttxtTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImgviewCancel.setVisibility(View.VISIBLE);
            }
        });

        mEdttxtTheme.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == 0) {
                    searchExhibition();
                    handled = true;
                }
                return handled;
            }
        });
        mEdttxtTheme.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mImgviewCancel.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchExhibition();
            }
        });
    }

    private void searchExhibition() {
        try{
            mStrTheme = mEdttxtTheme.getText().toString().trim();
            String str;
            if(null != mStrTheme && !("".equals(mStrTheme))){
                str = mController.getService().UnErollExList(token,SIZE,-1, mStrTheme);
                ExhibitionList allExhibitionData = new Gson().fromJson(str,ExhibitionList.class);
                setCreateAt(allExhibitionData);
                mData = Tool.makeAllExhibitionListAdapterData(allExhibitionData);
                mAdapter = new ExhibitionListAdapter(context, mData,token);
                mLvi.setAdapter(mAdapter);
                setItemClickdataes(mData);
            }else {
                mData.clear();
                mLvi.setAdapter(null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setCreateAt(ExhibitionList allExhibitionData) {
        int last = allExhibitionData.getList().size() - 1;
        if(last >= 0){
            mLngCreatedAt = allExhibitionData.getList().get(last).getCreatedAt();
        }
    }

    private void setItemClickdataes(List<Exhibition> dataes){
        mItemClickDataes.clear();
        for (Exhibition exhibition : dataes) {
            mItemClickDataes.add(exhibition);
        }
        mData.clear();
        for (Exhibition exhibition : mItemClickDataes) {
            mData.add(exhibition);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,ExhibitionActivity.class);
        intent.putExtra("exhibition",mItemClickDataes.get(position -1));
        intent.putExtra("token",token);
        startActivity(intent);
    }

    private void onLoad() {
        mLvi.stopRefresh();
        mLvi.stopLoadMore();
        mLvi.setRefreshTime("");
    }

    @Override
    public void onRefresh() {
        mStrTheme = mEdttxtTheme.getText().toString().trim();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mData.clear();
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
                mAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);
    }
    public  void makeAllExhibitionListAdapterData(ExhibitionList allExhibitionData){
        if(null != allExhibitionData){
            for(Exhibition exhibition : allExhibitionData.getList()){
                mData.add(exhibition);
            }
        }
    }


    private void loadNextPageData(){
        try {
            String str = mController.getService().UnErollExList(token,SIZE, mLngCreatedAt, mStrTheme);
            mJsonData = new Gson().fromJson(str,ExhibitionList.class);
            setCreateAt(mJsonData);
            makeAllExhibitionListAdapterData(mJsonData);
            setItemClickdataes(mData);
        } catch (Exception e) {
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

    class LoadAsyncTask extends AsyncTask<Void,Integer,Integer>{

        @Override
        protected Integer doInBackground(Void... voids) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if(null != mStrTheme && !("".equals(mStrTheme))){
                            String str = mController.getService().UnErollExList(token,SIZE,-1, mStrTheme);
                            mJsonData = new Gson().fromJson(str,ExhibitionList.class);
                            setCreateAt(mJsonData);
                            makeAllExhibitionListAdapterData(mJsonData);
                            sendHandlerMessage(1);
                        }
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
                        String str = mController.getService().UnErollExList(token,SIZE, mLngCreatedAt, mStrTheme);
                        mJsonData = new Gson().fromJson(str,ExhibitionList.class);
                        setCreateAt(mJsonData);
                        makeAllExhibitionListAdapterData(mJsonData);
                    } catch (Exception e) {
                    }
                }
            }).start();
            return null;
        }
    }
}
