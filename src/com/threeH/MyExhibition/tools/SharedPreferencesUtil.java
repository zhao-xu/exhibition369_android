package com.threeH.MyExhibition.tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pjq
 * Date: 13-7-16
 * Time: 上午10:12
 * To change this template use File | Settings | File Templates.
 */
public class SharedPreferencesUtil {
    private static final String SAVETAG = "list";

    /**
     *使用SharedPreferences保存对象类型的数据
     * 先将对象类型转化为二进制数据，然后用特定的字符集编码成字符串进行保存
     * @param object 要保存的对象
     * @param context
     * @param shaPreName 保存的文件名
     */
    public static void saveObject(Object object,Context context,String shaPreName){
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(shaPreName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        List<Object> list = getObject(context,shaPreName);
        if(null == list){
            list = new ArrayList<Object>();
        }
        list.add(object);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(list);
            String strList = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor.putString(SAVETAG, strList);
            editor.commit();
            oos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            try {
                baos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     *根据文件名取得存储的数据对象
     * 先将取得的数据转化成二进制数组，然后转化成对象
     * @param context
     * @param shaPreName    读取数据的文件名
     * @return
     */
    public static List<Object> getObject(Context context,String shaPreName){
        List<Object> list;
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(shaPreName, Activity.MODE_PRIVATE);
        String message  = sharedPreferences.getString(SAVETAG, "");
        byte[] buffer = Base64.decode(message.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            list = (List<Object>)ois.readObject();
            ois.close();
            return list;
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            try {
                bais.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }
}
