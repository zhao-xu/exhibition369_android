package com.threeH.MyExhibition.ui;

import android.os.Bundle;
import android.widget.ListView;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.adapters.MessageListAdapter;
import com.threeH.MyExhibition.entities.ExhibitionMessage;
import java.util.ArrayList;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.message_page);
        findView();
        addAction();
        mMessageListAdapter = new MessageListAdapter(context,mdataes);
        mMessageListView.setAdapter(mMessageListAdapter);
    }

    @Override
    public void findView() {
        mMessageListView = (ListView)findViewById(R.id.message_list_view);
    }

    @Override
    public void initdata() {
    }

    @Override
    public void addAction() {
        try {
            String mJsonData = mController.getService().ExMessage("1103","pjqAndroid");
            ExhibitionMessage mMessage = mGson.fromJson(mJsonData,ExhibitionMessage.class);
            for (ExhibitionMessage.ExMessage exMessage : mMessage.getList()){
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("date",exMessage.getCreatedAt()+"");
                map.put("content",exMessage.getContent());
                map.put("status",exMessage.getRead());
                mdataes.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}