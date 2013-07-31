package com.threeH.MyExhibition.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 已报名展会列表
 */
public class MyExhibitionList implements Serializable {

    private Exhibition[] myExhibitions;

    public Exhibition[] getMyExhibitions() {
        return myExhibitions;
    }

    public void setMyExhibitions(Exhibition[] myExhibitions) {
        this.myExhibitions = myExhibitions;
    }
}
