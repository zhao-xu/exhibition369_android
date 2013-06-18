package com.threeH.MyExhibition.netty.client;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.cache.XmlDB;
import com.threeH.MyExhibition.common.StringPools;
import com.threeH.MyExhibition.domain.mobile.MessageObject;
import com.threeH.MyExhibition.domain.mobile.MessageObjects;
import com.threeH.MyExhibition.domain.mobile.RespToken;
import com.threeH.MyExhibition.domain.mobile.StringMessage;
import com.threeH.MyExhibition.receiver.MessageReceiver;
import com.threeH.MyExhibition.tools.Resources;
import org.jboss.netty.channel.*;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 监听消息回调
 *
 * @author pjq
 */
public class ClientHandler extends SimpleChannelUpstreamHandler {
    private Context context;

    public ClientHandler(Context context) {
        this.context = context;
    }

    public ClientHandler() {
    }

    /**
     * linkService(socket连接)回调
     */
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Object obj = e.getMessage();
        if (obj != null && obj instanceof MessageObject) {
            MessageObject resp = null;
            switch (((MessageObject) obj).getType()) {
                case REQ_PING:
                    resp = MessageObjects.respPong();
                    break;
                case RESP_TOKEN:
                    XmlDB.getInstance(context).saveKey(StringPools.SERVICE_TOKEN, ((RespToken) obj).getToken());
                    break;

                case STRING:
                    String message = ((StringMessage) obj).getValue();
                    addMessageToList(message);
                    Notification notification = new Notification(R.drawable.ic_loading_40x40_01, message, System.currentTimeMillis());
//                    Intent intent = new Intent(context, MessageActivity.class);
//                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//                    notification.setLatestEventInfo(context, "推送的消息", message, pendingIntent);
                    NotificationManager noManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notification.defaults = Notification.DEFAULT_SOUND;
                    noManager.notify(110, notification);
                    break;
            }
            if (resp != null) {
                ctx.getChannel().write(resp);
            }
        }
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelConnected(ctx, e);
        Intent intent = new Intent(context, MessageReceiver.class);
        intent.putExtra("latitude", Resources.latitude);
        intent.putExtra("longitude", Resources.longitude);
        intent.putExtra("address", Resources.address);
        context.sendBroadcast(intent);
    }

    private void addMessageToList(String message) {
        Map<String, Object> map = new HashMap<String, Object>();
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        StringBuilder time = new StringBuilder();
        time.append(hour + ":");
        time.append(minute + "  ");
        map.put("timeAndContent", time.toString() + "         " + message);
        //Resources.messageMap.add(map);         
//        Tools.saveMessage(map,context);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        try {
            e.getChannel().close();
        } catch (Exception ignore) {
        }
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        //super.channelDisconnected(ctx, e);   
        Resources.isSocketLinked = false;
        e.getChannel().close();
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        super.channelClosed(ctx, e);
    }
}
