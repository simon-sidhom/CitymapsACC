package com.citymaps.citymapsacc;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * AsyncTask to get an array of Constants.IMAGE_LOAD_LIMIT CitymapsPhotos starting from the supplied index
 */
class GetPhotosAsync extends AsyncTask<Integer, Void, ArrayList<CitymapsPhoto>> {
    private final OnPhotosReceivedListener onPhotosReceivedListener;
    private final Context context;
    private Exception exception;

    public GetPhotosAsync(OnPhotosReceivedListener onPhotosReceivedListener, Context context) {
        this.onPhotosReceivedListener = onPhotosReceivedListener;
        this.context = context;
        this.exception = null;
    }

    @Override
    protected ArrayList<CitymapsPhoto> doInBackground(Integer... params) {
        CitymapsProxy proxy = new CitymapsProxy(context);
        if (params != null && params[0] != null) {
            try {
                return proxy.getCitymapsPhotos(params[0]);
            } catch (JSONException ex) {
                Log.e(Constants.LOG_TAG, "exception while parsing JSON", ex);
                this.exception = ex;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<CitymapsPhoto> photos) {
        super.onPostExecute(photos);
        if (exception == null) {
            onPhotosReceivedListener.onPhotosReceived(photos);
        } else {
            Toast.makeText(context, context.getString(R.string.prompt_problem_getting_photos)
                    , Toast.LENGTH_LONG).show();
        }
    }

    public interface OnPhotosReceivedListener {
        public void onPhotosReceived(ArrayList<CitymapsPhoto> photos);
    }
}
