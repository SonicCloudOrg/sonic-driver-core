package org.cloud.sonic.driver.android;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.driver.android.enmus.AndroidSelector;
import org.cloud.sonic.driver.android.service.AndroidElement;
import org.cloud.sonic.driver.common.enums.PasteboardType;
import org.cloud.sonic.driver.common.models.ElementRect;
import org.cloud.sonic.driver.common.models.WindowSize;
import org.cloud.sonic.driver.common.tool.SonicRespException;
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
    public void testSession() {
        String sessionId = androidDriver.getSessionId();
        androidDriver.getUiaClient().setSessionId(null);
        Boolean hasThrow = false;
        try {
            androidDriver.getUiaClient().pageSource();
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals("sessionId not found.", e.getMessage());
        }
        Assert.assertTrue(hasThrow);
        androidDriver.getUiaClient().setSessionId("");
        hasThrow = false;
        try {
            androidDriver.getUiaClient().pageSource();
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals("sessionId not found.", e.getMessage());
        }
        Assert.assertTrue(hasThrow);
        androidDriver.getUiaClient().setSessionId(sessionId);
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
        androidDriver.getPasteboard(PasteboardType.PLAIN_TEXT);
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
        AndroidElement w = androidDriver.findElement(AndroidSelector.XPATH, "//*[@text='标题']");
        w.click();
        String text = UUID.randomUUID().toString().substring(0, 6) + "中文";
        w.sendKeys(text);
        w.sendKeys(text, true);
        Assert.assertEquals(text, w.getText());
        w.clear();
        androidDriver.setDefaultFindElementInterval(null, 3000);
        androidDriver.setDefaultFindElementInterval(5, null);
        androidDriver.setDefaultFindElementInterval(null, null);
        ElementRect elementRect = w.getRect();
        Assert.assertTrue(elementRect.getX() > 0);
        Assert.assertTrue(elementRect.getY() > 0);
        Assert.assertTrue(elementRect.getWidth() > 0);
        Assert.assertTrue(elementRect.getHeight() > 0);
        Assert.assertTrue(elementRect.getCenter().getX() > 0);
        Assert.assertTrue(elementRect.getCenter().getY() > 0);
        byte[] bt = w.screenshot();
        File output = new File("./" + UUID.randomUUID() + ".png");
        FileImageOutputStream imageOutput = new FileImageOutputStream(output);
        imageOutput.write(bt, 0, bt.length);
        imageOutput.close();
        output.delete();
        Assert.assertEquals("android.widget.EditText", w.getAttribute("class"));
    }

    @Test
    public void testFindElementList() throws SonicRespException {
        int eleSize = androidDriver.findElementList("id", "android:id/content").size();
        Assert.assertEquals(eleSize, androidDriver.findElementList(AndroidSelector.Id, "android:id/content").size());
    }

    @Test
    public void testScreenshot() throws IOException, SonicRespException {
        byte[] bt = androidDriver.screenshot();
        File output = new File("./" + UUID.randomUUID() + ".png");
        FileImageOutputStream imageOutput = new FileImageOutputStream(output);
        imageOutput.write(bt, 0, bt.length);
        imageOutput.close();
        output.delete();
    }

    @Test
    public void testSetAppiumSettings() throws SonicRespException {
        androidDriver.setAppiumSettings(new JSONObject());
    }

    @Test
    public void testSwipeAction() throws SonicRespException {
        // 默认滑动操作的完成时间，500毫秒
        androidDriver.swipe(540, 1710, 540, 200);
        // 指定滑动操作在1000毫秒内完成
        androidDriver.swipe(540, 1710, 540, 200, 1000);
    }

    @Test
    public void testTapAction() throws SonicRespException {
        // 替换为任意待测试的元素
        AndroidElement androidElement = androidDriver.findElement(AndroidSelector.Id,
                "com.xueqiu.android:id/my_groups_new_title_bar_ding");
        ElementRect elementRect = androidElement.getRect();
        androidDriver.tap(elementRect.getX() + elementRect.getWidth() / 2, elementRect.getY() + elementRect.getHeight() / 2);
    }

    @Test
    public void testLongPressAction() throws SonicRespException {
        // 替换为任意待测试的元素
        AndroidElement androidElement = androidDriver.findElement(AndroidSelector.Id,
                "com.xueqiu.android:id/my_groups_list_item_stock_name_label");
        ElementRect elementRect = androidElement.getRect();
        androidDriver.longPress((double) elementRect.getX() + (double) elementRect.getWidth() / 2,
                (double) elementRect.getY() + (double) elementRect.getHeight() / 2,
                100);
    }

}
