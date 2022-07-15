package org.cloud.sonic.core.ios;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.core.tool.BaseResp;
import org.cloud.sonic.core.tool.RespHandler;
import org.cloud.sonic.core.tool.SonicRespException;

@Slf4j
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
            JSONObject re = JSONObject.parseObject(b.getValue().toString());
            setSessionId(re.getString("sessionId"));
            log.info("start session successful!");
        } else {
            log.error("start session failed.");
            throw new SonicRespException(b.getErr().getMessage());
        }
    }

    public void closeSession() {
        RespHandler.getResp(HttpUtil.createRequest(Method.DELETE, remoteUrl + "/session/" + sessionId));
        log.info("close session successful!");
    }

    public void swipe(int fromX, int fromY, int toX, int toY) throws SonicRespException {
        synchronized (WDAClient.class) {
            if (sessionId != null) {
                JSONObject data = new JSONObject();
                data.put("fromX", (float)fromX);
                data.put("fromY", (float)fromY);
                data.put("toX", (float)toX);
                data.put("toY", (float)toY);
                data.put("duration",0);
                BaseResp b = RespHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/wda/dragfromtoforduration")
                        .body(data.toJSONString()));
                if (b.getErr() != null) {
                    log.error("swipe failed.");
                    throw new SonicRespException(b.getErr().getMessage());
                }
            } else {
                log.error("sessionId not found.");
                throw new SonicRespException("sessionId not found.");
            }
        }
    }


}
