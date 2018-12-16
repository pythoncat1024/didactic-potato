package com.python.cat.potato.domain;

import java.util.List;

/**
 * user/login http resp body
 */
public class LoginResult {

    /**
     * data : {"chapterTops":[],"collectIds":[2864,3016,7418],"email":"","icon":"","id":6649,"password":"","token":"","type":0,"username":"pythoncat"}
     * errorCode : 0
     * errorMsg :
     */

    public DataBean data;
    public int errorCode;
    public String errorMsg;

    public static class DataBean {
        /**
         * chapterTops : []
         * collectIds : [2864,3016,7418]
         * email :
         * icon :
         * id : 6649
         * password :
         * token :
         * type : 0
         * username : pythoncat
         */

        public String email;
        public String icon;
        public int id;
        public String password;
        public String token;
        public int type;
        public String username;
        public List<?> chapterTops;
        public List<Integer> collectIds;
    }

    @Override
    public String toString() {
        return "LoginCallback{" +
                "data=" + data +
                ", errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
