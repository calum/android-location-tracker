package com.example.calum.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;

/**
 * Created by calum on 16/12/17.
 */

public class HTTP {

    private String TAG = "HTTP Class";

    private String url;

    private String data;

    public HTTP(String url) {
        this.url = url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setData(String data) {
        this.data = data;
    }
    public String getData( ) {
        return data;
    }

    public class Post extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... data) {
            Log.d(TAG, "Posting data: " +data[0]);

            HttpRequest request = HttpRequest.post(url);
            request.header("User-Agent", "Tracker App");

            //Accept all certificates
            request.trustAllCerts();
            //Accept all hostnames
            request.trustAllHosts();

            request.send(data[0]);

            return request.body();
        }

        protected void onPostExecute(String result) {
            Log.d(TAG,"HTTP POST Response: " + result);
        }

    }
}
