package com.threeH.MyExhibition.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 未报名展会列表
 */
public class ExhibitionList implements Serializable {
    /**
     * 返回搜索的关键字匹配
     */
    private String name;
    /**
     * 获取展会数据
     * 第一页数据设置为last = -1，
     * 第二页设置为第一页最后一条记录的createAt字段值
     */
    private Long last;
    /**
     * 展会数组
     */
    private ArrayList<Exhibition> list = new ArrayList<Exhibition>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLast() {
        return last;
    }

    public void setLast(Long last) {
        this.last = last;
    }

    public ArrayList<Exhibition> getList() {
        return list;
    }

    public void setList(ArrayList<Exhibition> list) {
        this.list = list;
    }
}
