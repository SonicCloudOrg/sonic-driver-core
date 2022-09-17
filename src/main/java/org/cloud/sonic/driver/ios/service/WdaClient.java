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
package org.cloud.sonic.driver.ios.service;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.driver.common.tool.RespHandler;
import org.cloud.sonic.driver.ios.models.TouchActions;
import org.cloud.sonic.driver.common.models.WindowSize;
import org.cloud.sonic.driver.common.tool.Logger;
import org.cloud.sonic.driver.common.tool.SonicRespException;

import java.util.List;

/**
 * @author Eason
 * wda client interface
 */
public interface WdaClient {
    //Client Setting
    void setGlobalTimeOut(int timeOut);

    RespHandler getRespHandler();

    void setRespHandler(RespHandler respHandler);

    Logger getLogger();

    void showLog();

    void disableLog();

    //Session handler.
    String getRemoteUrl();

    void setRemoteUrl(String remoteUrl);

    String getSessionId();

    void setSessionId(String sessionId);

    void newSession(JSONObject capabilities) throws SonicRespException;

    void closeSession() throws SonicRespException;

    void checkSessionId() throws SonicRespException;

    //window handler.
    WindowSize getWindowSize() throws SonicRespException;

    //lock handler.
    boolean isLocked() throws SonicRespException;

    void lock() throws SonicRespException;

    void unlock() throws SonicRespException;

    //perform handler.
    void performTouchAction(TouchActions touchActions) throws SonicRespException;

    //button handler.
    void pressButton(String buttonName) throws SonicRespException;

    //keyboard handler.
    void sendKeys(String text, Integer frequency) throws SonicRespException;

    void setPasteboard(String contentType, String content) throws SonicRespException;

    byte[] getPasteboard(String contentType) throws SonicRespException;

    //source handler.
    String pageSource() throws SonicRespException;

    //siri handler.
    void sendSiriCommand(String command) throws SonicRespException;

    //app handler.
    void appActivate(String bundleId) throws SonicRespException;

    boolean appTerminate(String bundleId) throws SonicRespException;

    void appRunBackground(int duration) throws SonicRespException;

    void appAuthReset(int resource) throws SonicRespException;

    //element handler.
    void setDefaultFindElementInterval(Integer retry, Integer interval);

    IOSElement findElement(String selector, String value, Integer retry, Integer interval) throws SonicRespException;

    List<IOSElement> findElementList(String selector, String value, Integer retry, Integer interval) throws SonicRespException;

    //screen handler.
    byte[] screenshot() throws SonicRespException;

    //appium setting handler.
    void setAppiumSettings(JSONObject settings) throws SonicRespException;
}
