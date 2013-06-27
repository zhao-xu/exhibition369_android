package com.threeH.MyExhibition.tools;

import android.util.Log;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.entities.UnEnrollExhibition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public static String makeNewsIconURL(String exKey,String newsKey){
        StringBuilder builder = new StringBuilder(ASSET_SERVER);
        builder.append(exKey);
        builder.append("/news/");
        builder.append(newsKey);
        builder.append(".png");
        return builder.toString();
    }

    public static String makeExhibitionIconURL(String exKey){
        StringBuilder builder = new StringBuilder(ASSET_SERVER);
        builder.append(exKey);
        builder.append("/icon.png");
        return builder.toString();
    }

    public static String makeQrcodeURL(String exKey,String token){
        StringBuilder builder = new StringBuilder(ASSET_SERVER);
        builder.append(exKey);
        builder.append("/qrcode/");
        builder.append(token);
        builder.append(".png");
        return builder.toString();
    }

    public static List<HashMap<String,String>> makeAllExhibitionListAdapterData(UnEnrollExhibition allExhibitionData){
        List<HashMap<String,String>> data = new ArrayList<HashMap<String, String>>();
        if(null != allExhibitionData){
            for(Exhibition exhibition : allExhibitionData.getList()){
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("exhibitionName",exhibition.getName());
                map.put("exhibitionDate",exhibition.getDate());
                map.put("exhibitionAddress",exhibition.getAddress());
                map.put("exhibitionSponser",exhibition.getOrganizer());
                map.put("exhibitionApplied",exhibition.getApplied());
                map.put("exhibitionExkey",exhibition.getExKey());
                data.add(map);
            }
        }
        return data;
    }

}
