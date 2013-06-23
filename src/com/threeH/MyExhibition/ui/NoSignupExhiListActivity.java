package com.threeH.MyExhibition.ui;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import cn.mobiledaily.module.android.module.mobilepush.service.helper.AndroidMessageClient;
import cn.mobiledaily.module.android.module.mobilepush.service.helper.OnMessageListener;
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
    private HomePageEnrollListAdapter adapter;
    private UnEnrollExhibition allExhibitionData;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithNoTitle(R.layout.unsingup_exhibitionlist);
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
        try {
            String str = mController.getService().UnErollExList("pjqAndroid",-1,-1,"");
            if(null != str){
                saveExhibitionData(str);
            }
            allExhibitionData = new Gson().fromJson(str,UnEnrollExhibition.class);
            List<HashMap<String,String>> data = Tool.makeAllExhibitionListAdapterData(allExhibitionData);
            adapter = new HomePageEnrollListAdapter(this,data);

            AndroidMessageClient client = new AndroidMessageClient();
            client.init("pjqAndroid",new MyMessageListener());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void saveExhibitionData(String str) {
        SharedPreferences sharedPreferences =   getSharedPreferences("allExhibitionData", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("exhibitionData",str);
        editor.commit();
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

    class MyMessageListener implements OnMessageListener {

        @Override
        public void onMessageReceived(String message) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification  = new Notification(R.drawable.icon1,message,System.currentTimeMillis());
            Intent intent = new Intent(context,MessageActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
            notification.setLatestEventInfo(context,"展会消息通知",message,pendingIntent);
            notification.defaults = Notification.DEFAULT_SOUND;
            notificationManager.notify(202,notification);
        }
    }

}
