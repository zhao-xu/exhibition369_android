package com.threeH.MyExhibition.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.HomePageSearchListAdapter;
import com.threeH.MyExhibition.entities.EnrollExhibition;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.entities.UnEnrollExhibition;
import com.threeH.MyExhibition.service.ClientController;
import com.threeH.MyExhibition.widget.MultiDirectionSlidingDrawer;
import com.threeH.MyExhibition.widget.PullToRefreshView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends BaseActivity implements ActivityInterface,
        OnClickListener,AdapterView.OnItemClickListener {
    private List<HashMap<String, String>> enrollDataes = new ArrayList<HashMap<String, String>>();
    private List<HashMap<String, String>> exhibitionDataes = new ArrayList<HashMap<String, String>>();
    private EditText mEditText;
    private Button mSearchButton;
    private Button mOpenButton, mCloseButton;
    private ListView mEnrollStatusListView;
    private PullToRefreshView mSearchResult;
    private ClientController mController;
//    private MultiDirectionSlidingDrawer mSlidingDrawer;
    private EnrollExhibition.EnrollStatus mEnrollStatus;
    private List<EnrollExhibition.EnrollStatus> mEnExhibitions = new ArrayList<EnrollExhibition.EnrollStatus>();
    private Exhibition mExhibition;
    private List<Exhibition> mUnEnrollExhibitions = new ArrayList<Exhibition>();
    private HomePageSearchListAdapter mSearchAdapter;
    private String mSearchName = "";
    private long mLast;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.head_page);
        findView();
        initdata();
        addAction();

        mSearchResult.setTag("Setting_Blocklist");
        mSearchResult.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh(int which) {
                switch (which) {
                    case PullToRefreshView.HEADER:
                        refresh();
                        break;
                    case PullToRefreshView.FOOTER:
                        sendrequst(mLast,mSearchName);
                        break;
                }
            }
        });
        mSearchAdapter = new HomePageSearchListAdapter(context,exhibitionDataes);
        mSearchResult.setAdapter(mSearchAdapter);
    }

    @Override
    public void findView() {
        mEditText = (EditText) findViewById(R.id.titlebar_et);
        mEnrollStatusListView = (ListView) findViewById(R.id.enroll_status_listview);
        mSearchResult = (PullToRefreshView) findViewById(R.id.search_result_listview);
        mSearchButton = (Button) findViewById(R.id.search_btn);
    }

    @Override
    public void initdata() {
        mController = ClientController.getController(this);
        refresh();
    }

    @Override
    public void addAction() {

//        mCloseButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mSlidingDrawer.animateClose();
//            }
//        });
//
//        mOpenButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (!mSlidingDrawer.isOpened())
//                    mSlidingDrawer.animateOpen();
//            }
//        });
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mOpenButton = (Button) findViewById(R.id.button_open);
        mCloseButton = (Button) findViewById(R.id.button_close);
//        mSlidingDrawer = (MultiDirectionSlidingDrawer) findViewById(R.id.slider_drawer);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
    private void getNetData(){
        try {
            final String mEnrollJson = mController.getService().ErollExList(token);
            mEnExhibitions = mGson.fromJson(mEnrollJson,EnrollExhibition.class).getList();

            mSearchAdapter = new HomePageSearchListAdapter(context,exhibitionDataes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void refresh() {
        exhibitionDataes.clear();
        mLast = -1;
        sendrequst(mLast,mSearchName);
    }

    private  void sendrequst(final long last,final String name) {
        try {
            mUnEnrollExhibitions.clear();
            final String mUnEnrollJson = mController.getService().UnErollExList(token,0,last,name);
            System.out.println("2113123====="+mUnEnrollJson);
            mUnEnrollExhibitions = mGson.fromJson(mUnEnrollJson,UnEnrollExhibition.class).getList();
            System.out.println("12313131321====="+mUnEnrollExhibitions.size());
            for(int i = 0;i<mUnEnrollExhibitions.size();i++){
                mLast = mUnEnrollExhibitions.get(mUnEnrollExhibitions.size()-1).getCreatedAt();
//                sendrequst(mLast,name);
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("name",mExhibition.getName());
                map.put("icon",mExhibition.getExKey());
                map.put("address",mExhibition.getAddress());
                map.put("data",mExhibition.getDate());
                map.put("the_me",mExhibition.getOrganizer());
                exhibitionDataes.add(map);
            }
            System.out.println("1232123132======="+exhibitionDataes.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
