package com.threeH.MyExhibition.entities;

import java.io.Serializable;

/**
 * 应用启动时需要读取全局配置信息
 */
public class OverAllConfig implements Serializable {
    /**资源服务器地址*/
    private String assetServer;
    /**报名电话*/
    private String tel;
    /**服务端生成token*/
    private String token;
    /**Android应用自动更新，为空时表示不需要更新*/
    private String upgrade;
    /**Android应用更新说明*/
    private String upgradeNote;

    public String getAssetServer() {
        return assetServer;
    }

    public void setAssetServer(String assetServer) {
        this.assetServer = assetServer;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(String upgrade) {
        this.upgrade = upgrade;
    }

    public String getUpgradeNote() {
        return upgradeNote;
    }

    public void setUpgradeNote(String upgradeNote) {
        this.upgradeNote = upgradeNote;
    }
}
