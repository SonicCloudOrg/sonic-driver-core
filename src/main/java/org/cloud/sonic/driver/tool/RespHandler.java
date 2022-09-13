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
package org.cloud.sonic.driver.tool;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.driver.android.service.UiaClient;
import org.cloud.sonic.driver.common.models.BaseResp;
import org.cloud.sonic.driver.common.models.ErrorMsg;
import org.cloud.sonic.driver.ios.service.WdaClient;
import org.cloud.sonic.driver.tool.SonicRespException;

import java.util.HashMap;
import java.util.Map;

public class RespHandler {
    public static final int DEFAULT_REQUEST_TIMEOUT = 15000;
    private int requestTimeout = 15000;

    public void setRequestTimeOut(int timeOut) {
        requestTimeout = timeOut;
    }

    public BaseResp getResp(HttpRequest httpRequest) throws SonicRespException {
        return getResp(httpRequest, requestTimeout);
    }

    public BaseResp getResp(HttpRequest httpRequest, int timeout) throws SonicRespException {
        synchronized (this) {
            try {
                return initResp(httpRequest.addHeaders(initHeader()).timeout(timeout).execute().body());
            } catch (HttpException | IORuntimeException e) {
                throw new SonicRespException(e.getMessage());
            }
        }
    }

    public BaseResp initResp(String response) {
        if (response.contains("traceback") || response.contains("stacktrace")) {
            return initErrorMsg(response.replace("stacktrace","traceback"));
        } else {
            return JSON.parseObject(response, BaseResp.class);
        }
    }

    public Map<String, String> initHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        return headers;
    }

    public BaseResp initErrorMsg(String resp) {
        BaseResp err = JSON.parseObject(resp, BaseResp.class);
        ErrorMsg errorMsg = JSONObject.parseObject(err.getValue().toString(), ErrorMsg.class);
        err.setErr(errorMsg);
        err.setValue(null);
        return err;
    }
}
