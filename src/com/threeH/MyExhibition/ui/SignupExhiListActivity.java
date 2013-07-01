package com.threeH.MyExhibition.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.HomePageEnrollListAdapter;
import com.threeH.MyExhibition.adapters.SignExhiListAdapter;
import com.threeH.MyExhibition.cache.XmlDB;
import com.threeH.MyExhibition.common.StringPools;
import com.threeH.MyExhibition.entities.EnrollExhibition;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.entities.UnEnrollExhibition;
import com.threeH.MyExhibition.widget.XListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
    private List<HashMap<String,String>> mdataes = new ArrayList<HashMap<String,String>>();
    private List<HashMap<String,String>> searchDataes = new ArrayList<HashMap<String,String>>();
    private SignExhiListAdapter mSignExhiListAdapter;
    private EnrollExhibition.EnrollStatus[] enrollStatuses;
    private LayoutInflater mInflater;
    private View viewFooter;
    private LinearLayout linlLoad;
    private ImageView imageviewCancel,imageviewPrompt;
    private EditText editText;
    private LoadDataTask loadDataTask;
    private String name;
    private Button buttonSearch;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(1 == msg.what){
                if(mdataes.size() == 0){
                    imageviewPrompt.setVisibility(View.VISIBLE);
                }else{
                    imageviewPrompt.setVisibility(View.GONE);
                    mSignExhiListAdapter = new SignExhiListAdapter(context,mdataes);
                    mListView.setAdapter(mSignExhiListAdapter);
                }
            }
        }
    };
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.signup_exhibition_page);
        findView();
        initdata();
        addAction();
    }
    @Override
    public void initdata() {
        loadDataTask = new LoadDataTask();
        loadDataTask.execute();
    }

    @Override
    public void findView() {
        mListView = (XListView)findViewById(R.id.signup_exhi_listview);
        mInflater = LayoutInflater.from(context);
        viewFooter = mInflater.inflate(R.layout.list_footer_new,null);
        linlLoad = (LinearLayout) viewFooter.findViewById(R.id.list_footer_new);
        editText = (EditText) this.findViewById(R.id.titlebar_et);
        imageviewCancel = (ImageView) this.findViewById(R.id.titlebar_imageview_cancel);
        imageviewPrompt = (ImageView) this.findViewById(R.id.prompt_imageview);
        buttonSearch = (Button) this.findViewById(R.id.search_btn);
    }
    @Override
    public void addAction() {
        mListView.setAdapter(mSignExhiListAdapter);
        mListView.setDividerHeight(0);
        mListView.setOnItemClickListener(this);
        imageviewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                imageviewCancel.setVisibility(View.GONE);
            }
        });
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageviewCancel.setVisibility(View.VISIBLE);
            }
        });
        /*editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean  handled = false;
                if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == 0){
                    searchExhibition();
                    handled = true;
                }
                return handled;
            }
        });*/
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
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromInputMethod(editText.getWindowToken(), 0);

                searchDataes.clear();
                name = editText.getText().toString().trim();
                if(name != null && "".equals(name)){
                    for(HashMap<String,String> hashMap :mdataes){
                    searchDataes.add(hashMap);
            }
        }else{
            for(HashMap<String,String> hashMap :mdataes){
                 if(hashMap.get("name").contains(name)){
                    searchDataes.add(hashMap);
                 }
            }
        }
        SignExhiListAdapter adapter = new SignExhiListAdapter(context,searchDataes);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,ExhibitionActivity.class);
        intent.putExtra("exKey",mdataes.get(position-1).get("exhibitionExkey"));
        intent.putExtra("exAddress",mdataes.get(position-1).get("address"));
        intent.putExtra("exTime",mdataes.get(position-1).get("date"));
        intent.putExtra("exTheme",mdataes.get(position-1).get("name"));
        intent.putExtra("exSponser",mdataes.get(position-1).get("organizer"));
        intent.putExtra("token",token);
        intent.putExtra("singupStatus",mdataes.get(position-1).get("status").charAt(0));
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        imageviewCancel.setVisibility(View.GONE);
        super.onResume();
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime("...");
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
}
