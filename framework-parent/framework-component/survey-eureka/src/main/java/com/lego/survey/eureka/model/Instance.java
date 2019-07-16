package com.lego.survey.eureka.model;

/**
 * @author yanglf
 * @description
 * @since 2019/7/16
 **/
public class Instance {

    /**
     * instance : {"instanceId":"192.168.101.103:48060","hostName":"192.168.101.103","app":"AUTH-SERVICE","ipAddr":"192.168.101.103","status":"UP","overriddenStatus":"UNKNOWN"}
     */

    private InstanceBean instance;

    public InstanceBean getInstance() {
        return instance;
    }

    public void setInstance(InstanceBean instance) {
        this.instance = instance;
    }

    public static class InstanceBean {
        /**
         * instanceId : 192.168.101.103:48060
         * hostName : 192.168.101.103
         * app : AUTH-SERVICE
         * ipAddr : 192.168.101.103
         * status : UP
         * overriddenStatus : UNKNOWN
         */

        private String instanceId;
        private String hostName;
        private String app;
        private String ipAddr;
        private String status;
        private String overriddenStatus;

        public String getInstanceId() {
            return instanceId;
        }

        public void setInstanceId(String instanceId) {
            this.instanceId = instanceId;
        }

        public String getHostName() {
            return hostName;
        }

        public void setHostName(String hostName) {
            this.hostName = hostName;
        }

        public String getApp() {
            return app;
        }

        public void setApp(String app) {
            this.app = app;
        }

        public String getIpAddr() {
            return ipAddr;
        }

        public void setIpAddr(String ipAddr) {
            this.ipAddr = ipAddr;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOverriddenStatus() {
            return overriddenStatus;
        }

        public void setOverriddenStatus(String overriddenStatus) {
            this.overriddenStatus = overriddenStatus;
        }
    }
}
