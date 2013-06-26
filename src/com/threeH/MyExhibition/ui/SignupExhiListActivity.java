package com.threeH.MyExhibition.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.SignExhiListAdapter;
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
public class SignupExhiListActivity extends BaseActivity implements ActivityInterface,AdapterView.OnItemClickListener {
    private ListView mListView;
    private List<HashMap<String,String>> mdataes = new ArrayList<HashMap<String,String>>();
    private SignExhiListAdapter mSignExhiListAdapter;
    private EnrollExhibition.EnrollStatus[] enrollStatuses;
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
    }

    @Override
    public void findView() {
        mListView = (ListView)findViewById(R.id.signup_exhi_listview);
    }
    @Override
    public void addAction() {
        mListView.setAdapter(mSignExhiListAdapter);
        mListView.setDividerHeight(0);
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
        SharedPreferences sharedPreferences =   context.getSharedPreferences("allExhibitionData", Activity.MODE_PRIVATE);
        String strExhibitionData = sharedPreferences.getString("exhibitionData",null);
        UnEnrollExhibition allExhibitionData = new Gson().fromJson(strExhibitionData,UnEnrollExhibition.class);
        for(Exhibition exhibition : allExhibitionData.getList()){
            if(null != exKey && exhibition.getExKey().equals(exKey)){
                return exhibition;
            }
        }
        return null;
    }
}
