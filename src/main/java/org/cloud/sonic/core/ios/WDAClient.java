package org.cloud.sonic.core.ios;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.core.tool.BaseResp;
import org.cloud.sonic.core.tool.RespHandler;
import org.cloud.sonic.core.tool.SonicRespException;

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

    public void newSession() throws SonicRespException {
        JSONObject data = new JSONObject();
        data.put("capabilities", new JSONObject());
        BaseResp b = RespHandler.getResp(HttpUtil.createPost(remoteUrl + "/session").body(data.toJSONString()));
        if (b.getErr() == null) {
            setSessionId(b.getSessionId());
        } else {
            throw new SonicRespException("start session failed.");
        }
    }

    public void closeSession() {
        RespHandler.getResp(HttpUtil.createRequest(Method.DELETE, remoteUrl + "/session/" + getSessionId()));
    }
}
