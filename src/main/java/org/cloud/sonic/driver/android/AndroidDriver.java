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
package org.cloud.sonic.driver.android;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.driver.android.enmus.AndroidSelector;
import org.cloud.sonic.driver.android.service.AndroidElement;
import org.cloud.sonic.driver.android.service.UiaClient;
import org.cloud.sonic.driver.android.service.impl.AndroidElementImpl;
import org.cloud.sonic.driver.android.service.impl.UiaClientImpl;
import org.cloud.sonic.driver.common.enums.PasteboardType;
import org.cloud.sonic.driver.common.models.WindowSize;
import org.cloud.sonic.driver.common.tool.RespHandler;
import org.cloud.sonic.driver.common.tool.SonicRespException;

import java.util.List;

/**
 * @author Eason
 * android driver
 */
public class AndroidDriver {
    private UiaClient uiaClient;

    /**
     * Init android driver.
     *
     * @param url
     * @throws SonicRespException
     */
    public AndroidDriver(String url) throws SonicRespException {
        this(url, RespHandler.DEFAULT_REQUEST_TIMEOUT);
    }

    /**
     * Init android driver.
     *
     * @param url
     * @param timeOut
     * @throws SonicRespException
     */
    public AndroidDriver(String url, int timeOut) throws SonicRespException {
        this(url, timeOut, new JSONObject());
    }

    /**
     * Init android driver.
     *
     * @param url
     * @param cap
     * @throws SonicRespException
     */
    public AndroidDriver(String url, JSONObject cap) throws SonicRespException {
        this(url, RespHandler.DEFAULT_REQUEST_TIMEOUT, cap);
    }

    /**
     * Init android driver.
     *
     * @param url
     * @param timeOut
     * @param cap
     * @throws SonicRespException
     */
    public AndroidDriver(String url, int timeOut, JSONObject cap) throws SonicRespException {
        uiaClient = new UiaClientImpl();
        uiaClient.setRemoteUrl(url);
        uiaClient.setGlobalTimeOut(timeOut);
        uiaClient.newSession(cap);
    }

    /**
     * Get uia2 client.
     *
     * @return
     */
    public UiaClient getUiaClient() {
        return uiaClient;
    }

    /**
     * get uia2 sessionId.
     *
     * @return
     */
    public String getSessionId() {
        return uiaClient.getSessionId();
    }

    /**
     * destroy sessionId.
     *
     * @throws SonicRespException
     */
    public void closeDriver() throws SonicRespException {
        uiaClient.closeSession();
    }

    /**
     * show log.
     */
    public void showLog() {
        uiaClient.showLog();
    }

    /**
     * disable log.
     */
    public void disableLog() {
        uiaClient.disableLog();
    }

    /**
     * get device window size.
     *
     * @return
     * @throws SonicRespException
     */
    public WindowSize getWindowSize() throws SonicRespException {
        return uiaClient.getWindowSize();
    }

    /**
     * send key without element.
     *
     * @param text
     * @throws SonicRespException
     */
    public void sendKeys(String text) throws SonicRespException {
        sendKeys(text, false);
    }

    /**
     * send key without element.
     *
     * @param text
     * @param isCover
     * @throws SonicRespException
     */
    public void sendKeys(String text, boolean isCover) throws SonicRespException {
        uiaClient.sendKeys(text, isCover);
    }

    /**
     * set pasteboard.
     *
     * @param contentType
     * @param content
     * @throws SonicRespException
     */
    public void setPasteboard(String contentType, String content) throws SonicRespException {
        uiaClient.setPasteboard(contentType, content);
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
        return uiaClient.getPasteboard(contentType);
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
        return uiaClient.pageSource();
    }

    /**
     * set default FindElement retry time and interval.
     *
     * @param retry
     * @param interval
     */
    public void setDefaultFindElementInterval(Integer retry, Integer interval) {
        uiaClient.setDefaultFindElementInterval(retry, interval);
    }

    /**
     * find element in device.
     *
     * @param androidSelector
     * @param value
     * @return
     * @throws SonicRespException
     */
    public AndroidElement findElement(AndroidSelector androidSelector, String value) throws SonicRespException {
        return findElement(androidSelector, value, null);
    }

    /**
     * find element in device.
     *
     * @param uiaElementID This ID is the id returned by uia after finding the control
     * @return
     * @throws SonicRespException
     */
    public AndroidElement findElement(String uiaElementID) throws SonicRespException {
        return new AndroidElementImpl(uiaElementID, uiaClient);
    }

    /**
     * find element in device.
     *
     * @param selector
     * @param value
     * @return
     * @throws SonicRespException
     */
    public AndroidElement findElement(String selector, String value) throws SonicRespException {
        return findElement(selector, value, null);
    }

    /**
     * find element in device.
     *
     * @param androidSelector
     * @param value
     * @param retry
     * @return
     * @throws SonicRespException
     */
    public AndroidElement findElement(AndroidSelector androidSelector, String value, Integer retry) throws SonicRespException {
        return findElement(androidSelector, value, retry, null);
    }

    /**
     * find element in device.
     *
     * @param selector
     * @param value
     * @param retry
     * @return
     * @throws SonicRespException
     */
    public AndroidElement findElement(String selector, String value, Integer retry) throws SonicRespException {
        return findElement(selector, value, retry, null);
    }

