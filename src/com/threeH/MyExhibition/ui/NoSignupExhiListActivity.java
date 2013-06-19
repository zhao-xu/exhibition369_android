package com.threeH.MyExhibition.ui;

import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.HomePageEnrollListAdapter;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.entities.UnEnrollExhibition;
import com.threeH.MyExhibition.service.ClientController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-6-19
 * Time: 下午2:29
 * To change this template use File | Settings | File Templates.
 */
public class NoSignupExhiListActivity extends BaseActivity implements ActivityInterface {
    private ListView listView;
    private ClientController mController;
    private HomePageEnrollListAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        mController = ClientController.getController(this);
        try {
            String str = mController.getService().UnErollExList("",-1,-1,"");
            UnEnrollExhibition allExhibitionData = new Gson().fromJson(str,UnEnrollExhibition.class);
            List<HashMap<String,String>> data = new ArrayList<HashMap<String, String>>();
            for(Exhibition exhibition : allExhibitionData.getList()){
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("exhibitionName",exhibition.getName() + "\n" + exhibition.getDate());
                data.add(map);
            }
            adapter = new HomePageEnrollListAdapter(this,data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addAction() {
        listView.setAdapter(adapter);
    }
}
