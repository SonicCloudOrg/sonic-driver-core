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
package org.cloud.sonic.core.ios.service;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.core.ios.models.TouchActions;
import org.cloud.sonic.core.tool.SonicRespException;

/**
 * @author Eason
 * wda client interface
 */
public interface WdaClient {
    //Client Setting
    void setGlobalTimeOut(int timeOut);

    //Session handler.
    String getRemoteUrl();

    void setRemoteUrl(String remoteUrl);

    String getSessionId();

    void setSessionId(String sessionId);

    void newSession(JSONObject capabilities) throws SonicRespException;

    void closeSession() throws SonicRespException;

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

    void appAuthReset(int resource) throws SonicRespException;
}