    /**
     * find element in device.
     *
     * @param androidSelector
     * @param value
     * @param retry
     * @param interval
     * @return
     * @throws SonicRespException
     */
    public AndroidElement findElement(AndroidSelector androidSelector, String value, Integer retry, Integer interval) throws SonicRespException {
        return findElement(androidSelector.getSelector(), value, retry, interval);
    }

    /**
     * find element in device.
     *
     * @param selector
     * @param value
     * @param retry
     * @param interval
     * @return
     * @throws SonicRespException
     */
    public AndroidElement findElement(String selector, String value, Integer retry, Integer interval) throws SonicRespException {
        return uiaClient.findElement(selector, value, retry, interval);
    }

    /**
     * find element list in device.
     *
     * @param androidSelector
     * @param value
     * @return
     * @throws SonicRespException
     */
    public List<AndroidElement> findElementList(AndroidSelector androidSelector, String value) throws SonicRespException {
        return findElementList(androidSelector, value, null);
    }

    /**
     * find element list in device.
     *
     * @param selector
     * @param value
     * @return
     * @throws SonicRespException
     */
    public List<AndroidElement> findElementList(String selector, String value) throws SonicRespException {
        return findElementList(selector, value, null);
    }

    /**
     * find element list in device.
     *
     * @param androidSelector
     * @param value
     * @param retry
     * @return
     * @throws SonicRespException
     */
    public List<AndroidElement> findElementList(AndroidSelector androidSelector, String value, Integer retry) throws SonicRespException {
        return findElementList(androidSelector, value, retry, null);
    }

    /**
     * find element list in device.
     *
     * @param selector
     * @param value
     * @param retry
     * @return
     * @throws SonicRespException
     */
    public List<AndroidElement> findElementList(String selector, String value, Integer retry) throws SonicRespException {
        return findElementList(selector, value, retry, null);
    }

    /**
     * find element list in device.
     *
     * @param androidSelector
     * @param value
     * @param retry
     * @param interval
     * @return
     * @throws SonicRespException
     */
    public List<AndroidElement> findElementList(AndroidSelector androidSelector, String value, Integer retry, Integer interval) throws SonicRespException {
        return findElementList(androidSelector.getSelector(), value, retry, interval);
    }

    /**
     * find element list in device.
     *
     * @param selector
     * @param value
     * @param retry
     * @param interval
     * @return
     * @throws SonicRespException
     */
    public List<AndroidElement> findElementList(String selector, String value, Integer retry, Integer interval) throws SonicRespException {
        return uiaClient.findElementList(selector, value, retry, interval);
    }

    /**
     * get screenshot.
     *
     * @return
     * @throws SonicRespException
     */
    public byte[] screenshot() throws SonicRespException {
        return uiaClient.screenshot();
    }

    /**
     * set appium settings.
     *
     * @param settings
     * @throws SonicRespException
     */
    public void setAppiumSettings(JSONObject settings) throws SonicRespException {
        uiaClient.setAppiumSettings(settings);
    }

    /**
     * tap position on screen.
     *
     * @param x
     * @param y
     * @throws SonicRespException
     */
    public void tap(int x, int y) throws SonicRespException {
        uiaClient.tap(x, y);
    }

    /**
     * long press position on screen.
     *
     * @param x
     * @param y
     * @param ms
     * @throws SonicRespException
     */
    public void longPress(double x, double y, double ms) throws SonicRespException {
        uiaClient.longPress(x, y, ms);
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
        this.swipe(fromX, fromY, toX, toY, null);
    }

    /**
     * swipe position on screen with target time
     *
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @param duration
     * @throws SonicRespException
     */
    public void swipe(int fromX, int fromY, int toX, int toY, Integer duration) throws SonicRespException {
        uiaClient.swipe(fromX, fromY, toX, toY, duration);
    }

    /**
     * Performs a long press followed by an immediate drag to a location and releases.
     * fromX(Y) are required if elementId is not provided, so do toX(Y) if destElId is not provided.
     *
     * @param fromX     Starting X coordinate
     * @param fromY     Starting Y coordinate
     * @param toX       Ending X coordinate
     * @param toY       Ending Y coordinate
     * @param duration  Duration of the action in milliseconds
     * @param elementId ID of the original element (optional), for specific interaction scenarios
     * @param destElId  ID of the target element (optional), for specific interaction scenarios
     * @throws SonicRespException Throws when the operation fails
     */
    public void drag(int fromX, int fromY, int toX, int toY, Integer duration, String elementId, String destElId) throws SonicRespException {
        uiaClient.drag(fromX, fromY, toX, toY, duration, elementId, destElId);
    }

    /**
     * Performs a touch action.
     * This method delegates to the UIA client's touchAction method to simulate
     * a touch event at a specified position on the screen with a given action type.
     *
     * @param methodType The type of touch action, enumerate in (down, up, move).
     *                   Specific supported types depend on the UIA client's implementation.
     * @param x          The X coordinate of the touch point.
     * @param y          The Y coordinate of the touch point.
     * @throws SonicRespException If an error occurs while performing the touch action,
     *                            this exception is thrown.
     */
    public void touchAction(String methodType, int x, int y) throws SonicRespException {
        uiaClient.touchAction(methodType, x, y);
    }

}
