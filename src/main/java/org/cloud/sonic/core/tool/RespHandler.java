package org.cloud.sonic.core.tool;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import org.cloud.sonic.core.ios.models.BaseResp;
import org.cloud.sonic.core.ios.models.ErrorMsg;

import java.util.HashMap;
import java.util.Map;

public class RespHandler {
    private static final int REQUEST_TIMEOUT = 5000;

    public static BaseResp getResp(HttpRequest httpRequest) {
        return getResp(httpRequest, REQUEST_TIMEOUT);
    }

    public static BaseResp getResp(HttpRequest httpRequest, int timeout) {
        synchronized (RespHandler.class) {
            return initResp(httpRequest.addHeaders(initHeader()).timeout(timeout).execute().body());
        }
    }

    public static BaseResp initResp(String response) {
        if (response.contains("traceback")) {
            return initErrorMsg(response);
        } else {
            return JSON.parseObject(response, BaseResp.class);
        }
    }

    public static Map<String, String> initHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        return headers;
    }

    public static BaseResp<ErrorMsg> initErrorMsg(String resp) {
        BaseResp<ErrorMsg> err = JSON.parseObject(resp, BaseResp.class);
        return new BaseResp(err);
    }
}
