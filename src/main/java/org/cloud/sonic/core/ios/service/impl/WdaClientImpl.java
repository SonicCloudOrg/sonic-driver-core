/*
 *  Copyright (C) [SonicCloudOrg] Sonic Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.cloud.sonic.core.ios.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.core.ios.models.BaseResp;
import org.cloud.sonic.core.ios.models.SessionInfo;
import org.cloud.sonic.core.ios.RespHandler;
import org.cloud.sonic.core.ios.models.TouchActions;
import org.cloud.sonic.core.ios.service.WdaClient;
import org.cloud.sonic.core.tool.SonicRespException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
public class WdaClientImpl implements WdaClient {
    private String remoteUrl;
    private String sessionId;
    private RespHandler respHandler = new RespHandler();

    private void checkSessionId() throws SonicRespException {
        if (sessionId == null || sessionId.length() == 0) {
            log.error("sessionId not found.");
            throw new SonicRespException("sessionId not found.");
        }
    }

    @Override
    public void setGlobalTimeOut(int timeOut) {
        respHandler.setRequestTimeOut(timeOut);
    }

    @Override
    public String getRemoteUrl() {
        return remoteUrl;
    }

    @Override
    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public void newSession(JSONObject capabilities) throws SonicRespException {
        JSONObject data = new JSONObject();
        data.put("capabilities", capabilities);
        BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session").body(data.toJSONString()));
        if (b.getErr() == null) {
            SessionInfo sessionInfo = JSON.parseObject(b.getValue().toString(), SessionInfo.class);
            setSessionId(sessionInfo.getSessionId());
            log.info("start session successful!");
            log.info("session capabilities : {}", sessionInfo.getCapabilities());
        } else {
            log.error("start session failed.");
            log.error("cause: {}",b.getErr());
            throw new SonicRespException(b.getErr().getMessage());
        }
    }

    @Override
    public void closeSession() throws SonicRespException {
        checkSessionId();
        respHandler.getResp(HttpUtil.createRequest(Method.DELETE, remoteUrl + "/session/" + sessionId));
        log.info("close session successful!");
    }

    @Override
    public boolean isLocked() throws SonicRespException {
        checkSessionId();
        BaseResp b = respHandler.getResp(HttpUtil.createGet(remoteUrl + "/session/" + sessionId + "/wda/locked"));
        if (b.getErr() == null) {
            log.info("device lock status: {}.", b.getValue());
            return (boolean) b.getValue();
        } else {
            log.error("get device lock status failed.");
            throw new SonicRespException(b.getErr().getMessage());
        }
    }

    @Override
    public void lock() throws SonicRespException {
        checkSessionId();
        BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/wda/lock"));
        if (b.getErr() == null) {
            log.info("lock device.");
        } else {
            log.error("lock device failed.");
            throw new SonicRespException(b.getErr().getMessage());
        }
    }

    @Override
    public void unlock() throws SonicRespException {
        checkSessionId();
        BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/wda/unlock"));
        if (b.getErr() == null) {
            log.info("unlock device.");
        } else {
            log.error("unlock device failed.");
            throw new SonicRespException(b.getErr().getMessage());
        }
    }

    @Override
    public void performTouchAction(TouchActions touchActions) throws SonicRespException {
        checkSessionId();
        BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/wda/touch/multi/perform")
                .body(String.valueOf(JSONObject.toJSON(touchActions))));
        if (b.getErr() == null) {
            log.info("perform action {}.", touchActions);
        } else {
            log.error("perform failed.");
            throw new SonicRespException(b.getErr().getMessage());
        }
    }

    @Override
    public void pressButton(String buttonName) throws SonicRespException {
        checkSessionId();
        JSONObject data = new JSONObject();
        data.put("name", buttonName);
        BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/wda/pressButton")
                .body(data.toJSONString()));
        if (b.getErr() == null) {
            log.info("press button {} .", buttonName);
        } else {
            log.error("press button failed.");
            throw new SonicRespException(b.getErr().getMessage());
        }
    }

    @Override
    public void sendKeys(String text, Integer frequency) throws SonicRespException {
        checkSessionId();
        JSONObject data = new JSONObject();
        data.put("value", text.split(""));
        data.put("frequency", frequency);
        BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/wda/keys")
                .body(data.toJSONString()));
        if (b.getErr() == null) {
            log.info("send key {} .", text);
        } else {
            log.error("send key failed.");
            throw new SonicRespException(b.getErr().getMessage());
        }
    }

    @Override
    public String pageSource() throws SonicRespException {
        checkSessionId();
        BaseResp b = respHandler.getResp(HttpUtil.createGet(remoteUrl + "/session/" + sessionId + "/source"), 60000);
        if (b.getErr() == null) {
            log.info("get page source.");
            return b.getValue().toString();
        } else {
            log.error("get page source failed.");
            throw new SonicRespException(b.getErr().getMessage());
        }
    }


}
