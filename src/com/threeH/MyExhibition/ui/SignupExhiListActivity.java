package com.threeH.MyExhibition.ui;

import android.os.Bundle;
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
public class SignupExhiListActivity extends BaseActivity implements ActivityInterface {
    private ListView mListView;
    private List<HashMap<String,String>> mdataes = new ArrayList<HashMap<String,String>>();
    private SignExhiListAdapter mSignExhiListAdapter;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.signup_exhibition_page);
        findView();
        addAction();
        mSignExhiListAdapter = new SignExhiListAdapter(context,mdataes);
        mListView.setAdapter(mSignExhiListAdapter);
    }

    @Override
    public void findView() {
        mListView = (ListView)findViewById(R.id.signup_exhi_listview);
    }

    @Override
    public void initdata() {

    }

    @Override
    public void addAction() {
        try {
            String jsonData = mController.getService().ErollExList(token);
            EnrollExhibition mEnrollExhibition = mGson.fromJson(jsonData, EnrollExhibition.class);
            for(EnrollExhibition.EnrollStatus mEnrollStatus : mEnrollExhibition.getList()){
                HashMap<String,String> map =new HashMap<String,String>();
                map.put("icon",mEnrollStatus.getExKey());
                map.put("name",mEnrollStatus.getName());
                map.put("status",mEnrollStatus.getStatus());
                mdataes.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
