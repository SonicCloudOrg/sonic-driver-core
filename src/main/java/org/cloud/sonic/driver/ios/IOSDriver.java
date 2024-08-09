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
package org.cloud.sonic.driver.ios;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.driver.common.enums.PasteboardType;
import org.cloud.sonic.driver.common.models.WindowSize;
import org.cloud.sonic.driver.common.tool.RespHandler;
import org.cloud.sonic.driver.common.tool.SonicRespException;
import org.cloud.sonic.driver.ios.enums.*;
import org.cloud.sonic.driver.ios.models.TouchActions;
import org.cloud.sonic.driver.ios.service.IOSElement;
import org.cloud.sonic.driver.ios.service.WdaClient;
import org.cloud.sonic.driver.ios.service.impl.IOSElementImpl;
import org.cloud.sonic.driver.ios.service.impl.WdaClientImpl;

import java.util.List;

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
     * show log.
     */
    public void showLog() {
        wdaClient.showLog();
    }

    /**
     * disable log.
     */
    public void disableLog() {
        wdaClient.disableLog();
    }

    /**
     * get device window size.
     *
     * @return
     * @throws SonicRespException
     */
    public WindowSize getWindowSize() throws SonicRespException {
        return wdaClient.getWindowSize();
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
     * double tap position on screen
     * @param x
     * @param y
     * @throws SonicRespException
     */
    public void doubleTap(int x, int y) throws SonicRespException {
        wdaClient.doubleTap(x,y);
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
        performTouchAction(new TouchActions().press(fromX, fromY).wait(300).move(toX, toY).wait(10).release());
    }

    /**
     * swipe position on screen with target time
     *
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @param duration 滑动完成的时间，单位为毫秒
     * @throws SonicRespException
     */
    public void swipe(double fromX, double fromY, double toX, double toY, double duration) throws SonicRespException {
        wdaClient.swipe(fromX, fromY, toX, toY, duration);
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
     * run app background in seconds.
     *
     * @param duration
     * @throws SonicRespException
     */
    public void appRunBackground(int duration) throws SonicRespException {
        wdaClient.appRunBackground(duration);
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

    /**
     * set default FindElement retry time and interval.
     *
     * @param retry
     * @param interval
     */
    public void setDefaultFindElementInterval(Integer retry, Integer interval) {
        wdaClient.setDefaultFindElementInterval(retry, interval);
    }

    /**
     * find element in device.
     *
     * @param iosSelector
     * @param value
     * @return
     * @throws SonicRespException
     */
    public IOSElement findElement(IOSSelector iosSelector, String value) throws SonicRespException {
        return findElement(iosSelector, value, null);
    }

    /**
     * find element in device.
     *
     * @param wdaElementID This id is the id returned by the wda lookup control
     * @return
     * @throws SonicRespException
     */
    public IOSElement findElement(String wdaElementID) throws SonicRespException {
        return new IOSElementImpl(wdaElementID, wdaClient);
    }

    /**
     * find element in device.
     *
     * @param xcuiElementType
     * @return
     * @throws SonicRespException
     */
    public IOSElement findElement(XCUIElementType xcuiElementType) throws SonicRespException {
        return findElement(xcuiElementType, null);
    }

    /**
     * find element in device.
     *
     * @param selector
     * @param value
     * @return
     * @throws SonicRespException
     */
    public IOSElement findElement(String selector, String value) throws SonicRespException {
        return findElement(selector, value, null);
    }

    /**
     * find element in device.
     *
     * @param iosSelector
     * @param value
     * @param retry
     * @return
     * @throws SonicRespException
     */
    public IOSElement findElement(IOSSelector iosSelector, String value, Integer retry) throws SonicRespException {
        return findElement(iosSelector, value, retry, null);
    }

    /**
     * find element in device.
     *
     * @param xcuiElementType
     * @param retry
     * @return
     * @throws SonicRespException
     */
    public IOSElement findElement(XCUIElementType xcuiElementType, Integer retry) throws SonicRespException {
        return findElement(xcuiElementType, retry, null);
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
    public IOSElement findElement(String selector, String value, Integer retry) throws SonicRespException {
        return findElement(selector, value, retry, null);
    }

    /**
     * find element in device.
     *
     * @param iosSelector
     * @param value
     * @param retry
     * @param interval
     * @return
     * @throws SonicRespException
     */
    public IOSElement findElement(IOSSelector iosSelector, String value, Integer retry, Integer interval) throws SonicRespException {
        return findElement(iosSelector.getSelector(), value, retry, interval);
    }

    /**
     * find element in device.
     *
     * @param xcuiElementType
     * @param retry
     * @param interval
     * @return
     * @throws SonicRespException
     */
    public IOSElement findElement(XCUIElementType xcuiElementType, Integer retry, Integer interval) throws SonicRespException {
        return findElement(IOSSelector.CLASS_NAME.getSelector(), xcuiElementType.getType(), retry, interval);
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
    public IOSElement findElement(String selector, String value, Integer retry, Integer interval) throws SonicRespException {
        return wdaClient.findElement(selector, value, retry, interval);
    }

    /**
     * find element list in device.
     *
     * @param iosSelector
     * @param value
     * @return
     * @throws SonicRespException
     */
    public List<IOSElement> findElementList(IOSSelector iosSelector, String value) throws SonicRespException {
        return findElementList(iosSelector, value, null);
    }

    /**
     * find element list in device.
     *
     * @param xcuiElementType
     * @return
     * @throws SonicRespException
     */
    public List<IOSElement> findElementList(XCUIElementType xcuiElementType) throws SonicRespException {
        return findElementList(xcuiElementType, null);
    }

    /**
     * find element list in device.
     *
     * @param selector
     * @param value
     * @return
     * @throws SonicRespException
     */
    public List<IOSElement> findElementList(String selector, String value) throws SonicRespException {
        return findElementList(selector, value, null);
    }

    /**
     * find element list in device.
     *
     * @param iosSelector
     * @param value
     * @param retry
     * @return
     * @throws SonicRespException
     */
    public List<IOSElement> findElementList(IOSSelector iosSelector, String value, Integer retry) throws SonicRespException {
        return findElementList(iosSelector, value, retry, null);
    }

    /**
     * find element list in device.
     *
     * @param xcuiElementType
     * @param retry
     * @return
     * @throws SonicRespException
     */
    public List<IOSElement> findElementList(XCUIElementType xcuiElementType, Integer retry) throws SonicRespException {
        return findElementList(xcuiElementType, retry, null);
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
    public List<IOSElement> findElementList(String selector, String value, Integer retry) throws SonicRespException {
        return findElementList(selector, value, retry, null);
    }

    /**
     * find element list in device.
     *
     * @param iosSelector
     * @param value
     * @param retry
     * @param interval
     * @return
     * @throws SonicRespException
     */
    public List<IOSElement> findElementList(IOSSelector iosSelector, String value, Integer retry, Integer interval) throws SonicRespException {
        return findElementList(iosSelector.getSelector(), value, retry, interval);
    }

    /**
     * find element list in device.
     *
     * @param xcuiElementType
     * @param retry
     * @param interval
     * @return
     * @throws SonicRespException
     */
    public List<IOSElement> findElementList(XCUIElementType xcuiElementType, Integer retry, Integer interval) throws SonicRespException {
        return findElementList(IOSSelector.CLASS_NAME.getSelector(), xcuiElementType.getType(), retry, interval);
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
    public List<IOSElement> findElementList(String selector, String value, Integer retry, Integer interval) throws SonicRespException {
        return wdaClient.findElementList(selector, value, retry, interval);
    }

    /**
     * get current active element
     * @return
     * @throws SonicRespException
     */
    public IOSElement activeElement() throws SonicRespException{
        return wdaClient.activeElement();
    }

    /**
     * get screenshot.
     *
     * @return
     * @throws SonicRespException
     */
    public byte[] screenshot() throws SonicRespException {
        return wdaClient.screenshot();
    }

    /**
     * set appium settings.
     *
     * @param settings
     * @throws SonicRespException
     */
    public void setAppiumSettings(JSONObject settings) throws SonicRespException {
        wdaClient.setAppiumSettings(settings);
    }

    /**
     * rotate screen orientation
     * @throws SonicRespException
     */
    public void rotate(Orientation orientation) throws SonicRespException {
        wdaClient.rotate(orientation);
    }

    public Orientation getRotate() throws SonicRespException {
        return wdaClient.getRotate();
    }
}
