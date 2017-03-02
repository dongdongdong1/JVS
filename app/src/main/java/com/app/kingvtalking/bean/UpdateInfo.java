package com.app.kingvtalking.bean;

/**
 * Created by wang55 on 2017/1/12.
 */

public class UpdateInfo {

    /**
     * latest : {"version":"1.0.3","version_desc":"203版本","download_url":"http://cdn.jvtalking.com/app_360.apk.1"}
     * force : 2
     * code : 1
     * message : 无效版本号
     */

    private LatestBean latest;
    private int force;
    private int code;
    private String message;

    public LatestBean getLatest() {
        return latest;
    }

    public void setLatest(LatestBean latest) {
        this.latest = latest;
    }

    public int getForce() {
        return force;
    }

    public void setForce(int force) {
        this.force = force;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class LatestBean {
        /**
         * version : 1.0.3
         * version_desc : 203版本
         * download_url : http://cdn.jvtalking.com/app_360.apk.1
         */

        private String version;
        private String version_desc;
        private String download_url;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getVersion_desc() {
            return version_desc;
        }

        public void setVersion_desc(String version_desc) {
            this.version_desc = version_desc;
        }

        public String getDownload_url() {
            return download_url;
        }

        public void setDownload_url(String download_url) {
            this.download_url = download_url;
        }
    }
}
