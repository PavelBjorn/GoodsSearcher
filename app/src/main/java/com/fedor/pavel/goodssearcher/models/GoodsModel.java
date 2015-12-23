package com.fedor.pavel.goodssearcher.models;


import android.content.ContentValues;

import com.fedor.pavel.goodssearcher.constants.APIQueryConstants;
import com.fedor.pavel.goodssearcher.data.SQLHelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GoodsModel {


    private long id;

    @Expose
    @SerializedName(APIQueryConstants.API_GOODS_ID_KEY)
    private long serverId;


    @Expose
    @SerializedName(APIQueryConstants.API_CATEGORY_ID_KEY)
    private long categoryId;

    @Expose
    @SerializedName(APIQueryConstants.API_GOODS_TITLE_KEY)
    private String title;


    @Expose
    @SerializedName(APIQueryConstants.API_GOODS_CURRENCY_CODE_KEY)
    private String currenciesCode;

    @Expose
    @SerializedName(APIQueryConstants.API_GOODS_PRICE_KEY)
    private double price;

    @Expose
    @SerializedName(APIQueryConstants.API_GOODS_DESCRIPTION_KEY)
    private String description;

    @Expose
    @SerializedName(APIQueryConstants.API_GOODS_PHOTO_KEY)
    private ArrayList<PhotoUrlModel> photosUri = new ArrayList<>();

    public GoodsModel() {
    }

    public GoodsModel(long id, long serverId, long categoryId, String title, String currenciesCode, double price, String description, ArrayList<PhotoUrlModel> photosUri) {
        this.id = id;
        this.serverId = serverId;
        this.categoryId = categoryId;
        setTitle(title);
        this.currenciesCode = currenciesCode;
        this.price = price;
        setDescription(description);
        this.photosUri.addAll(photosUri);
    }

    public GoodsModel(long serverId, long categoryId, String title, String currenciesCode, double price, String description) {
        this.serverId = serverId;
        this.categoryId = categoryId;
        setTitle(title);
        this.currenciesCode = currenciesCode;
        setDescription(description);
        this.description = description;
        this.photosUri.addAll(photosUri);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {

        if (title == null) {
            return "...";
        }

        return title.replace("&quot;", "\"");

    }

    public void setTitle(String title) {

        if (title == null) {

            title = "";

        }

        this.title = title;
    }

    public String getCurrenciesCode() {
        return currenciesCode;
    }

    public void setCurrenciesCode(String currenciesCode) {
        this.currenciesCode = currenciesCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {

        if (description == null) {
            return "";
        }

        return description.replace("&quot;", "\"");
    }

    public void setDescription(String description) {

        if (description == null) {
            description = "";
        }

        this.description = description;
    }

    public PhotoUrlModel getPhotoUri(int position) {


        return photosUri.get(position);


    }

    public void addPhotoUri(PhotoUrlModel photoUri) {

        photosUri.add(photoUri);

    }

    public int getNumPhoto() {

        return photosUri.size();
    }

    public ContentValues getContentValues() {

        ContentValues contentValues = new ContentValues();

        contentValues.put(SQLHelper.TABLE_GOODS_SERVER_ID_COLUMN, serverId);

        contentValues.put(SQLHelper.TABLE_GOODS_CATEGORY_ID_COLUMN, categoryId);

        contentValues.put(SQLHelper.TABLE_GOODS_TITLE_COLUMN, title);

        contentValues.put(SQLHelper.TABLE_GOODS_CURRENCY_CODE_COLUMN, currenciesCode);

        contentValues.put(SQLHelper.TABLE_GOODS_PRICE_COLUMN, price);

        contentValues.put(SQLHelper.TABLE_GOODS_DESCRIPTION_COLUMN, description);

        return contentValues;

    }
}
