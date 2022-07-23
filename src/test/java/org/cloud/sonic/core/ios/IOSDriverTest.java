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

import org.cloud.sonic.core.ios.models.SystemButton;
import org.cloud.sonic.core.ios.models.TouchActions;
import org.cloud.sonic.core.tool.SonicRespException;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class IOSDriverTest {
    static IOSDriver iosDriver;

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[200][0];
    }

    @BeforeClass
    public static void beforeClass() throws SonicRespException {
        iosDriver = new IOSDriver("http://localhost:8100");
        Assert.assertEquals("http://localhost:8100", iosDriver.getWdaClient().getRemoteUrl());
        Assert.assertTrue(iosDriver.getSessionId().length() > 0);
    }

    @Before
    public void before() throws InterruptedException {
        Thread.sleep(2000);
    }

    @Test
    public void testSwipe() throws SonicRespException, InterruptedException {
        iosDriver.swipe(100, 256, 50, 256);
        Thread.sleep(500);
        iosDriver.swipe(50, 256, 100, 256);
    }

    @Test
    public void testTap() throws SonicRespException, InterruptedException {
        iosDriver.tap(150, 81);
        Thread.sleep(500);
        iosDriver.pressButton(SystemButton.HOME);
    }

    @Test
    public void testLongPress() throws SonicRespException {
        iosDriver.longPress(150, 281, 1500);
        iosDriver.pressButton(SystemButton.HOME);
    }

    @Test
    public void testPerformTouchAction() throws SonicRespException, InterruptedException {
        iosDriver.performTouchAction(new TouchActions().press(100, 256).wait(50).move(50, 256).wait(10).release());
        Thread.sleep(500);
        iosDriver.performTouchAction(new TouchActions().press(50, 256).wait(50).move(100, 256).wait(10).release());
    }

    @Test
    public void testPressButton() throws SonicRespException, InterruptedException {
        iosDriver.pressButton(SystemButton.HOME);
        Thread.sleep(1000);
        iosDriver.pressButton(SystemButton.VOLUME_DOWN);
        Thread.sleep(1000);
        iosDriver.pressButton(SystemButton.VOLUME_UP);
    }

    @Test
    @Ignore
    public void testSendKeys() throws SonicRespException {
        iosDriver.sendKeys("123", 0);
    }

    @AfterClass
    public static void after() throws SonicRespException {
        iosDriver.closeDriver();
    }
}
