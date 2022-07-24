package org.cloud.sonic.core.ios;

import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.core.ios.models.BaseResp;
import org.cloud.sonic.core.ios.models.ErrorMsg;
import org.cloud.sonic.core.ios.service.WdaClient;
import org.cloud.sonic.core.tool.SonicRespException;

import java.util.HashMap;
import java.util.Map;

public class RespHandler {
    public static final int DEFAULT_REQUEST_TIMEOUT = 15000;
    private int requestTimeout = 15000;

    public void setRequestTimeOut(int timeOut) {
        requestTimeout = timeOut;
    }

    public BaseResp getResp(HttpRequest httpRequest) throws SonicRespException {
        return getResp(httpRequest, requestTimeout);
    }

    public BaseResp getResp(HttpRequest httpRequest, int timeout) throws SonicRespException {
        synchronized (WdaClient.class) {
            try {
                return initResp(httpRequest.addHeaders(initHeader()).timeout(timeout).execute().body());
            } catch (HttpException e) {
                throw new SonicRespException(e.getMessage());
            }
        }
    }

    public BaseResp initResp(String response) {
        if (response.contains("traceback")) {
            return initErrorMsg(response);
        } else {
            return JSON.parseObject(response, BaseResp.class);
        }
    }

    public Map<String, String> initHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        return headers;
    }

    public BaseResp initErrorMsg(String resp) {
        BaseResp err = JSON.parseObject(resp, BaseResp.class);
        ErrorMsg errorMsg = JSONObject.parseObject(err.getValue().toString(), ErrorMsg.class);
        err.setErr(errorMsg);
        err.setValue(null);
        return err;
    }
}
