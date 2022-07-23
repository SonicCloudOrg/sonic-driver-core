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
import org.cloud.sonic.core.ios.models.SystemButton;
import org.cloud.sonic.core.ios.models.TouchActions;
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
     * Init ios driver
     * @param url
     * @throws SonicRespException
     */
    public IOSDriver(String url) throws SonicRespException {
        wdaClient = new WdaClientImpl();
        wdaClient.setRemoteUrl(url);
        wdaClient.newSession(new JSONObject());
    }

    /**
     * Get wda client
     * @return
     */
    public WdaClient getWdaClient() {
        return wdaClient;
    }

    /**
     * get wda sessionId
     * @return
     */
    public String getSessionId() {
        return wdaClient.getSessionId();
    }

    /**
     * destroy sessionId
     * @throws SonicRespException
     */
    public void closeDriver() throws SonicRespException {
        wdaClient.closeSession();
    }

    /**
     * tap position on screen
     * @param x
     * @param y
     * @throws SonicRespException
     */
    public void tap(int x, int y) throws SonicRespException {
        wdaClient.performTouchAction(new TouchActions().press(x,y).release());
    }

    /**
     * long press position on screen
     * @param x
     * @param y
     * @param ms
     * @throws SonicRespException
     */
    public void longPress(int x, int y, int ms) throws SonicRespException {
        wdaClient.performTouchAction(new TouchActions().press(x,y).wait(ms).release());
    }

    /**
     * swipe position on screen
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @throws SonicRespException
     */
    public void swipe(int fromX, int fromY, int toX, int toY) throws SonicRespException {
        wdaClient.performTouchAction(new TouchActions().press(fromX,fromY).wait(50).move(toX,toY).wait(10).release());
    }

    /**
     * perform touch action
     * @param touchActions
     * @throws SonicRespException
     */
    public void performTouchAction(TouchActions touchActions) throws SonicRespException {
        wdaClient.performTouchAction(touchActions);
    }

    /**
     * press system button.
     * @param systemButton
     * @throws SonicRespException
     */
    public void pressButton(SystemButton systemButton) throws SonicRespException {
        wdaClient.pressButton(systemButton.getButton());
    }

    /**
     * send key without element
     * @param text
     * @throws SonicRespException
     */
    public void sendKeys(String text) throws SonicRespException {
        sendKeys(text, 3);
    }

    /**
     * send key without element
     * @param text
     * @param frequency
     * @throws SonicRespException
     */
    public void sendKeys(String text, int frequency) throws SonicRespException {
        wdaClient.sendKeys(text, frequency);
    }

    /**
     * get page source
     * @return
     * @throws SonicRespException
     */
    public String getPageSource() throws SonicRespException {
        return wdaClient.pageSource();
    }
}
