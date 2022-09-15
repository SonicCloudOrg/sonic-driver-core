package org.cloud.sonic.driver.android;

import com.alibaba.fastjson.JSONObject;
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
        androidDriver.closeDriver();
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
        androidDriver.setPasteboard(PasteboardType.PLAIN_TEXT,"abc");
//        System.out.println(androidDriver.getPasteboard(PasteboardType.PLAIN_TEXT));
    }

    @Test
    public void testFindElement() throws SonicRespException, InterruptedException, IOException {
        Boolean hasThrow = false;
        try {
            androidDriver.findElement("id", "android:id/content").click();
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertTrue(e.getMessage().contains("unable to find an element"));
        }
        Assert.assertTrue(hasThrow);
        Thread.sleep(2000);
        androidDriver.findElement("id", "android:id/content").click();
//        iosDriver.findElement(XCUIElementType.ANY);
//        Thread.sleep(2000);
//        iosDriver.pressButton(SystemButton.HOME);
//        Thread.sleep(2000);
//        iosDriver.findElement(IOSSelector.ACCESSIBILITY_ID, "地图").click();
//        Thread.sleep(2000);
//        iosDriver.findElement(IOSSelector.ACCESSIBILITY_ID, "搜索地点或地址").click();
//        WebElement w = iosDriver.findElement(IOSSelector.ACCESSIBILITY_ID, "搜索地点或地址");
//        String text = UUID.randomUUID().toString().substring(0, 6) + "中文";
//        w.sendKeys(text);
//        Assert.assertEquals(text, w.getText());
//        w.clear();
//        Assert.assertEquals("搜索地点或地址", w.getText());
//        IOSRect iosRect = w.getRect();
//        Assert.assertTrue(iosRect.getX() > 0);
//        Assert.assertTrue(iosRect.getY() > 0);
//        Assert.assertTrue(iosRect.getWidth() > 0);
//        Assert.assertTrue(iosRect.getHeight() > 0);
//        Assert.assertTrue(iosRect.getCenter().getX() > 0);
//        Assert.assertTrue(iosRect.getCenter().getY() > 0);
//        byte[] bt = w.screenshot();
//        File output = new File("./" + UUID.randomUUID() + ".png");
//        FileImageOutputStream imageOutput = new FileImageOutputStream(output);
//        imageOutput.write(bt, 0, bt.length);
//        imageOutput.close();
//        output.delete();
//        iosDriver.findElement(IOSSelector.ACCESSIBILITY_ID, "取消").click();
//        iosDriver.setDefaultFindElementInterval(null, 3000);
//        iosDriver.setDefaultFindElementInterval(5, null);
//        iosDriver.setDefaultFindElementInterval(null, null);
//        iosDriver.pressButton(SystemButton.HOME);
    }
}
