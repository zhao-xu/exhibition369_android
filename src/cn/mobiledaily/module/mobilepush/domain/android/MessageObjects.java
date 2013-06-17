package cn.mobiledaily.module.mobilepush.domain.android;

public class MessageObjects {
    private static final MessageObject PING = new MessageObject(MessageType.REQ_PING);
    private static final MessageObject PONG = new MessageObject(MessageType.RESP_PONG);
    private static final MessageObject REQ_TOKEN = new MessageObject(MessageType.REQ_TOKEN);
    private static final MessageObject REQ_MESSAGE = new MessageObject(MessageType.REQ_MESSAGE);

    public static MessageObject reqPing() {
        return PING;
    }

    public static MessageObject respPong() {
        return PONG;
    }

    public static MessageObject reqToken() {
        return REQ_TOKEN;
    }

    public static RespToken respToken(String token) {
        return new RespToken(token);
    }

    public static TextMessage textMessage(String value) {
        return new TextMessage(value);
    }

    public static MessageObject reqMessage() {
        return REQ_MESSAGE;
    }
}
