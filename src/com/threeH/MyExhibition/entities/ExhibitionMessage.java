package com.threeH.MyExhibition.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 13-6-9
 * Time: 下午3:01
 * To change this template use File | Settings | File Templates.
 */
public class ExhibitionMessage implements Serializable {
    private String exKey;
    private ArrayList<ExMessage> list = new ArrayList<ExMessage>();

    public String getExKey() {
        return exKey;
    }

    public void setExKey(String exKey) {
        this.exKey = exKey;
    }

    public ArrayList<ExMessage> getList() {
        return list;
    }

    public void setList(ArrayList<ExMessage> list) {
        this.list = list;
    }

    public class ExMessage implements Serializable {
        /**
         * 消息标识
         */
        private String msgKey;
        /**
         * 消息内容
         */
        private String content;
        /**
         * 消息创建时间
         */
        private long createdAt;
        /**
         * 已读状态，N 未读，Y 已读
         */
        private String read;

        String getMsgKey() {
            return msgKey;
        }

        void setMsgKey(String msgKey) {
            this.msgKey = msgKey;
        }

        public String getContent() {
            return content;
        }

        void setContent(String content) {
            this.content = content;
        }

        public long getCreatedAt() {
            return createdAt;
        }

        void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }

        public String getRead() {
            return read;
        }

        void setRead(String read) {
            this.read = read;
        }
    }
}
