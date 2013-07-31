package com.threeH.MyExhibition.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.*;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.NewslistAdapter;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.entities.NewList;
import com.threeH.MyExhibition.listener.TelephoneClickListener;
import com.threeH.MyExhibition.tools.MSYH;
import com.threeH.MyExhibition.tools.Tool;
import com.threeH.MyExhibition.widget.XListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 13-6-19
 * Time: 上午11:38
 * To change this template use File | Settings | File Templates.
 */
public class NewsPageActivity extends BaseActivity  implements
        ActivityInterface,AdapterView.OnItemClickListener,XListView.IXListViewListener{
    private XListView listView;
    private NewslistAdapter adapter;
    private NewList newsData;
    private ImageView imageviewTelephone;
    private TextView textViewTitle;
    private Typeface typeface;
    List<HashMap<String,String>> data = new ArrayList<HashMap<String, String>>();
    private ImageView imageviewPrompt;
    private LoadDataAsyncTask loadDataAsyncTask;
    private Exhibition mExhibition;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(1 == msg.what){
                if(data.size() == 0){
                    listView.setVisibility(View.GONE);
                    imageviewPrompt.setVisibility(View.VISIBLE);
                }else{
                    listView.setVisibility(View.VISIBLE);
                    imageviewPrompt.setVisibility(View.GONE);
                    adapter =  new NewslistAdapter(data,context);
                    listView.setAdapter(adapter);
                }
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.newslist);
        typeface = MSYH.getInstance(context.getApplicationContext()).getNormal();
        mExhibition = (Exhibition) getIntent().getExtras().get("exhibition");
        findView();
        initdata();
        addAction();
    }

    @Override
    public void findView() {
        listView = (XListView) this.findViewById(R.id.newslist_listview);
        imageviewTelephone = (ImageView) this.findViewById(R.id.exhibition_titlebar_btn_telephone);
        textViewTitle = (TextView) this.findViewById(R.id.exhibition_titlebar_txt_title);
        imageviewPrompt = (ImageView) this.findViewById(R.id.prompt_imageview);
    }

    @Override
    public void initdata() {
        loadDataAsyncTask = new LoadDataAsyncTask();
        loadDataAsyncTask.execute();
    }

    @Override
    public void addAction() {
        listView.setOnItemClickListener(this);
        listView.setPullLoadEnable(true);
        listView.setXListViewListener(this);
        imageviewTelephone.setOnClickListener(new TelephoneClickListener(this,tel_nummber));
        textViewTitle.setTypeface(typeface);
        textViewTitle.setText("展会新闻");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,ShowHtmlActivity.class);
        intent.putExtra("url", Tool.makeNewsURL(mExhibition.getExKey(),newsData.getList().get(position-1).getNewsKey()));
        intent.putExtra("title","展会新闻");
        intent.putExtra("exKey",mExhibition.getExKey());
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
                onLoad();
            }
        }, 2000);
    }

    class LoadDataAsyncTask extends AsyncTask<Void,Integer,Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        String str = mController.getService().ExNewsList(mExhibition.getExKey());
                        newsData = new Gson().fromJson(str, NewList.class);
                        for(NewList.News news : newsData.getList()){
                            HashMap<String,String> map = new HashMap<String, String>();
                            map.put("exKey",mExhibition.getExKey());
                            map.put("newsKey",news.getNewsKey());
                            map.put("newsTitle",news.getTitle());
                            data.add(map);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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