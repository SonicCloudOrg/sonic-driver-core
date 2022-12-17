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
package org.cloud.sonic.driver.webview.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.driver.common.tool.Logger;
import org.cloud.sonic.driver.common.tool.SonicRespException;
import org.cloud.sonic.driver.poco.service.impl.WebSocketClientImpl;
import org.cloud.sonic.driver.webview.service.CdpClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class CdpClientImpl implements CdpClient {
    private Logger logger;
    private String result = null;

    private org.java_websocket.client.WebSocketClient webSocketClient;

    public CdpClientImpl() {
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
    public void newClient(String url) {
        connect(url);
    }

    @Override
    public void closeClient() {
        disconnect();
    }

    @Override
    public String pageSource() throws SonicRespException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        jsonObject.put("method", "Runtime.evaluate");
        JSONObject params = new JSONObject();
        params.put("expression", "window.location.toString()");
        jsonObject.put("params", params);
        return sendAndReceive(jsonObject);
    }

    private String sendAndReceive(JSONObject jsonObject) throws SonicRespException {
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
                String re = result.toString();
                result = null;
                return re;
            } else {
                throw new SonicRespException("result not found!");
            }
        }
    }

    private void connect(String url) {
        URI ws = null;
        try {
            ws = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        webSocketClient = new org.java_websocket.client.WebSocketClient(ws) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                logger.info("cdp ws connected.");
            }

            @Override
            public void onMessage(String s) {
                logger.info(s);
                result = s;
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                logger.info("cdp ws close.");
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

    private void disconnect() {
        webSocketClient.close();
    }
}
