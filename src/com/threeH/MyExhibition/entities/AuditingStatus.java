package com.threeH.MyExhibition.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 报名审核状态
 */
public class AuditingStatus implements Serializable {
    /**展会标识*/
    private String exKey;
    /**审核状态，
     * N 未报名(Not Applied)，P 审核中，A 审核通过，D 未通过
     */
    private String status;
    /**审核记录*/
    private ArrayList<String> logs = new ArrayList<String>();

    public String getExKey() {
        return exKey;
    }

    public void setExKey(String exKey) {
        this.exKey = exKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getLogs() {
        return logs;
    }

    public void setLogs(ArrayList<String> logs) {
        this.logs = logs;
    }
}
