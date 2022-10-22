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
import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.driver.common.tool.Logger;
import org.cloud.sonic.driver.common.tool.SonicRespException;
import org.cloud.sonic.driver.poco.service.PocoConnection;
import org.cloud.sonic.driver.poco.util.PocoTool;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class WebSocketClientImpl implements PocoConnection {
    private int port;
    private Logger logger;

    private org.java_websocket.client.WebSocketClient webSocketClient;
    private String result = null;

    public WebSocketClientImpl(int port, Logger logger) {
        this.port = port;
        this.logger = logger;
    }

    @Override
    public String sendAndReceive(JSONObject jsonObject) throws SonicRespException {
        synchronized (WebSocketClientImpl.class) {
            webSocketClient.send(jsonObject.toString());
            int wait = 0;
            while (result == null) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wait++;
                if (wait >= 20) {
                    break;
                }
            }
            if (result != null) {
                int subStartIndex = result.indexOf("result");

                String pocoPrefix = result.substring(subStartIndex) + "}";

                if (PocoTool.checkPocoRpcResultID(pocoPrefix, jsonObject.getString("id"))) {
                    return "{" + result.substring(subStartIndex);
                } else {
                    throw new SonicRespException("id not found!");
                }
//                JSONObject re = JSON.parseObject(result);
//                result = null;
//                if (re.getString("id").equals(jsonObject.getString("id"))) {
//                    return re.get("result");
//                } else {
//                    throw new SonicRespException("id not found!");
//                }
            }
        }
        return null;
    }

    @Override
    public void connect() {
        URI ws = null;
        try {
            ws = new URI("ws://localhost:" + port);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        webSocketClient = new org.java_websocket.client.WebSocketClient(ws) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                logger.info("poco ws connected.");
            }

            @Override
            public void onMessage(String s) {
                logger.info(s);
                result = s;
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                logger.info("poco ws close.");
            }

            @Override
            public void onError(Exception e) {

            }
        };
        webSocketClient.connect();
        int waitConnect = 0;
        while (!webSocketClient.isOpen()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            waitConnect++;
            if (waitConnect >= 20) {
                break;
            }
        }
    }

    @Override
    public void disconnect() {
        webSocketClient.close();
    }
}
