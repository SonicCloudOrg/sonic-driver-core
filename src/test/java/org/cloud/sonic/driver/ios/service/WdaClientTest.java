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
package org.cloud.sonic.driver.ios.service;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.driver.tool.RespHandler;
import org.cloud.sonic.driver.ios.enums.IOSSelector;
import org.cloud.sonic.driver.common.models.BaseResp;
import org.cloud.sonic.driver.common.models.ErrorMsg;
import org.cloud.sonic.driver.ios.enums.PasteboardType;
import org.cloud.sonic.driver.ios.models.TouchActions;
import org.cloud.sonic.driver.ios.service.impl.WdaClientImpl;
import org.cloud.sonic.driver.tool.SonicRespException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

public class WdaClientTest {
    static WdaClient wdaClient;
    private static final String ERROR_MSG = "err message";
    static final String SONIC_REMOTE_URL = "http://SONIC_REMOTE_TEST_URL";
    static String url = "http://localhost:8100";

    @BeforeClass
    public static void before() throws Exception {
        if (!SONIC_REMOTE_URL.contains("SONIC_REMOTE_TEST")) {
            url = SONIC_REMOTE_URL;
        }
        wdaClient = new WdaClientImpl();
        wdaClient.setSessionId("test");
        wdaClient.setRemoteUrl(url);
        RespHandler respHandler = Mockito.mock(RespHandler.class);
        BaseResp b = new BaseResp();
        b.setErr(new ErrorMsg("testErr", ERROR_MSG, "traceback"));
        Assert.assertNull(b.getSessionId());
        Mockito.when(respHandler.getResp(Mockito.any())).thenReturn(b);
        Mockito.when(respHandler.getResp(Mockito.any(), Mockito.anyInt())).thenReturn(b);
        Field respField = wdaClient.getClass().getDeclaredField("respHandler");
        respField.setAccessible(true);
        respField.set(wdaClient, respHandler);
    }

    @Test
    public void testGetWindowSize() {
        Boolean hasThrow = false;
        try {
            wdaClient.getWindowSize();
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals(e.getMessage(), ERROR_MSG);
        }
        Assert.assertTrue(hasThrow);
    }

    @Test
    public void testLocked() {
        Boolean hasThrow = false;
        try {
            wdaClient.isLocked();
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals(e.getMessage(), ERROR_MSG);
        }
        Assert.assertTrue(hasThrow);
    }

    @Test
    public void testLock() {
        Boolean hasThrow = false;
        try {
            wdaClient.lock();
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals(e.getMessage(), ERROR_MSG);
        }
        Assert.assertTrue(hasThrow);
    }

    @Test
    public void testUnLock() {
        Boolean hasThrow = false;
        try {
            wdaClient.unlock();
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals(e.getMessage(), ERROR_MSG);
        }
        Assert.assertTrue(hasThrow);
    }

    @Test
    public void testPerformTouchAction() {
        Boolean hasThrow = false;
        try {
            wdaClient.performTouchAction(new TouchActions());
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals(e.getMessage(), ERROR_MSG);
        }
        Assert.assertTrue(hasThrow);
    }

    @Test
    public void testSendKeys() {
        Boolean hasThrow = false;
        try {
            wdaClient.sendKeys("test", 1);
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals(e.getMessage(), ERROR_MSG);
        }
        Assert.assertTrue(hasThrow);
    }

    @Test
    public void testSetPasteboard() {
        Boolean hasThrow = false;
        try {
            wdaClient.setPasteboard(PasteboardType.PLAIN_TEXT.getType(), "text");
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals(e.getMessage(), ERROR_MSG);
        }
        Assert.assertTrue(hasThrow);
    }

    @Test
    public void testGetPasteboard() {
        Boolean hasThrow = false;
        try {
            wdaClient.getPasteboard(PasteboardType.PLAIN_TEXT.getType());
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals(e.getMessage(), ERROR_MSG);
        }
        Assert.assertTrue(hasThrow);
    }

    @Test
    public void testPageSource() {
        Boolean hasThrow = false;
        try {
            wdaClient.pageSource();
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals(e.getMessage(), ERROR_MSG);
        }
        Assert.assertTrue(hasThrow);
    }

    @Test
    public void testPressButton() {
        Boolean hasThrow = false;
        try {
            wdaClient.pressButton("home");
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals(e.getMessage(), ERROR_MSG);
        }
        Assert.assertTrue(hasThrow);
    }

    @Test
    public void testSiriCommand() {
        Boolean hasThrow = false;
        try {
            wdaClient.sendSiriCommand("home");
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals(e.getMessage(), ERROR_MSG);
        }
        Assert.assertTrue(hasThrow);
    }

    @Test
    public void testAppActivate() {
        Boolean hasThrow = false;
        try {
            wdaClient.appActivate("developer.apple.wwdc-Release");
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals(e.getMessage(), ERROR_MSG);
        }
        Assert.assertTrue(hasThrow);
    }

    @Test
    public void testAppTerminate() {
        Boolean hasThrow = false;
        try {
            wdaClient.appTerminate("developer.apple.wwdc-Release");
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals(e.getMessage(), ERROR_MSG);
        }
        Assert.assertTrue(hasThrow);
    }

    @Test
    public void testAppRunBackground() {
        Boolean hasThrow = false;
        try {
            wdaClient.appRunBackground(10);
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals(e.getMessage(), ERROR_MSG);
        }
        Assert.assertTrue(hasThrow);
    }

    @Test
    public void testAppAuthReset() {
        Boolean hasThrow = false;
        try {
            wdaClient.appAuthReset(6);
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals(e.getMessage(), ERROR_MSG);
        }
        Assert.assertTrue(hasThrow);
    }

    @Test
    public void testFindElement() {
        Boolean hasThrow = false;
        try {
            wdaClient.findElement(IOSSelector.ACCESSIBILITY_ID.getSelector(), "abc", 10, 10);
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals(e.getMessage(), ERROR_MSG);
        }
        Assert.assertTrue(hasThrow);
    }

    @Test
    public void testFindElements() {
        Boolean hasThrow = false;
        try {
            wdaClient.findElementList(IOSSelector.ACCESSIBILITY_ID.getSelector(), "abc", 10, 10);
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals(e.getMessage(), ERROR_MSG);
        }
        Assert.assertTrue(hasThrow);
    }

    @Test
    public void testSetAppiumSettings() {
        Boolean hasThrow = false;
        try {
            wdaClient.setAppiumSettings(new JSONObject());
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals(e.getMessage(), ERROR_MSG);
        }
        Assert.assertTrue(hasThrow);
    }

    @Test
    public void testScreenShot() {
        Boolean hasThrow = false;
        try {
            wdaClient.screenshot();
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals(e.getMessage(), ERROR_MSG);
        }
        Assert.assertTrue(hasThrow);
    }
}
