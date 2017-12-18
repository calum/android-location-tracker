package com.example.calum.utils;

import com.github.kevinsawicki.http.HttpRequest;

/**
 * Created by calum on 16/12/17.
 */

public class HTTP {

    private String url;

    public HTTP(String url) {
        this.url = url;
    }

    public HttpRequest post(String data) {
        HttpRequest request = HttpRequest.post(url);
        request.header("User-Agent", "Calum's App");

        //Accept all certificates
        request.trustAllCerts();
        //Accept all hostnames
        request.trustAllHosts();

        request.send(data);

        return request;
    }
}
