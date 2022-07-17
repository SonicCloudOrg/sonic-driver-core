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
package org.cloud.sonic.core.ios.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.core.ios.models.BaseResp;
import org.cloud.sonic.core.ios.models.SessionInfo;
import org.cloud.sonic.core.ios.RespHandler;
import org.cloud.sonic.core.ios.service.WdaClient;
import org.cloud.sonic.core.tool.SonicRespException;

@Slf4j
public class WdaClientImpl implements WdaClient {
    private String remoteUrl;
    private String sessionId;

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void newSession(JSONObject capabilities) throws SonicRespException {
        JSONObject data = new JSONObject();
        data.put("capabilities", capabilities);
        BaseResp b = RespHandler.getResp(HttpUtil.createPost(remoteUrl + "/session").body(data.toJSONString()));
        if (b.getErr() == null) {
            SessionInfo sessionInfo = JSON.parseObject(b.getValue().toString(), SessionInfo.class);
            setSessionId(sessionInfo.getSessionId());
            log.info("start session successful!");
        } else {
            log.error("start session failed.");
            throw new SonicRespException(b.getErr().getMessage());
        }
    }

    public void closeSession() throws SonicRespException {
        RespHandler.getResp(HttpUtil.createRequest(Method.DELETE, remoteUrl + "/session/" + sessionId));
        log.info("close session successful!");
    }

    @Override
    public void tap(int x, int y) throws SonicRespException {
        if (sessionId != null) {
            JSONObject data = new JSONObject();
            data.put("x", (float) x);
            data.put("y", (float) y);
            BaseResp b = RespHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/wda/tap/0")
                    .body(data.toJSONString()));
            if (b.getErr() == null) {
                log.info("tap {} {}.", x, y);
            } else {
                log.error("tap failed.");
                throw new SonicRespException(b.getErr().getMessage());
            }
        } else {
            log.error("sessionId not found.");
            throw new SonicRespException("sessionId not found.");
        }
    }

    @Override
    public void touchAndHold(int x, int y, int second) throws SonicRespException {
        if (sessionId != null) {
            JSONObject data = new JSONObject();
            data.put("x", (float) x);
            data.put("y", (float) y);
            data.put("duration", (float) second);
            BaseResp b = RespHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/wda/touchAndHold")
                    .body(data.toJSONString()));
            if (b.getErr() == null) {
                log.info("touch {} {} and hold {}ms.", x, y, second);
            } else {
                log.error("touch and hold failed.");
                throw new SonicRespException(b.getErr().getMessage());
            }
        } else {
            log.error("sessionId not found.");
            throw new SonicRespException("sessionId not found.");
        }
    }

    public void swipe(int fromX, int fromY, int toX, int toY) throws SonicRespException {
        if (sessionId != null) {
            JSONObject data = new JSONObject();
            data.put("fromX", (float) fromX);
            data.put("fromY", (float) fromY);
            data.put("toX", (float) toX);
            data.put("toY", (float) toY);
            data.put("duration", 0);
            BaseResp b = RespHandler.getResp(HttpUtil.createPost(remoteUrl + "/session/" + sessionId + "/wda/dragfromtoforduration")
                    .body(data.toJSONString()));
            if (b.getErr() == null) {
                log.info("swipe {} {} to {} {}.", fromX, fromY, toX, toY);
            } else {
                log.error("swipe failed.");
                throw new SonicRespException(b.getErr().getMessage());
            }
        } else {
            log.error("sessionId not found.");
            throw new SonicRespException("sessionId not found.");
        }
    }


}
