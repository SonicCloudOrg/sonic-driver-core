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
package org.cloud.sonic.driver.poco.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.driver.common.models.WindowSize;
import org.cloud.sonic.driver.common.tool.Logger;
import org.cloud.sonic.driver.common.tool.SonicRespException;
import org.cloud.sonic.driver.poco.enums.PocoEngine;
import org.cloud.sonic.driver.poco.models.PocoElement;
import org.cloud.sonic.driver.poco.service.PocoClient;
import org.cloud.sonic.driver.poco.service.PocoConnection;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.cloud.sonic.driver.poco.enums.PocoEngine.*;

public class PocoClientImpl implements PocoClient {
    private Logger logger;
    private PocoConnection pocoConnection;
    PocoEngine engine;

    public PocoClientImpl() {
        logger = new Logger();
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
    public void newClient(PocoEngine engine, int port) {
        this.engine = engine;
        if (engine.equals(COCOS_2DX_JS) || engine.equals(COCOS_CREATOR) || engine.equals(EGRET)) {
            pocoConnection = new WebSocketClientImpl(port, logger);
        } else {
            pocoConnection = new SocketClientImpl(port, logger);
        }
        pocoConnection.connect();
    }

    @Override
    public void closeClient() {
        pocoConnection.disconnect();
    }

    @Override
    public PocoElement pageSource() throws SonicRespException {
        PocoElement pocoElement = JSON.parseObject(pageSourceForJson().toJSONString(), PocoElement.class);
        return pocoElement;
    }

    @Override
    public JSONObject pageSourceForJson() throws SonicRespException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jsonrpc", "2.0");
        jsonObject.put("params", Arrays.asList(true));
        jsonObject.put("id", UUID.randomUUID().toString());
        jsonObject.put("method", "Dump");
        if (engine.equals(COCOS_CREATOR) || engine.equals(COCOS_2DX_JS)) {
            jsonObject.put("method", "dump");
        }
        return (JSONObject) pocoConnection.sendAndReceive(jsonObject);
    }

    @Override
    public WindowSize getScreenSize() throws SonicRespException {
        if (engine.equals(UNITY_3D) || engine.equals(COCOS_2DX_LUA)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("jsonrpc", "2.0");
            jsonObject.put("params", Arrays.asList(true));
            jsonObject.put("id", UUID.randomUUID().toString());
            jsonObject.put("method", "GetScreenSize");
            List<Integer> result = ((JSONArray) pocoConnection.sendAndReceive(jsonObject)).toJavaList(Integer.class);
            return new WindowSize(result.get(0), result.get(1));
        }
        return null;
    }

}
