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
package org.cloud.sonic.driver.poco.service;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.driver.common.models.WindowSize;
import org.cloud.sonic.driver.common.tool.Logger;
import org.cloud.sonic.driver.common.tool.SonicRespException;
import org.cloud.sonic.driver.poco.enums.PocoEngine;
import org.cloud.sonic.driver.poco.models.PocoElement;
import org.jsoup.nodes.Element;

import java.util.List;

/**
 * @author Eason
 * poco client interface
 */
public interface PocoClient {
    //Client Setting
    Logger getLogger();

    void showLog();

    void disableLog();

    //Client handler.
    void newClient(PocoEngine engine, int port);

    void closeClient();

    //source handler.
    PocoElement pageSource() throws SonicRespException;

    String pageSourceForJsonString() throws SonicRespException;

    Element pageSourceForXmlElement() throws SonicRespException;

    PocoElement findElement(String selector, String expression) throws SonicRespException;

    List<PocoElement> findElements(String selector, String expression) throws SonicRespException;

    void freezeSource();

    void thawSource();

    //other
    WindowSize getScreenSize() throws SonicRespException;
}
