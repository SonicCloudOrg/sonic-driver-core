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
import org.cloud.sonic.driver.poco.models.PocoElement;
import org.cloud.sonic.driver.poco.service.PocoConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    public JSONObject sendAndReceive(JSONObject jsonObject) throws SonicRespException {
        int len = jsonObject.toJSONString().length();
        ByteBuffer header = ByteBuffer.allocate(4);
        header.put(intToByteArray(len), 0, 4);
        header.flip();
        ByteBuffer body = ByteBuffer.allocate(len);
        body.put(jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8), 0, len);
        body.flip();
        ByteBuffer total = ByteBuffer.allocate(len + 4);
        total.put(header.array());
        total.put(body.array());
        total.flip();
        synchronized (SocketClientImpl.class) {
            try {
                outputStream.write(total.array());
                byte[] head = new byte[4];
                inputStream.read(head);
                int headLen = toInt(head);
                StringBuilder s = new StringBuilder();
                while (poco.isConnected() && !Thread.interrupted()) {
                    byte[] buffer = new byte[1024];
                    int realLen;
                    realLen = inputStream.read(buffer);
                    if (buffer.length != realLen && realLen >= 0) {
                        buffer = subByteArray(buffer, 0, realLen);
                    }
                    if (realLen >= 0) {
                        s.append(new String(buffer));
                        if (s.toString().getBytes(StandardCharsets.UTF_8).length == headLen) {
                            JSONObject re = JSON.parseObject(s.toString());
                            logger.info(re.toJSONString());
                            if (re.getString("id").equals(jsonObject.getString("id"))) {
                                return re.getJSONObject("result");
                            } else {
                                throw new SonicRespException("id not found!");
                            }
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

    private byte[] subByteArray(byte[] byte1, int start, int end) {
        byte[] byte2;
        byte2 = new byte[end - start];
        System.arraycopy(byte1, start, byte2, 0, end - start);
        return byte2;
    }

    private int toInt(byte[] b) {
        int res = 0;
        for (int i = 0; i < b.length; i++) {
            res += (b[i] & 0xff) << (i * 8);
        }
        return res;
    }
}