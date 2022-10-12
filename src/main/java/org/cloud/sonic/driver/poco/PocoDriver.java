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
package org.cloud.sonic.driver.poco;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.driver.common.models.WindowSize;
import org.cloud.sonic.driver.common.tool.SonicRespException;
import org.cloud.sonic.driver.poco.enums.PocoEngine;
import org.cloud.sonic.driver.poco.models.PocoElement;
import org.cloud.sonic.driver.poco.service.PocoClient;
import org.cloud.sonic.driver.poco.service.impl.PocoClientImpl;

/**
 * @author Eason
 * poco driver
 */
public class PocoDriver {
    private PocoClient pocoClient;

    /**
     * init poco driver
     *
     * @param pocoEngine
     */
    public PocoDriver(PocoEngine pocoEngine) {
        this(pocoEngine, pocoEngine.getDefaultPort());
    }

    /**
     * init poco driver
     *
     * @param pocoEngine
     * @param port
     */
    public PocoDriver(PocoEngine pocoEngine, int port) {
        pocoClient = new PocoClientImpl();
        pocoClient.newClient(pocoEngine, port);
    }

    /**
     * close driver
     */
    public void closeDriver() {
        pocoClient.closeClient();
    }

    /**
     * show log.
     */
    public void showLog() {
        pocoClient.showLog();
    }

    /**
     * disable log.
     */
    public void disableLog() {
        pocoClient.disableLog();
    }

    /**
     * get Poco element
     *
     * @return
     * @throws SonicRespException
     */
    public PocoElement getPageSource() throws SonicRespException {
        return pocoClient.pageSource();
    }

    /**
     * get Poco element for Json
     *
     * @return
     * @throws SonicRespException
     */
    public JSONObject getPageSourceForJson() throws SonicRespException {
        return pocoClient.pageSourceForJson();
    }

    /**
     * find poco element
     *
     * @param expression
     * @return
     */
    public PocoElement findElement(String expression) throws SonicRespException {
        return pocoClient.findElement(expression);
    }

    /**
     * Freeze page source
     */
    public void freezeSource() {
        pocoClient.freezeSource();
    }

    /**
     * Thaw page source
     */
    public void thawSource() {
        pocoClient.thawSource();
    }

    /**
     * get windows size
     *
     * @return
     * @throws SonicRespException
     */
    public WindowSize getScreenSize() throws SonicRespException {
        return pocoClient.getScreenSize();
    }
}
