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
package org.cloud.sonic.core.ios;

import org.cloud.sonic.core.ios.service.WDAClient;
import org.cloud.sonic.core.ios.service.impl.WDAClientImpl;
import org.cloud.sonic.core.tool.SonicRespException;

public class IOSDriver {
    WDAClient wdaClient = new WDAClientImpl();

    public IOSDriver(String url) throws SonicRespException {
        wdaClient.setRemoteUrl(url);
        wdaClient.newSession();
    }

    public void closeDriver() {
        wdaClient.closeSession();
    }

    public void tap(int x, int y) {

    }

    public void swipe(int fromX, int fromY, int toX, int toY) throws SonicRespException {
        wdaClient.swipe(fromX, fromY, toX, toY);
    }
}
