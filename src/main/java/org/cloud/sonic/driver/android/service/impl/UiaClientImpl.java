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
package org.cloud.sonic.driver.android.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.driver.android.service.UiaClient;
import org.cloud.sonic.driver.common.models.BaseResp;
import org.cloud.sonic.driver.common.models.SessionInfo;
import org.cloud.sonic.driver.common.service.WebElement;
import org.cloud.sonic.driver.common.service.impl.WebElementImpl;
import org.cloud.sonic.driver.common.tool.RespHandler;
import org.cloud.sonic.driver.ios.models.TouchActions;
import org.cloud.sonic.driver.ios.models.WindowSize;
import org.cloud.sonic.driver.common.tool.Logger;
import org.cloud.sonic.driver.common.tool.SonicRespException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class UiaClientImpl implements UiaClient {
    private String remoteUrl;
    private String sessionId;
    private RespHandler respHandler;
    private Logger logger;
    private final String LEGACY_WEB_ELEMENT_IDENTIFIER = "ELEMENT";
    private final String WEB_ELEMENT_IDENTIFIER = "element-6066-11e4-a52e-4f735466cecf";
    private int FIND_ELEMENT_INTERVAL = 3000;
    private int FIND_ELEMENT_RETRY = 5;
    private WindowSize size;

    public UiaClientImpl() {
        respHandler = new RespHandler();
        logger = new Logger();
    }

    private void checkBundleId(String bundleId) throws SonicRespException {
        if (bundleId == null || bundleId.length() == 0) {
            logger.error("bundleId not found.");
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
    public Logger getLogger() {
        return logger;
    }

    @Override
    public void showLog() {
        logger.showLog();
    }

    @Override
    public void disableLog() {
        logger.disableLog();
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
            logger.info("start session successful!");
            logger.info("session : %s", sessionInfo.getSessionId());
            logger.info("session capabilities : %s", sessionInfo.getCapabilities().toString());
        } else {
            logger.error("start session failed.");
            logger.error("cause: %s", b.getErr().toString());
            throw new SonicRespException(b.getErr().getMessage());
        }
    }

    @Override
    public void closeSession() throws SonicRespException {
        checkSessionId();
        respHandler.getResp(HttpUtil.createRequest(Method.DELETE, remoteUrl + "/session/" + sessionId));
        logger.info("close session successful!");
    }

    @Override
    public void checkSessionId() throws SonicRespException {
        if (sessionId == null || sessionId.length() == 0) {
            logger.error("sessionId not found.");
            throw new SonicRespException("sessionId not found.");
        }
    }

    @Override
    public WindowSize getWindowSize() throws SonicRespException {
        if (size == null) {
            checkSessionId();
            BaseResp b = respHandler.getResp(HttpUtil.createGet(remoteUrl + "/session/" + sessionId + "/window/size"));
            if (b.getErr() == null) {
                size = JSON.parseObject(b.getValue().toString(), WindowSize.class);
                logger.info("get window size %s.", size.toString());
            } else {
                logger.error("get window size failed.");
                throw new SonicRespException(b.getErr().getMessage());
            }
        }
        return size;
    }

    @Override
    public void performTouchAction(TouchActions touchActions) throws SonicRespException {
        checkSessionId();
        BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/touch/multi/perform")
                .body(String.valueOf(JSONObject.toJSON(touchActions))));
        if (b.getErr() == null) {
            logger.info("perform action %s.", touchActions.toString());
        } else {
            logger.error("perform failed.");
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
            logger.info("press button %s.", buttonName);
        } else {
            logger.error("press button failed.");
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
            logger.info("send key %s.", text);
        } else {
            logger.error("send key failed.");
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
            logger.info("set pasteboard %s.", content);
        } else {
            logger.error("set pasteboard failed.");
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
            byte[] result = Base64.getMimeDecoder().decode(b.getValue().toString());
            logger.info("get pasteboard length: %d.", result.length);
            return result;
        } else {
            logger.error("get pasteboard failed.");
            throw new SonicRespException(b.getErr().getMessage());
        }
    }

    @Override
    public String pageSource() throws SonicRespException {
        checkSessionId();
        BaseResp b = respHandler.getResp(HttpUtil.createGet(remoteUrl + "/session/" + sessionId + "/source"), 60000);
        if (b.getErr() == null) {
            logger.info("get page source.");
            return b.getValue().toString();
        } else {
            logger.error("get page source failed.");
            throw new SonicRespException(b.getErr().getMessage());
        }
    }

    @Override
    public void setDefaultFindElementInterval(Integer retry, Integer interval) {
        if (retry != null) {
            FIND_ELEMENT_RETRY = retry;
        }
        if (interval != null) {
            FIND_ELEMENT_INTERVAL = interval;
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
                logger.info("find element successful.");
                String id = parseElementId(b.getValue());
                if (id.length() > 0) {
                    webElement = new WebElementImpl(id, this);
                    break;
                } else {
                    logger.error("parse element id %s failed. retried %d times, retry in %d ms.", b.getValue().toString(), wait, intervalInit);
                }
            } else {
                logger.error("element not found. retried %d times, retry in %d ms.", wait, intervalInit);
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

    @Override
    public List<WebElement> findElementList(String selector, String value, Integer retry, Integer interval) throws SonicRespException {
        List<WebElement> webElementList = new ArrayList<>();
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
            BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/elements")
                    .body(data.toJSONString()));
            if (b.getErr() == null) {
                logger.info("find elements successful.");
                List<JSONObject> ids = JSON.parseObject(b.getValue().toString(), ArrayList.class);
                for (JSONObject ele : ids) {
                    String id = parseElementId(ele);
                    if (id.length() > 0) {
                        webElementList.add(new WebElementImpl(id, this));
                    } else {
                        logger.error("parse element id %s failed.", ele);
                        continue;
                    }
                }
                break;
            } else {
                logger.error("elements not found. retried %d times, retry in %d ms.", wait, intervalInit);
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
        if (webElementList.size() == 0) {
            throw new SonicRespException(errMsg);
        }
        return webElementList;
    }

    @Override
    public byte[] screenshot() throws SonicRespException {
        checkSessionId();
        BaseResp b = respHandler.getResp(
                HttpUtil.createGet(remoteUrl + "/session/" + sessionId + "/screenshot"), 60000);
        if (b.getErr() == null) {
            logger.info("get screenshot.");
            return Base64.getMimeDecoder().decode(b.getValue().toString());
        } else {
            logger.error("get screenshot failed.");
            throw new SonicRespException(b.getErr().getMessage());
        }
    }

    @Override
    public void setAppiumSettings(JSONObject settings) throws SonicRespException {
        checkSessionId();
        JSONObject data = new JSONObject();
        data.put("settings", settings);
        BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/appium/settings")
                .body(data.toJSONString()));
        if (b.getErr() == null) {
            logger.info("set appium settings %s.", settings.toJSONString());
        } else {
            logger.error("set appium settings failed.");
            throw new SonicRespException(b.getErr().getMessage());
        }
    }
}
