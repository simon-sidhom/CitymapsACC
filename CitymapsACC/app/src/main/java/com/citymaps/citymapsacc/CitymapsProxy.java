package com.citymaps.citymapsacc;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

class CitymapsProxy {
    private final Context context;

    public CitymapsProxy(Context context) {
        this.context = context;
    }

    public ArrayList<CitymapsPhoto> getCitymapsPhotos(Integer startIndex) throws JSONException {
        String userId = "8ea239c4-c648-4009-a252-a220e018dc4b";
        int limit = 20;
        String url = String.format(Locale.US,
                "http://ndev-coreapi.citymaps.com/v2/activity/user/%1$s/images?offset=%2$d&limit=%3$d",
                userId, startIndex, limit);
        HttpResult httpResult = getHTTP(url);
        if (httpResult.getStatusCode() < 300) {
            return parseCitymapsPhotos(httpResult.getBody());
        }
        return null;
    }

    ArrayList<CitymapsPhoto> parseCitymapsPhotos(String jsonString) throws JSONException {
        ArrayList<CitymapsPhoto> ret = new ArrayList<>();
        String activitiesKey = "activities";
        String imageUrlKey = "image_url";
        JSONObject json = new JSONObject(jsonString);
        if (json.has(activitiesKey)) {
            JSONArray activities = json.getJSONArray(activitiesKey);
            if (activities != null) {
                for (int i = 0; i < activities.length(); i++) {
                    JSONObject activity = activities.getJSONObject(i);
                    if (activity.has(imageUrlKey)) {
                        JSONArray imageUrls = activity.getJSONArray(imageUrlKey);
                        for (int j = 0; j < imageUrls.length(); j++) {
                            ret.add(new CitymapsPhoto(imageUrls.getString(j)));
                        }
                    }
                }
            }
        }
        return ret;
    }

    HttpResult getHTTP(final String url) {
        HttpResult response = null;
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() && url != null) {
            try {
                URL urlObj = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setInstanceFollowRedirects(true);
                urlConnection.setUseCaches(false);
                int status = urlConnection.getResponseCode();
                boolean redirect = (status == HttpURLConnection.HTTP_MOVED_TEMP ||
                        status == HttpURLConnection.HTTP_MOVED_PERM ||
                        status == HttpURLConnection.HTTP_SEE_OTHER);
                if (redirect) {
                    // get redirect url from "location" header field
                    String newUrl = urlConnection.getHeaderField("Location");
                    return getHTTP(newUrl);
                }
                String result = "";
                if (status < 300) {
                    BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    result = convertStreamToString(in);
                }
                response = new HttpResult(status, result);
            } catch (IOException e) {
                // connection timeout with server
                response = new HttpResult(HttpURLConnection.HTTP_GATEWAY_TIMEOUT, null);
            }

        }
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "exception while reading input stream", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(Constants.LOG_TAG, "exception while closing input stream", e);
            }
        }
        return sb.toString();
    }
}
