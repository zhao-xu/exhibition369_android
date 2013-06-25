package com.threeH.MyExhibition.ui;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.*;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.MessageListAdapter;
import com.threeH.MyExhibition.entities.ExhibitionMessage;
import com.threeH.MyExhibition.listener.TelephoneClickListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 13-6-19
 * Time: 上午11:38
 * To change this template use File | Settings | File Templates.
 */
public class MessageActivity extends BaseActivity implements ActivityInterface{
    private List<HashMap<String,String>> mdataes = new ArrayList<HashMap<String, String>>();
    private ListView mMessageListView;
    private MessageListAdapter mMessageListAdapter;
    private ImageView imageviewTelephone;
    private TextView textViewTitle;
    private String exKey;
    private Typeface typeface;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(1 == msg.what){
                mMessageListAdapter = new MessageListAdapter(context,mdataes);
                mMessageListView.setAdapter(mMessageListAdapter);
            }
        }
    };
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.message_page);
        findView();
        initdata();
        addAction();

    }

    @Override
    public void findView() {
        mMessageListView = (ListView)findViewById(R.id.message_list_view);
        imageviewTelephone = (ImageView) this.findViewById(R.id.exhibition_titlebar_button_telephone);
        textViewTitle = (TextView) this.findViewById(R.id.exhibition_titlebar_textview_title);
    }
    @Override
    public void initdata() {
        exKey = getIntent().getStringExtra("exhibitionKey");
        typeface = Typeface.createFromAsset(context.getAssets(),"fonts/msyh.ttf");
        LoadMessageTask loadMessageTask = new LoadMessageTask();
        loadMessageTask.execute();
    }

    @Override
    public void addAction() {
        imageviewTelephone.setOnClickListener(new TelephoneClickListener(this,tel_nummber));
        textViewTitle.setTypeface(typeface);
        textViewTitle.setText("消息");
    }

    class LoadMessageTask extends AsyncTask<Void,Integer,Integer>{

        @Override
        protected Integer doInBackground(Void... params) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String mJsonData = mController.getService().ExMessage(exKey,token);
                        ExhibitionMessage mMessage = mGson.fromJson(mJsonData,ExhibitionMessage.class);
                        for (ExhibitionMessage.ExMessage exMessage : mMessage.getList()){
                            HashMap<String,String> map = new HashMap<String, String>();
                            map.put("date",new Date(exMessage.getCreatedAt()).toLocaleString());
                            map.put("content",exMessage.getContent());
                            map.put("status",exMessage.getRead());
                            mdataes.add(map);
                        }
                        Message message = handler.obtainMessage();
                        message.what = 1;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            return null;
        }
    }
}