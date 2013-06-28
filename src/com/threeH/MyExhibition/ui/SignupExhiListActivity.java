package com.threeH.MyExhibition.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.SignExhiListAdapter;
import com.threeH.MyExhibition.cache.XmlDB;
import com.threeH.MyExhibition.common.StringPools;
import com.threeH.MyExhibition.entities.EnrollExhibition;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.entities.UnEnrollExhibition;

import java.util.ArrayList;
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
            AdapterView.OnItemClickListener,AbsListView.OnScrollListener {
    private ListView mListView;
    private List<HashMap<String,String>> mdataes = new ArrayList<HashMap<String,String>>();
    private SignExhiListAdapter mSignExhiListAdapter;
    private EnrollExhibition.EnrollStatus[] enrollStatuses;
    private LayoutInflater mInflater;
    private View viewFooter;
    private LinearLayout linlLoad;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.signup_exhibition_page);
        initdata();
        findView();
        addAction();
    }
    @Override
    public void initdata() {
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
                map.put("status",mEnrollStatus.getStatus());
                mdataes.add(map);
            }
        } catch (Exception e) {
        }
        mSignExhiListAdapter = new SignExhiListAdapter(context,mdataes);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public void findView() {
        mListView = (ListView)findViewById(R.id.signup_exhi_listview);
        viewFooter = mInflater.inflate(R.layout.list_footer_new,null);
        linlLoad = (LinearLayout) viewFooter.findViewById(R.id.list_footer_new);
    }
    @Override
    public void addAction() {
        linlLoad.setVisibility(View.GONE);
        mListView.addFooterView(viewFooter);
        mListView.setAdapter(mSignExhiListAdapter);
        mListView.setDividerHeight(0);
        mListView.setOnScrollListener(this);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,ExhibitionActivity.class);
        intent.putExtra("exKey",enrollStatuses[position].getExKey());
        Exhibition exhibition = getExhibitionData(enrollStatuses[position].getExKey());
        intent.putExtra("exAddress",exhibition.getAddress());
        intent.putExtra("exTime",exhibition.getDate());
        intent.putExtra("exTheme",exhibition.getName());
        intent.putExtra("exSponser",exhibition.getOrganizer());
        intent.putExtra("token",token);
        startActivity(intent);
    }

    private Exhibition getExhibitionData(String exKey) {
        String strExhibitionData = XmlDB.getInstance(context).getKeyStringValue(StringPools.ALL_EXHIBITION_DATA,"");
        UnEnrollExhibition allExhibitionData = new Gson().fromJson(strExhibitionData,UnEnrollExhibition.class);
        for(Exhibition exhibition : allExhibitionData.getList()){
            if(null != exKey && exhibition.getExKey().equals(exKey)){
                return exhibition;
            }
        }
        return null;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        /*if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
            if(view.getLastVisiblePosition() == view.getCount() - 1){
                linlLoad.setVisibility(View.VISIBLE);
                mSignExhiListAdapter.notifyDataSetChanged();
            }
        }*/
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


}
