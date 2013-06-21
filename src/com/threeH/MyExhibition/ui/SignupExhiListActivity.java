package com.threeH.MyExhibition.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.SignExhiListAdapter;
import com.threeH.MyExhibition.entities.EnrollExhibition;
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
            String jsonData = mController.getService().ErollExList("pjqAndroid");
            enrollStatuses =  mGson.fromJson(jsonData, EnrollExhibition.EnrollStatus[].class);
            for(EnrollExhibition.EnrollStatus mEnrollStatus : enrollStatuses){
                HashMap<String,String> map =new HashMap<String,String>();
                map.put("icon",mEnrollStatus.getExKey());
                map.put("name",mEnrollStatus.getName());
                map.put("status",mEnrollStatus.getStatus());
                mdataes.add(map);
            }
        } catch (Exception e) {
            Log.e("data",e.getMessage());
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
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,ExhibitionActivity.class);
        intent.putExtra("exKey",enrollStatuses[position].getExKey());
        startActivity(intent);
    }
}
