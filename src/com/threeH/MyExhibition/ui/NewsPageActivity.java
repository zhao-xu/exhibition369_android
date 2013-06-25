package com.threeH.MyExhibition.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.HomePageEnrollListAdapter;
import com.threeH.MyExhibition.adapters.NewslistAdapter;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.entities.ExhibitionNews;
import com.threeH.MyExhibition.entities.UnEnrollExhibition;
import com.threeH.MyExhibition.listener.TelephoneClickListener;
import com.threeH.MyExhibition.service.ClientController;
import com.threeH.MyExhibition.tools.Tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 13-6-19
 * Time: 上午11:38
 * To change this template use File | Settings | File Templates.
 */
public class NewsPageActivity extends BaseActivity  implements ActivityInterface,AdapterView.OnItemClickListener{
    private ListView listView;
    private NewslistAdapter adapter;
    private String exKey;
    private ExhibitionNews newsData;
    private ImageView imageviewTelephone;
    private TextView textViewTitle;
    private Typeface typeface;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(1 == msg.what){
                listView.setAdapter(adapter);
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.newslist);
        findView();
        initdata();
        addAction();
    }

    @Override
    public void findView() {
        listView = (ListView) this.findViewById(R.id.newslist_listview);
        imageviewTelephone = (ImageView) this.findViewById(R.id.exhibition_titlebar_button_telephone);
        textViewTitle = (TextView) this.findViewById(R.id.exhibition_titlebar_textview_title);
    }

    @Override
    public void initdata() {
        typeface = Typeface.createFromAsset(context.getAssets(),"fonts/msyh.ttf");
        exKey = getIntent().getStringExtra("exKey");
        LoadDataAsyncTask loadDataAsyncTask = new LoadDataAsyncTask();
        loadDataAsyncTask.execute();
    }

    @Override
    public void addAction() {
        //listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        imageviewTelephone.setOnClickListener(new TelephoneClickListener(this,tel_nummber));
        textViewTitle.setTypeface(typeface);
        textViewTitle.setText("展会新闻");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,ShowHtmlActivity.class);
        intent.putExtra("url", Tool.makeNewsURL(exKey,newsData.getList().get(position).getNewsKey()));
        intent.putExtra("title","展会新闻");
        startActivity(intent);
    }

    class LoadDataAsyncTask extends AsyncTask<Void,Integer,Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<HashMap<String,String>> data = new ArrayList<HashMap<String, String>>();
                    try {
                        String str = mController.getService().ExNewsList(exKey);
                        newsData = new Gson().fromJson(str, ExhibitionNews.class);
                        for(ExhibitionNews.News news : newsData.getList()){
                            HashMap<String,String> map = new HashMap<String, String>();
                            map.put("exKey",exKey);
                            map.put("newsKey",news.getNewsKey());
                            map.put("newsTitle",news.getTitle());
                            data.add(map);
                        }
                        adapter =  new NewslistAdapter(data,context);
                        Message message = handler.obtainMessage();
                        message.what = 1;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            return null;
        }
    }
}