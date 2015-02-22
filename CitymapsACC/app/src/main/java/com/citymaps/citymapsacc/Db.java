package com.citymaps.citymapsacc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

class Db {
    private static DbHelper _dbHelper;

    public Db(Context c) {
        if (_dbHelper == null) {
            _dbHelper = new DbHelper(c);
        }
    }

    private static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        } else {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 0, outputStream);
            return outputStream.toByteArray();
        }
    }

    private static Bitmap getBitmapFromByteArray(byte[] imgByte) {
        if (imgByte == null) return null;
        return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
    }

    public void addImageBitmap(int index, Bitmap image) {
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        if (db != null && image != null) {
            ContentValues values = new ContentValues();
            values.put(_dbHelper.COLUMN_NAME_INDEX, index);
            values.put(_dbHelper.COLUMN_NAME_BITMAP, getBitmapAsByteArray(image));
            db.replace(_dbHelper.TABLE_NAME_IMAGES, "", values);
        }
    }

    public Bitmap getImageBitmap(int index) {
        Bitmap ret = null;
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery(
                    "select * from " + _dbHelper.TABLE_NAME_IMAGES
                            + " where " + _dbHelper.COLUMN_NAME_INDEX + "=" + index, null
            );
            if (c.getCount() > 0) {
                c.moveToFirst();
                ret = getBitmapFromByteArray(c.getBlob(c
                        .getColumnIndex(_dbHelper.COLUMN_NAME_BITMAP)));
            }
        }
        return ret;
    }

    public void clearCache() {
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        if (db != null) {
            db.delete(_dbHelper.TABLE_NAME_IMAGES, "", null);
        }
    }

}
