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
package org.cloud.sonic.driver.android.service;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.driver.common.service.BaseClient;
import org.cloud.sonic.driver.common.service.WebElement;
import org.cloud.sonic.driver.common.tool.RespHandler;
import org.cloud.sonic.driver.common.tool.Logger;
import org.cloud.sonic.driver.common.tool.SonicRespException;
import org.cloud.sonic.driver.common.models.WindowSize;

import java.util.List;

/**
 * @author Eason
 * uia client interface
 */
public interface UiaClient extends BaseClient {
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

    //keyboard handler.
    void sendKeys(String text, boolean isCover) throws SonicRespException;

    void setPasteboard(String contentType, String content) throws SonicRespException;

    byte[] getPasteboard(String contentType) throws SonicRespException;

    //source handler.
    String pageSource() throws SonicRespException;

    //element handler.
    void setDefaultFindElementInterval(Integer retry, Integer interval);

    WebElement findElement(String selector, String value, Integer retry, Integer interval) throws SonicRespException;

    List<WebElement> findElementList(String selector, String value, Integer retry, Integer interval) throws SonicRespException;

    //screen handler.
    byte[] screenshot() throws SonicRespException;

    //appium setting handler.
    void setAppiumSettings(JSONObject settings) throws SonicRespException;
}
