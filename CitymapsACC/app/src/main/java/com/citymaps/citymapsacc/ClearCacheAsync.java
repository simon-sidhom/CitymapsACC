package com.citymaps.citymapsacc;

import android.content.Context;
import android.os.AsyncTask;

class ClearCacheAsync extends AsyncTask<Void, Void, Void> {
    private final Context context;

    public ClearCacheAsync(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Db db = new Db(context);
        db.clearCache();
        return null;
    }
}
