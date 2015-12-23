package com.fedor.pavel.goodssearcher.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fedor.pavel.goodssearcher.models.GoodsModel;
import com.fedor.pavel.goodssearcher.models.PhotoUrlModel;

import java.util.ArrayList;


public class SQLManager {

    private static SQLManager instance;
    private SQLHelper helper;
    private SQLiteDatabase sqLiteDatabase;
    private static Context context;


    private SQLManager(Context context) {
        helper = SQLHelper.getInstance(context);
    }


    public static SQLManager initialized(Context context) {

        if (instance == null) {
            SQLManager.context = context;
            instance = new SQLManager(context);
        }

        return instance;
    }

    public static SQLManager getInstance() {

        return instance;

    }

    public void open() {

        this.sqLiteDatabase = helper.getWritableDatabase();

    }

    public void close() {
        if (helper != null) {
            this.sqLiteDatabase.close();
        }
    }

    public long insertModel(String tableName, ContentValues values) {
        return sqLiteDatabase.insert(tableName, null, values);
    }

    public ArrayList<GoodsModel> getGoodsModel() {

        Cursor cursor = sqLiteDatabase.query(SQLHelper.TABLE_NAME_GOODS, null, null, null, null, null, null);

        ArrayList<GoodsModel> goods = new ArrayList<>();

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            long serverId = cursor.getLong(cursor.getColumnIndex(SQLHelper.TABLE_GOODS_SERVER_ID_COLUMN));

            goods.add(new GoodsModel(cursor.getLong(cursor.getColumnIndex(SQLHelper.TABLE_GOODS_ID_COLUMN))
                    , serverId
                    , cursor.getLong(cursor.getColumnIndex(SQLHelper.TABLE_GOODS_CATEGORY_ID_COLUMN))
                    , cursor.getString(cursor.getColumnIndex(SQLHelper.TABLE_GOODS_TITLE_COLUMN))
                    , cursor.getString(cursor.getColumnIndex(SQLHelper.TABLE_GOODS_CURRENCY_CODE_COLUMN))
                    , cursor.getDouble(cursor.getColumnIndex(SQLHelper.TABLE_GOODS_PRICE_COLUMN))
                    , cursor.getString(cursor.getColumnIndex(SQLHelper.TABLE_GOODS_DESCRIPTION_COLUMN))
                    , getPhoto(serverId)
            ));

            cursor.moveToNext();

        }

        return goods;

    }

    public ArrayList<PhotoUrlModel> getPhoto(long goodsServerId) {

        String[] columns = {SQLHelper.TABLE_GOODS_PHOTO_URL_170x135_COLUMN
                , SQLHelper.TABLE_GOODS_PHOTO_URL_500xN_COLUMN};


        Cursor cursor = sqLiteDatabase.query(SQLHelper.TABLE_NAME_GOODS_PHOTO
                , columns, SQLHelper.TABLE_GOODS_PHOTO_SERVER_ID_COLUMN + "=" + "\'" + goodsServerId + "\'",
                null, null, null, null);

        ArrayList<PhotoUrlModel> photoUrlModels = new ArrayList<>();

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            photoUrlModels.add(new PhotoUrlModel(goodsServerId
                    , cursor.getString(cursor.getColumnIndex(columns[0]))
                    , cursor.getString(cursor.getColumnIndex(columns[1]))
            ));

            cursor.moveToNext();
        }


        return photoUrlModels;
    }

    public void deleteGoodsModel(GoodsModel model) {

        sqLiteDatabase.delete(SQLHelper.TABLE_NAME_GOODS, SQLHelper.TABLE_GOODS_SERVER_ID_COLUMN + "=" + "\'" + model.getServerId() + "\'", null);

        sqLiteDatabase.delete(SQLHelper.TABLE_NAME_GOODS_PHOTO, SQLHelper.TABLE_GOODS_PHOTO_SERVER_ID_COLUMN + "=" + "\'" + model.getServerId() + "\'", null);

    }

    public Boolean checkDbForObject(GoodsModel item) {

        Cursor cursor = sqLiteDatabase.query(SQLHelper.TABLE_NAME_GOODS,
                new String[]{SQLHelper.TABLE_GOODS_ID_COLUMN}
                , SQLHelper.TABLE_GOODS_SERVER_ID_COLUMN + "=\'" + item.getServerId() + "\'"
                , null, null, null, null);

        if (cursor.getCount()==0){
            return false;
        }else {
            return true;
        }

    }
}
