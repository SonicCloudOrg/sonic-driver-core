package org.cloud.sonic.driver.android;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.driver.android.enmus.AndroidSelector;
import org.cloud.sonic.driver.common.service.WebElement;
import org.cloud.sonic.driver.common.tool.SonicRespException;
import org.cloud.sonic.driver.common.models.WindowSize;
import org.cloud.sonic.driver.ios.enums.IOSSelector;
import org.cloud.sonic.driver.ios.enums.PasteboardType;
import org.cloud.sonic.driver.ios.enums.SystemButton;
import org.cloud.sonic.driver.ios.enums.XCUIElementType;
import org.cloud.sonic.driver.ios.models.IOSRect;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

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
        androidDriver.showLog();
    }

    @AfterClass
    public static void afterClass() throws SonicRespException {
//        androidDriver.closeDriver();
    }

    @Test
    public void testSource() throws SonicRespException {
        Assert.assertTrue(androidDriver.getPageSource().contains("android.widget.FrameLayout"));
    }

    @Test
    public void testGetWindowSize() throws SonicRespException {
        WindowSize size = androidDriver.getWindowSize();
        Assert.assertNotNull(size);
        Assert.assertTrue(size.getHeight() > 0);
        Assert.assertTrue(size.getWidth() > 0);
    }

    @Test
    public void testClipboard() throws SonicRespException {
        androidDriver.setPasteboard(PasteboardType.PLAIN_TEXT, "abc");
//        System.out.println(androidDriver.getPasteboard(PasteboardType.PLAIN_TEXT));
    }

    @Test
    public void testFindElement() throws SonicRespException, InterruptedException, IOException {
        Boolean hasThrow = false;
        try {
            androidDriver.findElement("id", "android:id/content1").click();
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertTrue(e.getMessage().contains("An element could not be located on the page using the given search parameters"));
        }
        Assert.assertTrue(hasThrow);
        Thread.sleep(2000);
        androidDriver.findElement(AndroidSelector.Id, "android:id/content").click();
        Thread.sleep(2000);
        WebElement w = androidDriver.findElement(AndroidSelector.XPATH, "//*[@text='标题']");
        w.click();
        w.sendKeys("hello");
        w.clear();
        androidDriver.setDefaultFindElementInterval(null, 3000);
        androidDriver.setDefaultFindElementInterval(5, null);
        androidDriver.setDefaultFindElementInterval(null, null);
    }
}
