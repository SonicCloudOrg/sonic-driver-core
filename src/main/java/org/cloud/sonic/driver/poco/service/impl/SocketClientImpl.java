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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class SocketClientImpl implements PocoConnection {

    private Socket poco = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;

    private int port;
    private Logger logger;

    public SocketClientImpl(int port, Logger logger) {
        this.port = port;
        this.logger = logger;
    }

    @Override
    public Object sendAndReceive(JSONObject jsonObject) throws SonicRespException {
        byte[] data = jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8);
        byte[] header = intToByteArray(data.length);

        synchronized (SocketClientImpl.class) {
            try {
                outputStream.write(header);
                outputStream.write(data);
                outputStream.flush();
                byte[] head = new byte[4];
                byte[] buffer = new byte[8192];
                inputStream.read(head);
                int headLen = toInt(head);
                ByteBuffer rData = ByteBuffer.allocate(headLen);
                while (poco.isConnected() && !Thread.interrupted()) {

                    int realLen;
                    realLen = inputStream.read(buffer);
                    if (realLen > 0 ) {
                        rData.put(buffer, 0, realLen);
                    }

                    if (rData.position() == headLen) {
                        rData.flip();
                        String sData = new String(rData.array(),StandardCharsets.UTF_8 );
                        JSONObject re = JSON.parseObject(sData);
                        logger.info(sData);
                        if (re.getString("id").equals(jsonObject.getString("id"))) {
                            return re.get("result");
                        } else {
                            throw new SonicRespException("id not found!");
                        }
                    }
                }
            } catch (Exception e) {
                throw new SonicRespException(e.getMessage());
            }
        }
        return null;
    }

    @Override
    public void connect() {
        int waitConnect = 0;
        while (poco == null || !poco.isConnected()) {
            try {
                poco = new Socket("localhost", port);
                inputStream = poco.getInputStream();
                outputStream = poco.getOutputStream();
                logger.info("poco socket connected.");
            } catch (Exception e) {
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
        if (poco != null && poco.isConnected()) {
            try {
                poco.close();
                logger.info("poco socket closed.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (inputStream != null) {
            try {
                inputStream.close();
                logger.info("poco input stream closed.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close();
                logger.info("poco output stream closed.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) (i & 0xff);
        result[1] = (byte) (i >> 8 & 0xff);
        result[2] = (byte) (i >> 16 & 0xff);
        result[3] = (byte) (i >> 24 & 0xff);
        return result;
    }

    private int toInt(byte[] b) {
        int res = 0;
        for (int i = 0; i < b.length; i++) {
            res += (b[i] & 0xff) << (i * 8);
        }
        return res;
    }
}
