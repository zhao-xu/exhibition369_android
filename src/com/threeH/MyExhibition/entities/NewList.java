package com.threeH.MyExhibition.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 新闻列表
 */
public class NewList implements Serializable {
    /**
     * 展会标识
     */
    private String exKey;
    /**
     * 新闻list
     */
    private ArrayList<News> list = new ArrayList<News>();

    public String getExKey() {
        return exKey;
    }

    public void setExKey(String exKey) {
        this.exKey = exKey;
    }

    public ArrayList<News> getList() {
        return list;
    }

    public void setList(ArrayList<News> list) {
        this.list = list;
    }

    public class News implements Serializable {
        /**
         * 新闻标识
         */
        private String newsKey;
        /**
         * 新闻标题
         */
        private String title;
        /**
         * 新闻发布时间
         */
        private Long createdAt;

        public String getNewsKey() {
            return newsKey;
        }

        public void setNewsKey(String newsKey) {
            this.newsKey = newsKey;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Long createdAt) {
            this.createdAt = createdAt;
        }
    }
}
