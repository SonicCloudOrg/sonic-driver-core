package org.cloud.sonic.core.tool;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class RespHandler {
    private static final int REQUEST_TIMEOUT = 5000;

    public static void getResp(HttpRequest httpRequest){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        String response = httpRequest.addHeaders(headers).timeout(REQUEST_TIMEOUT).execute().body();

    }

    public static void getResp(HttpRequest httpRequest){
        httpRequest.timeout(5000).execute();
    }
}
