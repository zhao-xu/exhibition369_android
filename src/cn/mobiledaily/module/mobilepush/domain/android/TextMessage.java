package cn.mobiledaily.module.mobilepush.domain.android;

public class TextMessage extends MessageObject {
    private String text;

    public TextMessage(String text) {
        super(MessageType.TEXT);
        this.text = text;
    }

    public TextMessage() {
        super(MessageType.TEXT);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
