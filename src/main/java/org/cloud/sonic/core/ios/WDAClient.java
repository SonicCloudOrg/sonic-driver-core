package org.cloud.sonic.core.ios;

import cn.hutool.http.HttpUtil;

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

    public String getSource(){
        HttpUtil.post(remoteUrl+"/");
    }
}
