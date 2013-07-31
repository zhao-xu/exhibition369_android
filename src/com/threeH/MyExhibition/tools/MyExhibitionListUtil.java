package com.threeH.MyExhibition.tools;

import android.content.Context;
import com.google.gson.Gson;
import com.threeH.MyExhibition.cache.XmlDB;
import com.threeH.MyExhibition.common.StringPools;
import com.threeH.MyExhibition.entities.Exhibition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-7-31
 * Time: 上午11:43
 * To change this template use File | Settings | File Templates.
 */
public class MyExhibitionListUtil {
    private String mJsonData;
    private Exhibition[] mMyExhibitons;
    private List<Object> mList;
    private List<HashMap<String,String>> mListMyexhibiton =
            new ArrayList<HashMap<String, String>>();
    private static MyExhibitionListUtil instance;
    private Context mContext;
    public static MyExhibitionListUtil  getInstance(Context context) {
        if(instance == null){
            synchronized (MyExhibitionListUtil.class){
                instance = new MyExhibitionListUtil(context);
            }
        }
        return instance;
    }

    public MyExhibitionListUtil(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 加载本地存储的我的展会的数据
     */
    public void initMyExhiibitonList(){
        mListMyexhibiton.clear();
        mJsonData =  XmlDB.getInstance(mContext).
                getKeyStringValue(StringPools.SIGNUP_EXHIBITION_DATA, "");
        mMyExhibitons =
                new Gson().fromJson(mJsonData, Exhibition[].class);
        mList  =
                SharedPreferencesUtil.getObject(mContext, StringPools.SCAN_EXHIBITION_DATA);
        if(mList != null){
            for(Object object : mList){
                addToList(((Exhibition)object).getExKey());
            }
        }
        for(Exhibition exhibition : mMyExhibitons){
            addToList(exhibition.getExKey());
        }
    }

    /**
     * 添加到我的展会列表
     * @param exKey
     */
    private void addToList(String exKey){
        HashMap<String,String> map =new HashMap<String,String>();
        map.put("exhibitionExkey",exKey);
        mListMyexhibiton.add(map);
    }

    /**
     * 判断该展会是否已经存在在我的展会列表中
     * @param exkey 展会标识
     * @return 存在返回true 不存在返回false
     */
    public boolean isMyExhibiton(String exkey){
        for (HashMap<String, String> hashMap : mListMyexhibiton) {
            if (hashMap.get("exhibitionExkey").contains(exkey)) {
                return true;
            }
        }
        return false;
    }
}
