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

import org.cloud.sonic.core.tool.SonicRespException;
import org.junit.*;

public class IOSDriverTest {
    static IOSDriver iosDriver;

    @BeforeClass
    public static void before() throws SonicRespException {
        iosDriver = new IOSDriver("http://localhost:8100");
        Assert.assertEquals("http://localhost:8100", iosDriver.getWdaClient().getRemoteUrl());
        Assert.assertTrue(iosDriver.getSessionId().length() > 0);
    }

    @Test
    public void testSwipe() throws SonicRespException {
        iosDriver.swipe(50, 256, 100, 256);
        iosDriver.swipe(100, 256, 50, 256);
    }

    @Test
    public void testTap() throws SonicRespException {
        iosDriver.tap(150, 81);
    }

    @Test
    public void testTouchAndHold() throws SonicRespException {
        iosDriver.touchAndHold(150, 81, 3000);
    }

    @AfterClass
    public static void after() throws SonicRespException {
        iosDriver.closeDriver();
    }
}
