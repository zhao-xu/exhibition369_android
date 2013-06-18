package com.threeH.MyExhibition.service;

import android.util.Log;
import com.threeH.MyExhibition.common.StringPools;
import com.threeH.MyExhibition.tools.CustomerHttpClient;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.HashMap;
import java.util.Map;

public class ClientServiceImplForNet implements ClientService {
    private ClientController controller;

    public ClientServiceImplForNet() {

    }

    public ClientServiceImplForNet(ClientContext context) {
        super();
    }

    public String getBusinessData(String name, String version) throws Exception {
        return version;
    }

    /**
     * 初始化App信息
     */
    public String findAll() throws Exception {
        try {
//            String response = CustomerHttpClient.get("", "");
//            return response;
        } catch (Exception ignore) {
        }
        return null;
    }

    /**
     * 全局数据获取
     * type	必须为android_phone
     * osVer Android版本
     * ver	应用版本
     * token 网卡地址
     * method:get
     */
    @Override
    public String OverAllData(String type, String osVer, String ver, String token) throws Exception {
        final String url = StringPools.APP_SERVER + "/rest/configurations/get";

        Map<String, String> map = new HashMap<String, String>();
        map.put("type", type);
        map.put("osVer", osVer);
        map.put("ver", ver);
        map.put("token", token);
        try {
            System.out.println("1212121212====" + url);
//            String response = CustomerHttpClient.get(url, map);
            String res01 = CustomerHttpClient.getRequest(url, map);
            System.out.print("11312312313123" + res01);
            return res01;
//            return response;
        } catch (RuntimeException e) {
            // 请求失败或者连接失败
            System.out.println("新闻列表请求失败或者连接失败");
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 未报名展会列表
     * token 手机唯一标识码，用于过滤已报名展会(为服务器返回值)
     * size 返回记录条数，size = -1时返回不分页的所有数据
     * last	第一页数据设置为last = -1，第二页设置为第一页最后一条记录的createAt字段值
     * name 展会名关键字，搜索时按此字段值匹配，不设置时不进行过滤（可以不传）
     * method:get
     */
    @Override
    public String UnErollExList(String token, int size, long last, String name) throws Exception {
        final String url = StringPools.APP_SERVER+ "/rest/exhibitions/find";
        /*StringBuffer str = new StringBuffer(url);

        str.append("?");
        str.append("token="+token+"&");
        str.append("size="+size+"&");
        str.append("last="+last+"&");
        str.append("name="+name);*/
        Map<String,String> params = new HashMap<String, String>();
        params.put("token",token);
        params.put("size",String.valueOf(size));
        params.put("last",String.valueOf(last));
        params.put("name",name);

        try {
            String response = CustomerHttpClient.getRequest(url, params);
            return response;
        } catch (RuntimeException e) {
            // 请求失败或者连接失败
            System.out.println("新闻列表请求失败或者连接失败");
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 已报名展会列表
     * token 手机唯一标识码，用于过滤已报名展会(为服务器返回值)
     * method:get
     */
    @Override
    public String ErollExList(String token) throws Exception {
        final String url = StringPools.APP_SERVER + "/rest/exhibitions/find_applied";

        try {
//            String response = CustomerHttpClient.get(url,"token="+token);
//            return response;
        } catch (RuntimeException e) {
            // 请求失败或者连接失败
            System.out.println("新闻列表请求失败或者连接失败");
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 新闻列表
     * exKey 展会标识(为服务器返回值)
     * method:get
     */
    @Override
    public String ExNewsList(String exKey) throws Exception {
        final String url = StringPools.APP_SERVER + "/rest/news/find";

        try {
//            String response = CustomerHttpClient.get(url,"token="+exKey);
//            return response;
        } catch (RuntimeException e) {
            // 请求失败或者连接失败
            System.out.println("新闻列表请求失败或者连接失败");
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 展会报名
     * exKey	string	展会标识
     * token	string	手机标识
     * name	string	姓名
     * mobile	string	手机
     * email	string	邮箱
     * method:put
     */
    @Override
    public String ExEnroll(String exKey, String token, String name, String mobile, String email) throws Exception {
        return null;
    }

    /**
     * 签到
     */
    @Override
    public void checkIn(String serviceToken, String exhibitionCode, double latitude,
                        double longitude, String address) throws Exception {
        // 准备数据
        NameValuePair param1 = new BasicNameValuePair("serviceToken", serviceToken);
        NameValuePair param2 = new BasicNameValuePair("exhibitionCode", exhibitionCode);
        NameValuePair param3 = new BasicNameValuePair("latitude", latitude + "");
        NameValuePair param4 = new BasicNameValuePair("longitude", longitude + "");
        NameValuePair param5 = new BasicNameValuePair("address", address);

        try {
            System.out.println(param1 + ":" + param2 + ":" + param3 + ":" + param4 + ":" + param5);
            // 使用工具类直接发出POST请求
            String response = CustomerHttpClient.post("", param1, param2,
                    param3, param4, param5);

        } catch (RuntimeException e) {
            // 请求失败
            e.getMessage();
        } catch (Exception e) {

        }
    }

    public String registerService(String serviceToken, String exhibitionCode,
                                  String mobilePlatform) throws Exception {
//        ClientServiceToken mClientServiceToken = new ClientServiceToken();
//        mClientServiceToken.setServiceToken(serviceToken);
//        mClientServiceToken.setExhibitionCode(exhibitionCode);
//        mClientServiceToken.setMobilePlatform(mobilePlatform);
//        String jsonData = new Gson().toJson(mClientServiceToken);
//        try {
//            String response = CustomerHttpClient.postJson(AppConfig.URL_REGISTER, jsonData);
//            return response;
//        } catch (Exception e) {
//            Toast.makeText(
//                    controller.getCurrentActivity().getApplicationContext(),
//                    e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
        return null;
    }

    public String getNewsData() throws Exception {
        try {
//            String response = CustomerHttpClient.get("", "");
//            return response;
        } catch (Exception ignore) {
        }
        return null;
    }
}
