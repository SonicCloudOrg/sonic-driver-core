package org.cloud.sonic.core.ios;

import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.core.ios.models.BaseResp;
import org.cloud.sonic.core.ios.models.ErrorMsg;
import org.cloud.sonic.core.tool.SonicRespException;

import java.util.HashMap;
import java.util.Map;

public class RespHandler {
    private static final int REQUEST_TIMEOUT = 5000;

    public static BaseResp getResp(HttpRequest httpRequest) throws SonicRespException {
        return getResp(httpRequest, REQUEST_TIMEOUT);
    }

    public static BaseResp getResp(HttpRequest httpRequest, int timeout) throws SonicRespException {
        synchronized (RespHandler.class) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                return initResp(httpRequest.addHeaders(initHeader()).timeout(timeout).execute().body());
            } catch (HttpException e) {
                throw new SonicRespException(e.getMessage());
            }
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

    public static BaseResp initErrorMsg(String resp) {
        BaseResp err = JSON.parseObject(resp, BaseResp.class);
        ErrorMsg errorMsg = JSONObject.parseObject(err.getValue().toString(), ErrorMsg.class);
        err.setErr(errorMsg);
        err.setValue(null);
        return err;
    }
}
