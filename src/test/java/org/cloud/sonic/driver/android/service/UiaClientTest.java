package org.cloud.sonic.driver.android.service;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.driver.android.service.impl.UiaClientImpl;
import org.cloud.sonic.driver.common.enums.PasteboardType;
import org.cloud.sonic.driver.common.models.BaseResp;
import org.cloud.sonic.driver.common.models.ErrorMsg;
import org.cloud.sonic.driver.common.tool.RespHandler;
import org.cloud.sonic.driver.common.tool.SonicRespException;
import org.cloud.sonic.driver.ios.enums.IOSSelector;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

public class UiaClientTest {
    static UiaClient uiaClient;
    private static final String ERROR_MSG = "err message";
    static String url = "http://localhost:6790";

    @BeforeClass
    public static void before() throws Exception {
        uiaClient = new UiaClientImpl();
        uiaClient.setSessionId("test");
        uiaClient.setRemoteUrl(url);
        RespHandler respHandler = Mockito.mock(RespHandler.class);
        BaseResp b = new BaseResp();
        b.setErr(new ErrorMsg("testErr", ERROR_MSG, "traceback"));
        Assert.assertNull(b.getSessionId());
        Mockito.when(respHandler.getResp(Mockito.any())).thenReturn(b);
        Mockito.when(respHandler.getResp(Mockito.any(), Mockito.anyInt())).thenReturn(b);
        Field respField = uiaClient.getClass().getDeclaredField("respHandler");
        respField.setAccessible(true);
        respField.set(uiaClient, respHandler);
    }

    @Test
    public void testGetWindowSize() {
        Boolean hasThrow = false;
        try {
            uiaClient.getWindowSize();
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
            uiaClient.sendKeys("test", false);
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
            uiaClient.setPasteboard(PasteboardType.PLAIN_TEXT.getType(), "text");
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
            uiaClient.getPasteboard(PasteboardType.PLAIN_TEXT.getType());
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
            uiaClient.pageSource();
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
            uiaClient.findElement(IOSSelector.ACCESSIBILITY_ID.getSelector(), "abc", 10, 10);
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
            uiaClient.findElementList(IOSSelector.ACCESSIBILITY_ID.getSelector(), "abc", 10, 10);
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
            uiaClient.setAppiumSettings(new JSONObject());
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
            uiaClient.screenshot();
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals(e.getMessage(), ERROR_MSG);
        }
        Assert.assertTrue(hasThrow);
    }
}
