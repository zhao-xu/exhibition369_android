package com.threeH.MyExhibition.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.HomePageEnrollListAdapter;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.entities.ExhibitionNews;
import com.threeH.MyExhibition.entities.UnEnrollExhibition;
import com.threeH.MyExhibition.service.ClientController;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.unsingup_exhibitionlist);
        initdata();
        findView();
        addAction();
    }

    @Override
    public void findView() {
        listView = (ListView) this.findViewById(R.id.unsingup_exhibition_listview);
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
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,ShowHtmlActivity.class);
        intent.putExtra("url",makeURL(exKey,newsData.getList().get(position).getNewsKey()));
        startActivity(intent);
    }

    public String makeURL(String exKey,String newsKey){
        StringBuilder builder = new StringBuilder("http://180.168.35.37:8080/e369_asset/");
        builder.append(exKey);
        builder.append("/news/");
        builder.append(newsKey);
        builder.append(".html");
        return builder.toString();
    }
}