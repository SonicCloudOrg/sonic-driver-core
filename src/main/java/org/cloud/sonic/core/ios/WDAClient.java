package org.cloud.sonic.core.ios;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WDAClient {
    private String remoteUrl;
    private String sessionId;

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void newSession() {
        JSONObject data = new JSONObject();
        data.put("capabilities", new JSONObject());
        HttpRequest request = HttpUtil.createPost(remoteUrl + "/session").body(data.toJSONString());
        System.out.println(response.getStatus());
    }
}
