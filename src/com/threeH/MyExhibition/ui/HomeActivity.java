package com.threeH.MyExhibition.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import cn.mobiledaily.module.android.module.mobilepush.service.helper.OnMessageListener;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.HomePageEnrollListAdapter;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.entities.UnEnrollExhibition;
import com.threeH.MyExhibition.service.ClientController;
import com.threeH.MyExhibition.widget.MultiDirectionSlidingDrawer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends BaseActivity implements ActivityInterface {
    private Gson mGson = new Gson();
    private List<HashMap<String, String>> enrollDataes = new ArrayList<HashMap<String, String>>();
    private List<HashMap<String, String>> exhibitionDataes = new ArrayList<HashMap<String, String>>();
    private EditText mEditText;
    private Button mSearchButton;
    private Button mOpenButton, mCloseButton;
    private ListView mEnrollStatus, mSearchResult;
    private ClientController mController;
    private MultiDirectionSlidingDrawer mSlidingDrawer;
    private HomePageEnrollListAdapter adapter;

    private ListView exhibitionListView;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.home);
        initdata();
        findView();
        addAction();
    }

    @Override
    public void findView() {
        exhibitionListView = (ListView) this.findViewById(R.id.home_listview);

    }

    @Override
    public void initdata() {
        mController = ClientController.getController(this);
        try {
            String str = mController.getService().UnErollExList("",-1,-1,"");
            UnEnrollExhibition  allExhibitionData = new Gson().fromJson(str,UnEnrollExhibition.class);
            List<HashMap<String,String>> data = new ArrayList<HashMap<String, String>>();
            for(Exhibition exhibition : allExhibitionData.getList()){
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("exhibitionName",exhibition.getName() + "\n" + exhibition.getDate());
                data.add(map);
            }
            adapter = new HomePageEnrollListAdapter(HomeActivity.this,data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addAction() {
       /* mSearchButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("21111111111132414124124");

                *//*try{
                AndroidMessageClient client = new AndroidMessageClient();
                client.init("pjqandroid",new MyMessageListener());
Log.i("data","link server");
                }catch (Exception e){
Log.e("error",e.getMessage());
                }*//*
            }
        });
        mCloseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlidingDrawer.animateClose();
            }
        });

        mOpenButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!mSlidingDrawer.isOpened())
                    mSlidingDrawer.animateOpen();
            }
        });*/

        exhibitionListView.setAdapter(adapter);
    }

    class MyMessageListener implements OnMessageListener{

        @Override
        public void onMessageReceived(String message) {
Log.i("data","o ye " + message);
        }
    }

    /*@Override
    public void onContentChanged() {
        super.onContentChanged();
        mOpenButton = (Button) findViewById(R.id.button_open);
        mCloseButton = (Button) findViewById(R.id.button_close);
        mSlidingDrawer = (MultiDirectionSlidingDrawer) findViewById(R.id.slider_drawer);
    }*/
}
