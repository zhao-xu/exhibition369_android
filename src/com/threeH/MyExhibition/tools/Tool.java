package com.threeH.MyExhibition.tools;

import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-6-21
 * Time: 下午5:33
 * To change this template use File | Settings | File Templates.
 */
public class Tool {
    private static final String ASSET_SERVER = "http://180.168.35.37:8080/e369_asset/";
    public static String makeNewsURL(String exKey,String newsKey){
        StringBuilder builder = new StringBuilder(ASSET_SERVER);
        builder.append(exKey);
        builder.append("/news/");
        builder.append(newsKey);
        builder.append(".html");
        return builder.toString();
    }

    public static String makeExhibitionIconURL(String exKey){
        StringBuilder builder = new StringBuilder(ASSET_SERVER);
        builder.append(exKey);
        builder.append("/icon.png");
Log.i("data",builder.toString());
        return builder.toString();
    }
}
