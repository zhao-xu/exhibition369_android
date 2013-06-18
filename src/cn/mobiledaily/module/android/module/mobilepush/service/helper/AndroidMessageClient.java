package cn.mobiledaily.module.android.module.mobilepush.service.helper;

import android.util.Log;
import cn.mobiledaily.module.android.common.config.AppConfig;
import cn.mobiledaily.module.mobilepush.domain.android.MessageObject;
import cn.mobiledaily.module.mobilepush.domain.android.MessageObjects;
import cn.mobiledaily.module.mobilepush.domain.android.TextMessage;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class AndroidMessageClient {
    String host;
    int port;
    ClientBootstrap clientBootstrap;
    String token;
    OnMessageListener onMessageListener;

    public void init(String token, OnMessageListener listener) {
        host = AppConfig.MESSAGE_SERVER;
        port = AppConfig.MESSAGE_SERVER_PORT;
        onMessageListener = listener;
        if ((token == null) || token.length() == 0) {
            throw new RuntimeException("require token");
        }
        if (listener == null) {
            throw new RuntimeException("require OnMessageListener");
        }
        this.token = token;
        clientBootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        clientBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline result = new DefaultChannelPipeline();
                result.addLast("encode", new ObjectEncoder());
                result.addLast("decode", new ObjectDecoder(
                        ClassResolvers.softCachingConcurrentResolver(MessageObject.class.getClassLoader())));
                result.addLast("handler", new ChannelHandler());
                return result;
            }
        });
        clientBootstrap.connect(new InetSocketAddress(host, port));
    }

    class ChannelHandler extends SimpleChannelHandler {
        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            Object obj = e.getMessage();
            if (obj instanceof MessageObject) {
                MessageObject resp = null;
                switch (((MessageObject) obj).getType()) {
                    case REQ_PING:
                        resp = MessageObjects.respPong();
                        break;
                    case REQ_TOKEN:
                        resp = MessageObjects.respToken(token);
                        break;
                    case TEXT:
                        TextMessage message = (TextMessage) obj;
                        onMessageListener.onMessageReceived(message.getText());
                        break;
                    case REQ_MESSAGE:
                        ctx.getChannel().write(MessageObjects.reqMessage());
                        break;
                }
                if (resp != null) {
                    ctx.getChannel().write(resp);
                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
             super.exceptionCaught(ctx, e);
            ctx.getChannel().close();
        }
    }
}
