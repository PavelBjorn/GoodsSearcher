package com.fedor.pavel.goodssearcher.models;


import com.fedor.pavel.goodssearcher.constants.APIQueryConstants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryModel {


    private long id;

    @Expose
    @SerializedName(APIQueryConstants.API_CATEGORY_ID_KEY)
    private long serverId;

    @Expose
    @SerializedName(APIQueryConstants.API_CATEGORY_SEARCH_NAME_KEY)
    private String requestName;


    @Expose
    @SerializedName(APIQueryConstants.API_CATEGORY_LONG_NAME_KEY)
    private String longName;


    public CategoryModel() {
    }

    public CategoryModel(long serverId, String requestName, String longName) {
        this.serverId = serverId;
        this.requestName = requestName;
        this.longName = longName;
    }

    public CategoryModel(long id, long serverId, String requestName, String longName) {
        this.id = id;
        this.serverId = serverId;
        this.requestName = requestName;
        this.longName = longName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public long getId() {
        return id;
    }

    public long getServerId() {
        return serverId;
    }

    public String getRequestName() {
        return requestName;
    }

    public String getLongName() {
        return longName;
    }
}
