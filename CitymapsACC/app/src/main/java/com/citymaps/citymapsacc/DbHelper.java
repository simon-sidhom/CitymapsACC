package com.citymaps.citymapsacc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DbHelper extends SQLiteOpenHelper {
    //NOTE: Any changes to this file need to be accompanied by an increment of the version counter
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "imageCache.db";
    public final String TABLE_NAME_IMAGES = "images";
    public final String COLUMN_NAME_INDEX = "imageIndex";
    public final String COLUMN_NAME_BITMAP = "imageBitmap";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDb) {
        String sql = "CREATE TABLE " + TABLE_NAME_IMAGES + " (" + COLUMN_NAME_INDEX
                + " INTEGER PRIMARY KEY, " + COLUMN_NAME_BITMAP + " BLOB);";
        sqliteDb.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDb, int oldVersion,
                          int newVersion) {
        Log.i(DbHelper.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        sqliteDb.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_IMAGES);
        onCreate(sqliteDb);
    }
}