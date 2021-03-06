package com.threeH.MyExhibition.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import com.threeH.MyExhibition.cache.XmlDB;
import com.threeH.MyExhibition.common.StringPools;
import com.threeH.MyExhibition.service.ClientContext;
import com.threeH.MyExhibition.service.ClientService;
import com.threeH.MyExhibition.service.ClientServiceImplForNet;

/**
 * 消息广播接收者
 *
 * @author pjq
 */
public class MessageReceiver extends BroadcastReceiver {
    private Context context;
    private ClientService clientService;
    private double latitude;
    private double longitude;
    private String address = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        ClientContext clientContext = ClientContext.createClientContext();
        clientService = new ClientServiceImplForNet(clientContext);
        latitude = intent.getDoubleExtra("latitude", 0.0);
        longitude = intent.getDoubleExtra("longitude", 0.0);
        address = intent.getStringExtra("address");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    registerService();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                checkIn();
            }
        }).start();
    }

    /**
     * 注册
     */
    private void registerService() {
        try {
            if (!XmlDB.getInstance(context)
                    .getKeyStringValue(StringPools.SERVICE_TOKEN, "")
                    .equals(""))
                clientService.registerService(
                        XmlDB.getInstance(context).getKeyStringValue(
                                StringPools.SERVICE_TOKEN, ""), "CCBN",
                        "ANDROID");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 签到
     */
    private void checkIn() {
        try {
            System.out.println("21312321312312321"
                    + XmlDB.getInstance(context).getKeyStringValue(
                    StringPools.SERVICE_TOKEN, ""));
            clientService.checkIn(
                    XmlDB.getInstance(context).getKeyStringValue(
                            StringPools.SERVICE_TOKEN, ""),
                    "CCBN",
                    latitude,
                    longitude,
                    address);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
