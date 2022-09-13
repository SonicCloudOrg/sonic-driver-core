package org.cloud.sonic.driver.android;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.driver.common.tool.SonicRespException;
import org.cloud.sonic.driver.ios.enums.AuthResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class AndroidDriverTest {
    static AndroidDriver androidDriver;
    static final String SONIC_REMOTE_URL = "http://SONIC_REMOTE_TEST_URL";
    static String url = "http://localhost:6790";

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[1][0];
    }

    @Before
    public void before() throws InterruptedException {
        Thread.sleep(2000);
    }

    @BeforeClass
    public static void beforeClass() throws SonicRespException {
        if (!SONIC_REMOTE_URL.contains("SONIC_REMOTE_TEST")) {
            url = SONIC_REMOTE_URL;
        }
        Boolean hasThrow = false;
        try {
            new AndroidDriver(url, null);
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals("io.appium.uiautomator2.common.exceptions.InvalidArgumentException: 'capabilities' are mandatory for session creation", e.getMessage());
        }
        Assert.assertTrue(hasThrow);
        androidDriver = new AndroidDriver(url, new JSONObject());
        androidDriver.disableLog();
        Assert.assertEquals(url, androidDriver.getUiaClient().getRemoteUrl());
        Assert.assertTrue(androidDriver.getSessionId().length() > 0);
        androidDriver.closeDriver();

        androidDriver = new AndroidDriver(url);
        Assert.assertEquals(url, androidDriver.getUiaClient().getRemoteUrl());
        Assert.assertTrue(androidDriver.getSessionId().length() > 0);
    }

    @Test
    public void testApp() throws SonicRespException {
        androidDriver.appActivate("developer.apple.wwdc-Release");
        Boolean hasThrow = false;
        try {
            androidDriver.appActivate("");
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals("bundleId not found.", e.getMessage());
        }
        Assert.assertTrue(hasThrow);
        hasThrow = false;
        try {
            androidDriver.appActivate(null);
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals("bundleId not found.", e.getMessage());
        }
        Assert.assertTrue(hasThrow);
        androidDriver.appRunBackground(5);
        Assert.assertTrue(androidDriver.appTerminate("developer.apple.wwdc-Release"));
        Assert.assertFalse(androidDriver.appTerminate("developer.apple.wwdc-Release"));
        androidDriver.appAuthReset(AuthResource.CAMERA);
    }
}
