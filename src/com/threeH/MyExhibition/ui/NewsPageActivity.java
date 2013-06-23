package com.threeH.MyExhibition.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.HomePageEnrollListAdapter;
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
public class NewsPageActivity extends Activity  implements ActivityInterface,AdapterView.OnItemClickListener{
    private ListView listView;
    private SimpleAdapter adapter;
    private ClientController mController;
    private String exKey;
    private ExhibitionNews newsData;
    private ImageButton button_telephone;
    private TextView textViewTitle;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.newslist);
        initdata();
        findView();
        addAction();
    }

    @Override
    public void findView() {
        listView = (ListView) this.findViewById(R.id.newslist_listview);
        button_telephone = (ImageButton) this.findViewById(R.id.exhibition_titlebar_button_telephone);
        textViewTitle = (TextView) this.findViewById(R.id.exhibition_titlebar_textview_title);
    }

    @Override
    public void initdata() {
        exKey = getIntent().getStringExtra("exKey");
        mController = ClientController.getController(this);
        List<HashMap<String,String>> data = new ArrayList<HashMap<String, String>>();
        try {
            String str = mController.getService().ExNewsList(exKey);
            newsData = new Gson().fromJson(str, ExhibitionNews.class);

            for(ExhibitionNews.News news : newsData.getList()){
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("newsTitle",news.getTitle());
                data.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter =  new SimpleAdapter(this,
                                     data,
                                     R.layout.newslist_item,
                                     new String[]{"newsTitle"},
                                     new int[]{R.id.newslist_item_text});
    }

    @Override
    public void addAction() {
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        button_telephone.setOnClickListener(new TelephoneClickListener(this));
        textViewTitle.setText("展会新闻");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,ShowHtmlActivity.class);
        intent.putExtra("url", Tool.makeNewsURL(exKey,newsData.getList().get(position).getNewsKey()));
        intent.putExtra("title","展会新闻");
        startActivity(intent);
    }

}