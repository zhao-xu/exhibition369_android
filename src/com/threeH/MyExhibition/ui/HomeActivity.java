package com.threeH.MyExhibition.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import cn.mobiledaily.module.android.module.mobilepush.service.helper.AndroidMessageClient;
import cn.mobiledaily.module.android.module.mobilepush.service.helper.OnMessageListener;
import com.google.gson.Gson;
import com.threeH.MyExhibition.R;
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

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.head_page);
        initdata();
        findView();
        addAction();
//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    String str = mController.getService().OverAllData("android","4.0","1.0","00-00-00-00-00");
//                    System.out.print("qweyoqyeoiy===="+str);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
    }

    @Override
    public void findView() {
        mEditText = (EditText)findViewById(R.id.titlebar_et);
        mEnrollStatus = (ListView)findViewById(R.id.enroll_status_listview);
        mSearchResult = (ListView)findViewById(R.id.search_result_listview);
        mSearchButton = (Button)findViewById(R.id.search_btn);

        mEditText = (EditText) findViewById(R.id.titlebar_et);
        mEnrollStatus = (ListView) findViewById(R.id.enroll_status_listview);
        mSearchResult = (ListView) findViewById(R.id.search_result_listview);
        mSearchButton = (Button) findViewById(R.id.search_btn);
        mSearchButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("2133333213123131");
                try {
                    String str = mController.getService().OverAllData("android_phone", "4.0", "1.0", "00-00-00-00-00");
                    System.out.print("qweyoqyeoiy====" + str);
                    showShortText(str + "73913819");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void initdata() {
        mController = ClientController.getController(this);
    }

    @Override
    public void addAction() {
        mSearchButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                /*System.out.println("21111111111132414124124");
                try {
                    String str = mController.getService().UnErollExList("",-1,-1,"");
                    System.out.print("qweyoqyeoiy===="+str);
                    showShortText(str+"73913819");
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                AndroidMessageClient client = new AndroidMessageClient();
                client.init("test-android",new MyMessageListener());
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
        });

    }

    class MyMessageListener implements OnMessageListener{

        @Override
        public void onMessageReceived(String message) {
Log.i("data",message);
        }

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mOpenButton = (Button) findViewById(R.id.button_open);
        mCloseButton = (Button) findViewById(R.id.button_close);
        mSlidingDrawer = (MultiDirectionSlidingDrawer) findViewById(R.id.slider_drawer);
    }
}
