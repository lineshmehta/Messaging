package com.telenor.cos.messaging;

/**
 * Define users for integration tests here.
 */
public enum User {

    SYSTEM_USER(){
        public String getUsername(){ return "system";}
        public String getPassword(){ return "manager";}
    },

    OTA(){
        public String getUsername(){ return "ota";}
        public String getPassword(){ return "ota";}
    };

    /**
     * @return username for this user
     */
    public abstract String getUsername();

    /**
     * @return password for this user
     */
    public abstract String getPassword();
}
