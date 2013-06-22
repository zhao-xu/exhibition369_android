package com.threeH.MyExhibition.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.HomePageEnrollListAdapter;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.entities.UnEnrollExhibition;
import com.threeH.MyExhibition.service.ClientController;
import com.threeH.MyExhibition.tools.Tool;

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
public class NoSignupExhiListActivity extends BaseActivity implements ActivityInterface,AdapterView.OnItemClickListener {
    public static   ListView listView;
    private ClientController mController;
    private HomePageEnrollListAdapter adapter;
    private UnEnrollExhibition allExhibitionData;
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
            String str = mController.getService().UnErollExList("pjqAndroid",-1,-1,"");
            allExhibitionData = new Gson().fromJson(str,UnEnrollExhibition.class);
            List<HashMap<String,String>> data = Tool.makeAllExhibitionListAdapterData(allExhibitionData);
            adapter = new HomePageEnrollListAdapter(this,data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addAction() {
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,ExhibitionActivity.class);
        intent.putExtra("exKey",allExhibitionData.getList().get(position).getExKey());
        startActivity(intent);
    }

}
