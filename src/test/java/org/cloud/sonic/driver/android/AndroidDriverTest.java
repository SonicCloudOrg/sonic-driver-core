package org.cloud.sonic.driver.android;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.driver.common.tool.SonicRespException;
import org.cloud.sonic.driver.ios.enums.AuthResource;
import org.cloud.sonic.driver.ios.enums.SystemButton;
import org.cloud.sonic.driver.ios.models.TouchActions;
import org.junit.*;
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
    }

    @AfterClass
    public static void afterClass() throws SonicRespException {
        androidDriver.closeDriver();
    }

    @Test
    public void testSource() throws SonicRespException {
        Assert.assertTrue(androidDriver.getPageSource().contains("android.widget.FrameLayout"));
    }

    @Test
    public void testSwipe() throws SonicRespException, InterruptedException {
        androidDriver.swipe(100, 256, 50, 256);
        Thread.sleep(500);
        androidDriver.swipe(50, 256, 100, 256);
    }

    @Test
    public void testTap() throws SonicRespException, InterruptedException {
        androidDriver.tap(150, 81);
        Thread.sleep(500);
//        androidDriver.pressButton(SystemButton.HOME);
    }

    @Test
    public void testLongPress() throws SonicRespException {
        androidDriver.longPress(150, 281, 1500);
//        androidDriver.pressButton(SystemButton.HOME);
    }

    @Test
    public void testPerformTouchAction() throws SonicRespException, InterruptedException {
        androidDriver.performTouchAction(new TouchActions().press(100, 256).wait(50).move(50, 256).wait(10).release());
        Thread.sleep(1500);
        androidDriver.performTouchAction(new TouchActions().press(50, 256).wait(50).move(100, 256).wait(10).release());
    }
}
