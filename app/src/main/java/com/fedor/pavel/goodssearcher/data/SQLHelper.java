package com.fedor.pavel.goodssearcher.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLHelper extends SQLiteOpenHelper {

    public static SQLHelper instance;

    /*Data base properties*/
    public static final String DATA_BASE_NAME = "goods_searcher.db";

    public static final int DATA_BASE_VERSION = 1;

    /*Tables*/

    /*Goods table*/

    public static final String TABLE_NAME_GOODS = "goods";

    public static final String TABLE_GOODS_ID_COLUMN = "_id";

    public static final String TABLE_GOODS_SERVER_ID_COLUMN = "goods_server_id";

    public static final String TABLE_GOODS_CATEGORY_ID_COLUMN = "goods_category_id";

    public static final String TABLE_GOODS_TITLE_COLUMN = "goods_title";

    public static final String TABLE_GOODS_CURRENCY_CODE_COLUMN = "goods_currency_code";

    public static final String TABLE_GOODS_PRICE_COLUMN = "goods_price";

    public static final String TABLE_GOODS_DESCRIPTION_COLUMN = "goods_description";

    /*Goods photo table*/

    public static final String TABLE_NAME_GOODS_PHOTO = "goods_photo";

    public static final String TABLE_GOODS_PHOTO_ID_COLUMN = "_id";

    public static final String TABLE_GOODS_PHOTO_SERVER_ID_COLUMN = "goods_id";

    public static final String TABLE_GOODS_PHOTO_URL_170x135_COLUMN = "url_small";

    public static final String TABLE_GOODS_PHOTO_URL_500xN_COLUMN = "url_big";

    /*Commands*/

    private static final String CREATE_TABLE_GOODS = "CREATE TABLE " + TABLE_NAME_GOODS + " ("

            + TABLE_GOODS_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "

            + TABLE_GOODS_SERVER_ID_COLUMN + " INTEGER, "

            + TABLE_GOODS_CATEGORY_ID_COLUMN + " INTEGER, "

            + TABLE_GOODS_TITLE_COLUMN + " TEXT, "

            + TABLE_GOODS_CURRENCY_CODE_COLUMN + " TEXT, "

            + TABLE_GOODS_PRICE_COLUMN + " REAL, "

            + TABLE_GOODS_DESCRIPTION_COLUMN + " TEXT"

            + ");";

    private static final String CREATE_TABLE_GOODS_PHOTO = "CREATE TABLE " + TABLE_NAME_GOODS_PHOTO + " ("

            + TABLE_GOODS_PHOTO_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "

            + TABLE_GOODS_PHOTO_SERVER_ID_COLUMN + " INTEGER, "

            + TABLE_GOODS_PHOTO_URL_170x135_COLUMN + " TEXT, "

            + TABLE_GOODS_PHOTO_URL_500xN_COLUMN + " TEXT"

            + ");";

    private static final String DROP_TABLE = "DROP TABLE ";

    private SQLHelper(Context context) {

        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);


    }

    public static SQLHelper getInstance(Context context) {

        if (instance == null) {
            instance = new SQLHelper(context);
        }

        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_GOODS);
        db.execSQL(CREATE_TABLE_GOODS_PHOTO);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_TABLE + TABLE_NAME_GOODS);

        db.execSQL(DROP_TABLE + TABLE_NAME_GOODS_PHOTO);

        onCreate(db);

    }
}
