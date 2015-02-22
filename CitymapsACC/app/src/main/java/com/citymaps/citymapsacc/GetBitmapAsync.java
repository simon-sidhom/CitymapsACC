package com.citymaps.citymapsacc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

class GetBitmapAsync extends AsyncTask<GetBitmapAsync.Params, Void, Bitmap> {
    private final OnBitmapRetrievedListener onBitmapRetrievedListener;
    private final Db bitmapCacheDb;

    public GetBitmapAsync(OnBitmapRetrievedListener onBitmapRetrievedListener, Context context) {
        this.onBitmapRetrievedListener = onBitmapRetrievedListener;
        bitmapCacheDb = new Db(context);
    }

    @Override
    protected Bitmap doInBackground(Params... params) {
        try {
            if (params != null && params.length > 0 && params[0] != null) {
                Params param = params[0];

                Bitmap bitmap = bitmapCacheDb.getImageBitmap(param.getIndex());
                if (bitmap == null) {
                    URL url = new URL(param.getUrl());
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    bitmapCacheDb.addImageBitmap(param.getIndex(), bitmap);
                }
                return bitmap;
            }
        } catch (MalformedURLException e) {
            Log.e(Constants.LOG_TAG, "exception while parsing url", e);
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "exception while decoding input stream", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        onBitmapRetrievedListener.onBitmapRetrieved(bitmap);
    }

    public interface OnBitmapRetrievedListener {
        public void onBitmapRetrieved(Bitmap bitmap);
    }

    public static class Params {
        private final int index;
        private final String url;

        public Params(int index, String url) {
            this.index = index;
            this.url = url;
        }

        public int getIndex() {
            return index;
        }

        public String getUrl() {
            return url;
        }

    }
}
