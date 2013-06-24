package com.threeH.MyExhibition.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.HomePageEnrollListAdapter;
import com.threeH.MyExhibition.entities.UnEnrollExhibition;
import com.threeH.MyExhibition.service.ClientController;
import com.threeH.MyExhibition.tools.Tool;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-6-19
 * Time: 下午2:17
 * To change this template use File | Settings | File Templates.
 */
public class HomeOfTabActivity extends TabActivity implements ActivityInterface{
    private TabHost tabhost;
    private RadioGroup radioGroup;
    private Button buttonSearch;
    private static final String TAB_SIGNUP = "tabSingup";
    private static final String TAB_NO_SIGNUP = "tabNoSingup";
    private ClientController mController;
    private EditText editText;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.exhibitionlist_page);
        initdata();
        findView();
        addAction();
    }

    @Override
    public void findView() {
        radioGroup = (RadioGroup) this.findViewById(R.id.homeoftab_radiogroup);
        buttonSearch = (Button) this.findViewById(R.id.search_btn);
        editText = (EditText) this.findViewById(R.id.titlebar_et);
    }

    @Override
    public void initdata() {
        tabhost = this.getTabHost();
        mController = ClientController.getController(this);
    }

    @Override
    public void addAction() {
        tabhost.addTab(tabhost.newTabSpec(TAB_SIGNUP).setIndicator(TAB_SIGNUP)
                .setContent(new Intent(this,SignupExhiListActivity.class)));
        tabhost.addTab(tabhost.newTabSpec(TAB_NO_SIGNUP).setIndicator(TAB_NO_SIGNUP)
                .setContent(new Intent(this,NoSignupExhiListActivity.class)));
        tabhost.setCurrentTabByTag(TAB_NO_SIGNUP);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.exhibitionlsit_radiobutton_singup:
                        tabhost.setCurrentTabByTag(TAB_SIGNUP);
                        radioGroup.setBackgroundResource(R.drawable.homepage_titlebar_background_exhibition);
                        break;
                    case R.id.exhibitionlsit_radiobutton_no_singup:
                        tabhost.setCurrentTabByTag(TAB_NO_SIGNUP);
                        radioGroup.setBackgroundResource(R.drawable.homepage_titlebar_background_signup);
                        break;
                }
            }
        });
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //v.setBackgroundResource(R.drawable.search_focus);
                try {
                    name = editText.getText().toString();
                    String str = mController.getService().UnErollExList("pjqAndroid",-1,-1,name);
                    UnEnrollExhibition allExhibitionData = new Gson().fromJson(str,UnEnrollExhibition.class);
                    List<HashMap<String,String>> data = Tool.makeAllExhibitionListAdapterData(allExhibitionData);
                    HomePageEnrollListAdapter adapter = new HomePageEnrollListAdapter(HomeOfTabActivity.this,data);
                    NoSignupExhiListActivity.listView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
