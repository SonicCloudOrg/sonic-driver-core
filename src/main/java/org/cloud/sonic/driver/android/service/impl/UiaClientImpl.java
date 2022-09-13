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

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.driver.android.service.UiaClient;
import org.cloud.sonic.driver.tool.RespHandler;
import org.cloud.sonic.driver.ios.models.WindowSize;
import org.cloud.sonic.driver.tool.Logger;
import org.cloud.sonic.driver.tool.SonicRespException;

public class UiaClientImpl implements UiaClient {
    private String remoteUrl;
    private String sessionId;
    private RespHandler respHandler;
    private Logger logger;
    private WindowSize size;

    public UiaClientImpl() {
        respHandler = new RespHandler();
        logger = new Logger();
    }

    @Override
    public void setGlobalTimeOut(int timeOut) {
        respHandler.setRequestTimeOut(timeOut);
    }

    @Override
    public RespHandler getRespHandler() {
        return null;
    }

    @Override
    public void setRespHandler(RespHandler respHandler) {

    }

    @Override
    public Logger getLogger() {
        return null;
    }

    @Override
    public void showLog() {

    }

    @Override
    public void disableLog() {

    }

    @Override
    public String getRemoteUrl() {
        return null;
    }

    @Override
    public void setRemoteUrl(String remoteUrl) {

    }

    @Override
    public String getSessionId() {
        return null;
    }

    @Override
    public void setSessionId(String sessionId) {

    }

    @Override
    public void newSession(JSONObject capabilities) throws SonicRespException {

    }

    @Override
    public void closeSession() throws SonicRespException {

    }

    @Override
    public void checkSessionId() throws SonicRespException {

    }
}
