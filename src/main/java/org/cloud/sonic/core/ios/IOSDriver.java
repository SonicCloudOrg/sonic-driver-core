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
package org.cloud.sonic.core.ios;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.core.ios.enums.AuthResource;
import org.cloud.sonic.core.ios.enums.PasteboardType;
import org.cloud.sonic.core.ios.enums.SystemButton;
import org.cloud.sonic.core.ios.enums.TextKey;
import org.cloud.sonic.core.ios.models.*;
import org.cloud.sonic.core.ios.service.WdaClient;
import org.cloud.sonic.core.ios.service.impl.WdaClientImpl;
import org.cloud.sonic.core.tool.SonicRespException;

/**
 * @author Eason
 * ios driver
 */
public class IOSDriver {
    private WdaClient wdaClient;

    /**
     * Init ios driver.
     *
     * @param url
     * @throws SonicRespException
     */
    public IOSDriver(String url) throws SonicRespException {
        this(url, RespHandler.DEFAULT_REQUEST_TIMEOUT);
    }

    /**
     * Init ios driver.
     *
     * @param url
     * @param timeOut
     * @throws SonicRespException
     */
    public IOSDriver(String url, int timeOut) throws SonicRespException {
        this(url, timeOut, new JSONObject());
    }

    /**
     * Init ios driver.
     *
     * @param url
     * @param cap
     * @throws SonicRespException
     */
    public IOSDriver(String url, JSONObject cap) throws SonicRespException {
        this(url, RespHandler.DEFAULT_REQUEST_TIMEOUT, cap);
    }

    /**
     * Init ios driver.
     *
     * @param url
     * @param timeOut
     * @param cap
     * @throws SonicRespException
     */
    public IOSDriver(String url, int timeOut, JSONObject cap) throws SonicRespException {
        wdaClient = new WdaClientImpl();
        wdaClient.setRemoteUrl(url);
        wdaClient.setGlobalTimeOut(timeOut);
        wdaClient.newSession(cap);
    }

    /**
     * Get wda client.
     *
     * @return
     */
    public WdaClient getWdaClient() {
        return wdaClient;
    }

    /**
     * get wda sessionId.
     *
     * @return
     */
    public String getSessionId() {
        return wdaClient.getSessionId();
    }

    /**
     * destroy sessionId.
     *
     * @throws SonicRespException
     */
    public void closeDriver() throws SonicRespException {
        wdaClient.closeSession();
    }

    /**
     * get device lock status.
     *
     * @return
     * @throws SonicRespException
     */
    public boolean isLocked() throws SonicRespException {
        return wdaClient.isLocked();
    }

    /**
     * lock device.
     *
     * @throws SonicRespException
     */
    public void lock() throws SonicRespException {
        wdaClient.lock();
    }

    /**
     * unlock device.
     *
     * @throws SonicRespException
     */
    public void unlock() throws SonicRespException {
        wdaClient.unlock();
    }

    /**
     * tap position on screen.
     *
     * @param x
     * @param y
     * @throws SonicRespException
     */
    public void tap(int x, int y) throws SonicRespException {
        performTouchAction(new TouchActions().press(x, y).release());
    }

    /**
     * long press position on screen.
     *
     * @param x
     * @param y
     * @param ms
     * @throws SonicRespException
     */
    public void longPress(int x, int y, int ms) throws SonicRespException {
        performTouchAction(new TouchActions().press(x, y).wait(ms).release());
    }

    /**
     * swipe position on screen.
     *
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @throws SonicRespException
     */
    public void swipe(int fromX, int fromY, int toX, int toY) throws SonicRespException {
        performTouchAction(new TouchActions().press(fromX, fromY).wait(50).move(toX, toY).wait(10).release());
    }

    /**
     * perform touch action.
     *
     * @param touchActions
     * @throws SonicRespException
     */
    public void performTouchAction(TouchActions touchActions) throws SonicRespException {
        wdaClient.performTouchAction(touchActions);
    }

    /**
     * press system button.
     *
     * @param systemButton
     * @throws SonicRespException
     */
    public void pressButton(String systemButton) throws SonicRespException {
        wdaClient.pressButton(systemButton);
    }

    /**
     * press system button.
     *
     * @param systemButton
     * @throws SonicRespException
     */
    public void pressButton(SystemButton systemButton) throws SonicRespException {
        pressButton(systemButton.getButton());
    }

    /**
     * send key without element.
     *
     * @param text
     * @throws SonicRespException
     */
    public void sendKeys(String text) throws SonicRespException {
        sendKeys(text, 3);
    }

    /**
     * send key without element.
     *
     * @param text
     * @param frequency
     * @throws SonicRespException
     */
    public void sendKeys(String text, int frequency) throws SonicRespException {
        wdaClient.sendKeys(text, frequency);
    }

    /**
     * send key without element.
     *
     * @param text
     * @throws SonicRespException
     */
    public void sendKeys(TextKey text) throws SonicRespException {
        sendKeys(text, 3);
    }

    /**
     * send key without element.
     *
     * @param text
     * @param frequency
     * @throws SonicRespException
     */
    public void sendKeys(TextKey text, int frequency) throws SonicRespException {
        sendKeys(text.getKey(), frequency);
    }

    /**
     * set pasteboard.
     *
     * @param contentType
     * @param content
     * @throws SonicRespException
     */
    public void setPasteboard(String contentType, String content) throws SonicRespException {
        wdaClient.setPasteboard(contentType, content);
    }

    /**
     * set pasteboard.
     *
     * @param pasteboardType
     * @param content
     * @throws SonicRespException
     */
    public void setPasteboard(PasteboardType pasteboardType, String content) throws SonicRespException {
        setPasteboard(pasteboardType.getType(), content);
    }

    /**
     * get pasteboard.
     *
     * @param contentType
     * @return
     * @throws SonicRespException
     */
    public byte[] getPasteboard(String contentType) throws SonicRespException {
        return wdaClient.getPasteboard(contentType);
    }

    /**
     * get pasteboard.
     *
     * @param pasteboardType
     * @return
     * @throws SonicRespException
     */
    public byte[] getPasteboard(PasteboardType pasteboardType) throws SonicRespException {
        return getPasteboard(pasteboardType.getType());
    }

    /**
     * get page source.
     *
     * @return
     * @throws SonicRespException
     */
    public String getPageSource() throws SonicRespException {
        return wdaClient.pageSource();
    }

    /**
     * send siri command.
     *
     * @param command
     * @throws SonicRespException
     */
    public void sendSiriCommand(String command) throws SonicRespException {
        wdaClient.sendSiriCommand(command);
    }

    /**
     * activate app.
     *
     * @param bundleId
     * @throws SonicRespException
     */
    public void appActivate(String bundleId) throws SonicRespException {
        wdaClient.appActivate(bundleId);
    }

    /**
     * terminate app and get status.
     *
     * @param bundleId
     * @return
     * @throws SonicRespException
     */
    public boolean appTerminate(String bundleId) throws SonicRespException {
        return wdaClient.appTerminate(bundleId);
    }

    /**
     * reset app auth source.
     *
     * @param resource
     * @throws SonicRespException
     */
    public void appAuthReset(int resource) throws SonicRespException {
        wdaClient.appAuthReset(resource);
    }

    /**
     * reset app auth source.
     *
     * @param authResource
     * @throws SonicRespException
     */
    public void appAuthReset(AuthResource authResource) throws SonicRespException {
        appAuthReset(authResource.getResource());
    }
}
