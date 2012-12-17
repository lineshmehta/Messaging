package com.telenor.cos.messaging.web.form;

public class CacheForm {

    private String id;
    private String cacheType;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCacheType() {
        return cacheType;
    }
    public void setCacheType(String cacheType) {
        this.cacheType = cacheType;
    }
    @Override
    public String toString() {
        return "CacheForm [id=" + id + ", cacheType=" + cacheType + "]";
    }
}
