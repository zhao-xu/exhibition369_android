package cn.mobiledaily.module.mobilepush.domain.android;

import java.io.Serializable;

public class MessageObject implements Serializable {
    private MessageType type;

    public MessageObject() {
    }

    public MessageObject(MessageType type) {
        this.type = type;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
