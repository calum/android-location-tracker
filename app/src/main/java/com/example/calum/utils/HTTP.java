package com.example.calum.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONObject;

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

    public class Post extends AsyncTask<String, Void, HttpRequest> {

        protected HttpRequest doInBackground(String... data) {
            Log.d(TAG, "Posting data: " +data[0]);

            HttpRequest request = HttpRequest.post(url);
            request.header("User-Agent", "Calum's App");

            //Accept all certificates
            request.trustAllCerts();
            //Accept all hostnames
            request.trustAllHosts();

            request.send(data[0]);

            return request;
        }

        protected void onPostExecute(HttpRequest result) {
            Log.d(TAG,"HTTP POST Response: " + result.body());
        }

    }
}
