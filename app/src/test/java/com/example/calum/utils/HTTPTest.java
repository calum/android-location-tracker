package com.example.calum.utils;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by calum on 17/12/17.
 */

public class HTTPTest {
    @Test
    public void http_can_post() throws Exception {
        String data = new JSONObject()
                .put("JSON", "Hello, World!").toString();

        HTTP http = new HTTP("https://requestb.in/144b2np1");

        http.post(data);
    }
}
