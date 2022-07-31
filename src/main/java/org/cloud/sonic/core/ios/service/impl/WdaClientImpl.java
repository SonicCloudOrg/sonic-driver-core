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
import org.cloud.sonic.core.ios.RespHandler;
import org.cloud.sonic.core.ios.models.BaseResp;
import org.cloud.sonic.core.ios.models.SessionInfo;
import org.cloud.sonic.core.ios.models.TouchActions;
import org.cloud.sonic.core.ios.service.WebElement;
import org.cloud.sonic.core.ios.service.WdaClient;
import org.cloud.sonic.core.tool.SonicRespException;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Slf4j
public class WdaClientImpl implements WdaClient {
    private String remoteUrl;
    private String sessionId;
    private RespHandler respHandler;
    private final String LEGACY_WEB_ELEMENT_IDENTIFIER = "ELEMENT";
    private final String WEB_ELEMENT_IDENTIFIER = "element-6066-11e4-a52e-4f735466cecf";
    private int FIND_ELEMENT_INTERVAL = 3000;
    private int FIND_ELEMENT_RETRY = 5;

    public WdaClientImpl() {
        respHandler = new RespHandler();
    }

    private void checkBundleId(String bundleId) throws SonicRespException {
        if (bundleId == null || bundleId.length() == 0) {
            log.error("bundleId not found.");
            throw new SonicRespException("bundleId not found.");
        }
    }

    private String parseElementId(Object o) {
        JSONObject jsonObject = (JSONObject) o;
        List<String> identifier = Arrays.asList(LEGACY_WEB_ELEMENT_IDENTIFIER, WEB_ELEMENT_IDENTIFIER);
        for (String i : identifier) {
            String result = jsonObject.getString(i);
            if (result != null && result.length() > 0) {
                return result;
            }
        }
        return "";
    }

    @Override
    public void checkSessionId() throws SonicRespException {
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
    public RespHandler getRespHandler() {
        return respHandler;
    }

    @Override
    public void setRespHandler(RespHandler respHandler) {
        this.respHandler = respHandler;
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
            log.info("session : {}", sessionInfo.getSessionId());
            log.info("session capabilities : {}", sessionInfo.getCapabilities());
        } else {
            log.error("start session failed.");
            log.error("cause: {}", b.getErr());
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
    public void setPasteboard(String contentType, String content) throws SonicRespException {
        checkSessionId();
        JSONObject data = new JSONObject();
        data.put("contentType", contentType);
        data.put("content", Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8)));
        BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/wda/setPasteboard")
                .body(data.toJSONString()));
        if (b.getErr() == null) {
            log.info("set pasteboard {} .", content);
        } else {
            log.error("set pasteboard failed.");
            throw new SonicRespException(b.getErr().getMessage());
        }
    }

    @Override
    public byte[] getPasteboard(String contentType) throws SonicRespException {
        checkSessionId();
        JSONObject data = new JSONObject();
        data.put("contentType", contentType);
        BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/wda/getPasteboard")
                .body(data.toJSONString()));
        if (b.getErr() == null) {
            byte[] result = Base64.getDecoder().decode(b.getValue().toString());
            log.info("get pasteboard {} .", result);
            return result;
        } else {
            log.error("get pasteboard failed.");
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

    @Override
    public void sendSiriCommand(String command) throws SonicRespException {
        if (command != null && command.length() != 0) {
            checkSessionId();
            JSONObject data = new JSONObject();
            data.put("text", command);
            BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/wda/siri/activate")
                    .body(data.toJSONString()));
            if (b.getErr() == null) {
                log.info("send siri command: {}", command);
            } else {
                log.error("send siri command [{}] failed.", command);
                throw new SonicRespException(b.getErr().getMessage());
            }
        } else {
            log.error("siri command is null!");
            throw new SonicRespException("siri command is null!");
        }
    }

    @Override
    public void appActivate(String bundleId) throws SonicRespException {
        checkSessionId();
        checkBundleId(bundleId);
        JSONObject data = new JSONObject();
        data.put("bundleId", bundleId);
        BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/wda/apps/activate")
                .body(data.toJSONString()));
        if (b.getErr() == null) {
            log.info("activate app {}.", bundleId);
        } else {
            log.error("activate app {} failed.", bundleId);
            throw new SonicRespException(b.getErr().getMessage());
        }
    }

    @Override
    public boolean appTerminate(String bundleId) throws SonicRespException {
        checkSessionId();
        checkBundleId(bundleId);
        JSONObject data = new JSONObject();
        data.put("bundleId", bundleId);
        BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/wda/apps/terminate")
                .body(data.toJSONString()));
        if (b.getErr() == null) {
            log.info("terminate app {} status: {}.", bundleId, b.getValue());
            return (boolean) b.getValue();
        } else {
            log.error("terminate app failed.", bundleId);
            throw new SonicRespException(b.getErr().getMessage());
        }
    }

    @Override
    public void appAuthReset(int resource) throws SonicRespException {
        checkSessionId();
        JSONObject data = new JSONObject();
        data.put("resource", resource);
        BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/wda/resetAppAuth")
                .body(data.toJSONString()));
        if (b.getErr() == null) {
            log.info("reset app auth {}.", resource);
        } else {
            log.error("reset app auth failed.");
            throw new SonicRespException(b.getErr().getMessage());
        }
    }

    @Override
    public WebElement findElement(String selector, String value, Integer retry, Integer interval) throws SonicRespException {
        WebElement webElement = null;
        int wait = 0;
        int intervalInit = (interval == null ? FIND_ELEMENT_INTERVAL : interval);
        int retryInit = (retry == null ? FIND_ELEMENT_RETRY : retry);
        String errMsg = "";
        while (wait < retryInit) {
            wait++;
            checkSessionId();
            JSONObject data = new JSONObject();
            data.put("using", selector);
            data.put("value", value);
            BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/element")
                    .body(data.toJSONString()));
            if (b.getErr() == null) {
                log.info("find element successful.");
                String id = parseElementId(b.getValue());
                if (id.length() > 0) {
                    webElement = new WebElementImpl(id, this);
                    break;
                } else {
                    log.error("parse element id {} failed. retried {} times, retry in {} ms.", b.getValue(), wait, intervalInit);
                }
            } else {
                log.error("element not found. retried {} times, retry in {} ms.", wait, intervalInit);
                errMsg = b.getErr().getMessage();
            }
            if (wait < retryInit) {
                try {
                    Thread.sleep(intervalInit);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (webElement == null) {
            throw new SonicRespException(errMsg);
        }
        return webElement;
    }
}
