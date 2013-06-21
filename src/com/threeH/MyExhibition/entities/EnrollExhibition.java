package com.threeH.MyExhibition.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 已报名展会列表
 */
public class EnrollExhibition implements Serializable {

    private EnrollStatus[] enrollStatuses;
    public EnrollStatus[] getEnrollStatuses() {
        return enrollStatuses;
    }

    public void setEnrollStatuses(EnrollStatus[] enrollStatuses) {
        this.enrollStatuses = enrollStatuses;
    }
    /*private ArrayList<EnrollStatus> list = new ArrayList<EnrollStatus>();

    public ArrayList<EnrollStatus> getList() {
        return list;
    }

    public void setList(ArrayList<EnrollStatus> list) {
        this.list = list;
    }*/

    public class EnrollStatus implements Serializable {
        /**
         * 展会标识
         */
        private String exKey;
        /**
         * 展会名称
         */
        private String name;
        /**
         * 审核状态:
         * P 审核中(Processing)，A 审核通过(Approved)，D 审核未通过(Denied)
         */
        private String status;

        public String getExKey() {
            return exKey;
        }

        void setExKey(String exKey) {
            this.exKey = exKey;
        }

        public String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        void setStatus(String status) {
            this.status = status;
        }
    }
}
