package com.threeH.MyExhibition.tools;

import android.content.Context;
import com.google.gson.Gson;
import com.threeH.MyExhibition.cache.XmlDB;
import com.threeH.MyExhibition.common.StringPools;
import com.threeH.MyExhibition.entities.Exhibition;
import com.threeH.MyExhibition.entities.ExhibitionList;
import com.threeH.MyExhibition.entities.OverAllConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-6-21
 * Time: 下午5:33
 * To change this template use File | Settings | File Templates.
 */
public class Tool {
    public static final String ASSET_SERVER = "http://180.168.35.37:8080/e369_asset/";
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

    public static List<Exhibition> makeAllExhibitionListAdapterData(ExhibitionList allExhibitionData){
        List<Exhibition> data = new ArrayList<Exhibition>();
        if(null != allExhibitionData){
            for(Exhibition exhibition : allExhibitionData.getList()){
                data.add(exhibition);
            }
        }
        return data;
    }

    /**
     * 获取保存在全局配置中的电话号码
     * @return
     */
    public static String getTelephone(Context context){
        Gson gson = new Gson();
        OverAllConfig mOverAllConfig = gson.fromJson(XmlDB.getInstance(context).
                getKeyStringValue(StringPools.OVERALL_CONFIG, ""),OverAllConfig.class);
        if(null != mOverAllConfig){
            return mOverAllConfig.getTel();
        }
        return null;
    }
}
