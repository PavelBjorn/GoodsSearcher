package com.fedor.pavel.goodssearcher.models;

import android.content.ContentValues;

import com.fedor.pavel.goodssearcher.constants.APIQueryConstants;
import com.fedor.pavel.goodssearcher.data.SQLHelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PhotoUrlModel {

    private long id;

    @Expose
    @SerializedName(APIQueryConstants.API_GOODS_PHOTO_170x135_KEY)
    private String photoUrl170x135;

    @Expose
    @SerializedName(APIQueryConstants.API_GOODS_PHOTO_500xN_KEY)
    private String photoUrl500xN;


    public PhotoUrlModel() {

    }

    public PhotoUrlModel(long id, String photoUrl170x135, String photoUrl500xN) {

        this.photoUrl170x135 = photoUrl170x135;
        this.photoUrl500xN = photoUrl500xN;
        this.id = id;

    }

    public PhotoUrlModel(String photoUrl170x135, String photoUrl500xN) {

        this.photoUrl170x135 = photoUrl170x135;
        this.photoUrl500xN = photoUrl500xN;
    }

    public String getPhotoUrl170x135() {
        return photoUrl170x135;
    }

    public void setPhotoUrl170x135(String photoUrl170x135) {
        this.photoUrl170x135 = photoUrl170x135;
    }

    public String getPhotoUrl500xN() {
        return photoUrl500xN;
    }

    public void setPhotoUrl500xN(String photoUrl500xN) {
        this.photoUrl500xN = photoUrl500xN;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ContentValues getContentValues(long goodsServerId) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(SQLHelper.TABLE_GOODS_PHOTO_URL_170x135_COLUMN, photoUrl170x135);

        contentValues.put(SQLHelper.TABLE_GOODS_PHOTO_URL_500xN_COLUMN, photoUrl500xN);

        contentValues.put(SQLHelper.TABLE_GOODS_PHOTO_SERVER_ID_COLUMN, goodsServerId);

        return contentValues;

    }
}
