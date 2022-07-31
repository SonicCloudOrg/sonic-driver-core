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
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.core.ios.models.BaseResp;
import org.cloud.sonic.core.ios.service.WdaClient;
import org.cloud.sonic.core.ios.service.WebElement;
import org.cloud.sonic.core.tool.SonicRespException;

@Slf4j
public class WebElementImpl implements WebElement {
    private String id;
    private WdaClient wdaClient;

    public WebElementImpl(String id, WdaClient wdaClient) {
        this.id = id;
        this.wdaClient = wdaClient;
    }

    @Override
    public void click() throws SonicRespException {
        wdaClient.checkSessionId();
        BaseResp b = wdaClient.getRespHandler().getResp(
                HttpUtil.createPost(wdaClient.getRemoteUrl() + "/session/"
                        + wdaClient.getSessionId() + "/element/" + id + "/click"));
        if (b.getErr() == null) {
            log.info("click element {}.", id);
        } else {
            log.error("click element {} failed.", id);
            throw new SonicRespException(b.getErr().getMessage());
        }
    }

    @Override
    public void sendKeys() throws SonicRespException {

    }
}
