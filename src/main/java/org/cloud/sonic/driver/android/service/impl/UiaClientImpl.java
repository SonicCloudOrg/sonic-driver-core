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
import org.cloud.sonic.driver.android.service.AndroidElement;
import org.cloud.sonic.driver.android.service.UiaClient;
import org.cloud.sonic.driver.common.models.BaseResp;
import org.cloud.sonic.driver.common.models.SessionInfo;
import org.cloud.sonic.driver.common.models.WindowSize;
import org.cloud.sonic.driver.common.tool.Logger;
import org.cloud.sonic.driver.common.tool.RespHandler;
import org.cloud.sonic.driver.common.tool.SonicRespException;

import java.nio.charset.StandardCharsets;
import java.util.*;

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
            BaseResp b = respHandler.getResp(HttpUtil.createGet(remoteUrl + "/session/" + sessionId + "/window/:windowHandle/size"));
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
    public void sendKeys(String text, boolean isCover) throws SonicRespException {
        checkSessionId();
        JSONObject data = new JSONObject();
        data.put("text", text);
        data.put("replace", isCover);
        BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/keys")
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
        data.put("contentType", contentType.toUpperCase(Locale.ROOT));
        data.put("content", Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8)));
        BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/appium/device/set_clipboard")
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
        data.put("contentType", contentType.toUpperCase(Locale.ROOT));
        BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/appium/device/get_clipboard")
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
    public AndroidElement findElement(String selector, String value, Integer retry, Integer interval) throws SonicRespException {
        AndroidElement androidElement = null;
        int wait = 0;
        int intervalInit = (interval == null ? FIND_ELEMENT_INTERVAL : interval);
        int retryInit = (retry == null ? FIND_ELEMENT_RETRY : retry);
        String errMsg = "";
        while (wait < retryInit) {
            wait++;
            checkSessionId();
            JSONObject data = new JSONObject();
            data.put("strategy", selector);
            data.put("selector", value);
            BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/element")
                    .body(data.toJSONString()));
            if (b.getErr() == null) {
                logger.info("find element successful.");
                String id = parseElementId(b.getValue());
                if (id.length() > 0) {
                    androidElement = new AndroidElementImpl(id, this);
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
        if (androidElement == null) {
            throw new SonicRespException(errMsg);
        }
        return androidElement;
    }

    @Override
    public List<AndroidElement> findElementList(String selector, String value, Integer retry, Integer interval) throws SonicRespException {
        List<AndroidElement> androidElementList = new ArrayList<>();
        int wait = 0;
        int intervalInit = (interval == null ? FIND_ELEMENT_INTERVAL : interval);
        int retryInit = (retry == null ? FIND_ELEMENT_RETRY : retry);
        String errMsg = "";
        while (wait < retryInit) {
            wait++;
            checkSessionId();
            JSONObject data = new JSONObject();
            data.put("strategy", selector);
            data.put("selector", value);
            BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/elements")
                    .body(data.toJSONString()));
            if (b.getErr() == null) {
                logger.info("find elements successful.");
                List<JSONObject> ids = JSON.parseObject(b.getValue().toString(), ArrayList.class);
                for (JSONObject ele : ids) {
                    String id = parseElementId(ele);
                    if (id.length() > 0) {
                        androidElementList.add(new AndroidElementImpl(id, this));
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
        if (androidElementList.size() == 0) {
            throw new SonicRespException(errMsg);
        }
        return androidElementList;
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

    @Override
    public void tap(int x, int y) throws SonicRespException {
        checkSessionId();
        JSONObject data = new JSONObject();
        data.put("x", x);
        data.put("y", y);
        BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/appium/tap")
                .body(data.toJSONString()));
        if (b.getErr() == null) {
            logger.info("perform tap action %s.", data.toString());
        } else {
            logger.error("perform tap action failed.");
            throw new SonicRespException(b.getErr().getMessage());
        }
    }

    @Override
    public void longPress(int x, int y, int ms) throws SonicRespException {

    }

    @Override
    public void swipe(int fromX, int fromY, int toX, int toY, Integer duration) throws SonicRespException {
        checkSessionId();
        JSONObject data = new JSONObject();
        data.put("startX", fromX);
        data.put("startY", fromY);
        data.put("endX", toX);
        data.put("endY", toY);
        // steps 参数为uiautomator层定义的单位
        // example：So for a 100 steps, the swipe will take about 1/2 second to complete.
        if (duration == null) {
            data.put("steps", 100);
        } else {
            data.put("steps", duration / 5);
        }
        BaseResp b = respHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/touch/perform")
                .body(data.toJSONString()));
        if (b.getErr() == null) {
            logger.info("perform swipe action %s.", data.toString());
        } else {
            logger.error("perform swipe action failed.");
            throw new SonicRespException(b.getErr().getMessage());
        }
    }

}
