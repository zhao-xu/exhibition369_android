package com.threeH.MyExhibition.service;

public interface ClientService {
    /**
     * 初始化App信息
     *
     * @return
     * @throws Exception
     */
    public String findAll() throws Exception;

    /**
     * 全局数据获取
     * @return
     * @throws Exception
     */
    public String OverAllData(String type,String osVer,String ver,String token) throws Exception;

    /**
     * 未报名展会列表
     * @return
     * @throws Exception
     */
    public String UnErollExList(String token,int size,long last,String name) throws Exception;

    /**
     * 已报名展会列表
     * @return
     * @throws Exception
     */
    public String ErollExList(String token) throws Exception;

    /**
     * 新闻列表
     * @return
     * @throws Exception
     */
    public String ExNewsList(String exKey) throws Exception;

    /**
     * 展会报名
     * @return
     * @throws Exception
     */
    public String ExEnroll(String exKey,String token,String name,String mobile,String email) throws Exception;

    /**
     * 签到
     *
     * @throws Exception
     */
    public void checkIn(String serviceToken, String exhibitionCode, double latitude,
                        double longitude, String address) throws Exception;

    /**
     * 注册serviceToken
     *
     * @return
     * @throws Exception
     */
    public String registerService(String serviceToken, String exhibitionCode, String mobilePlatform) throws Exception;
    
    /**
     * 获得新闻数据
     * @return 
     * @throws Exception
     */
    public String getNewsData() throws Exception;

}
