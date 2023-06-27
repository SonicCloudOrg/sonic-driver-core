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
package org.cloud.sonic.driver.ios;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.driver.common.enums.PasteboardType;
import org.cloud.sonic.driver.common.models.ElementRect;
import org.cloud.sonic.driver.common.models.WindowSize;
import org.cloud.sonic.driver.common.tool.SonicRespException;
import org.cloud.sonic.driver.ios.enums.*;
import org.cloud.sonic.driver.ios.models.TouchActions;
import org.cloud.sonic.driver.ios.service.IOSElement;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RunWith(Parameterized.class)
public class IOSDriverTest {
    static IOSDriver iosDriver;
    static String url = "http://localhost:8100";

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
            new IOSDriver(url, null);
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals("'capabilities' is mandatory to create a new session", e.getMessage());
        }
        Assert.assertTrue(hasThrow);
        iosDriver = new IOSDriver(url, new JSONObject());
        iosDriver.disableLog();
        iosDriver.showLog();
        Assert.assertEquals(url, iosDriver.getWdaClient().getRemoteUrl());
        Assert.assertTrue(iosDriver.getSessionId().length() > 0);
        iosDriver.closeDriver();

        iosDriver = new IOSDriver(url);
        Assert.assertEquals(url, iosDriver.getWdaClient().getRemoteUrl());
        Assert.assertTrue(iosDriver.getSessionId().length() > 0);
    }

    @Test
    public void testApp() throws SonicRespException {
        iosDriver.appActivate("developer.apple.wwdc-Release");
        Boolean hasThrow = false;
        try {
            iosDriver.appActivate("");
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals("bundleId not found.", e.getMessage());
        }
        Assert.assertTrue(hasThrow);
        hasThrow = false;
        try {
            iosDriver.appActivate(null);
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals("bundleId not found.", e.getMessage());
        }
        Assert.assertTrue(hasThrow);
        iosDriver.appRunBackground(5);
        Assert.assertTrue(iosDriver.appTerminate("developer.apple.wwdc-Release"));
        Assert.assertFalse(iosDriver.appTerminate("developer.apple.wwdc-Release"));
        iosDriver.appAuthReset(AuthResource.CAMERA);
    }

    @Test
    public void testSiriAndSendKeys() throws SonicRespException, InterruptedException {
        iosDriver.sendSiriCommand("打开提醒事项");
        Boolean hasThrow = false;
        try {
            iosDriver.sendSiriCommand("");
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals("siri command is null!", e.getMessage());
        }
        Assert.assertTrue(hasThrow);
        hasThrow = false;
        try {
            iosDriver.sendSiriCommand(null);
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals("siri command is null!", e.getMessage());
        }
        Assert.assertTrue(hasThrow);
        Thread.sleep(4000);
        iosDriver.tap(150, 181);
        iosDriver.sendKeys("中文123");
        iosDriver.sendKeys(TextKey.DELETE);
        iosDriver.sendKeys(TextKey.BACK_SPACE);
        iosDriver.pressButton(SystemButton.HOME);
    }

    @Test
    public void testPasteboard() throws SonicRespException, InterruptedException {
        iosDriver.pressButton(SystemButton.HOME);
        String text = UUID.randomUUID() + "中文";
        iosDriver.appActivate("com.apple.springboard");
        iosDriver.findElement(IOSSelector.ACCESSIBILITY_ID, "WebDriverAgentRunner-Runner").click();
        iosDriver.setPasteboard(PasteboardType.PLAIN_TEXT, text);
        Thread.sleep(1000);
        Assert.assertEquals(text, new String(iosDriver.getPasteboard(PasteboardType.PLAIN_TEXT), StandardCharsets.UTF_8));
        iosDriver.pressButton(SystemButton.HOME);
    }

    @Test
    public void testSwipe() throws SonicRespException, InterruptedException {
        iosDriver.swipe(100, 256, 50, 256);
        Thread.sleep(500);
        iosDriver.swipe(100, 600, 100, 200);
    }

    @Test
    public void testSwipeWithTime() throws SonicRespException, InterruptedException {
        iosDriver.swipe(100, 600, 100, 200, 2000);
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
        Thread.sleep(1500);
        iosDriver.performTouchAction(new TouchActions().press(50, 256).wait(50).move(100, 256).wait(10).release());
    }

    @Test
    public void testPressButton() throws SonicRespException, InterruptedException {
        iosDriver.pressButton(SystemButton.HOME);
        Thread.sleep(1000);
        iosDriver.pressButton(SystemButton.VOLUME_DOWN);
        Thread.sleep(1000);
        iosDriver.pressButton(SystemButton.VOLUME_UP);
        Thread.sleep(1000);
        iosDriver.pressButton("home");
    }

    @Test
    public void testGetPageSource() throws SonicRespException {
        Assert.assertTrue(iosDriver.getPageSource().contains("XCUIElementTypeApplication"));
    }

    @Test
    public void testLock() throws SonicRespException {
        iosDriver.lock();
        Assert.assertTrue(iosDriver.isLocked());
        iosDriver.unlock();
        Assert.assertFalse(iosDriver.isLocked());
    }

    @Test
    public void testSession() {
        String sessionId = iosDriver.getSessionId();
        iosDriver.getWdaClient().setSessionId(null);
        Boolean hasThrow = false;
        try {
            iosDriver.getWdaClient().lock();
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals("sessionId not found.", e.getMessage());
        }
        Assert.assertTrue(hasThrow);
        iosDriver.getWdaClient().setSessionId("");
        hasThrow = false;
        try {
            iosDriver.getWdaClient().lock();
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertEquals("sessionId not found.", e.getMessage());
        }
        Assert.assertTrue(hasThrow);
        iosDriver.getWdaClient().setSessionId(sessionId);
    }

    @Test
    public void testFindElement() throws SonicRespException, InterruptedException, IOException {
        Boolean hasThrow = false;
        try {
            iosDriver.findElement("accessibility id", "地图1").click();
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertTrue(e.getMessage().contains("unable to find an element"));
        }
        Assert.assertTrue(hasThrow);
        iosDriver.pressButton(SystemButton.HOME);
        Thread.sleep(2000);
        iosDriver.findElement("accessibility id", "地图").click();
        iosDriver.findElement(XCUIElementType.ANY);
        Thread.sleep(2000);
        iosDriver.pressButton(SystemButton.HOME);
        Thread.sleep(2000);
        iosDriver.findElement(IOSSelector.ACCESSIBILITY_ID, "地图").click();
        Thread.sleep(2000);
        iosDriver.findElement(IOSSelector.ACCESSIBILITY_ID, "搜索地点或地址").click();
        IOSElement w = iosDriver.findElement(IOSSelector.ACCESSIBILITY_ID, "搜索地点或地址");
        String text = UUID.randomUUID().toString().substring(0, 6) + "中文";
        w.sendKeys(text);
        Assert.assertEquals(text, w.getText());
        w.clear();
        Assert.assertEquals("搜索地点或地址", w.getAttribute("name"));
        Assert.assertEquals("搜索地点或地址", w.getText());
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
        iosDriver.findElement(IOSSelector.ACCESSIBILITY_ID, "取消").click();
        iosDriver.setDefaultFindElementInterval(null, 3000);
        iosDriver.setDefaultFindElementInterval(5, null);
        iosDriver.setDefaultFindElementInterval(null, null);
        iosDriver.pressButton(SystemButton.HOME);
    }

    @Test
    public void testFindElementList() throws SonicRespException {
        int eleSize = iosDriver.findElementList(XCUIElementType.WINDOW).size();
        Assert.assertEquals(eleSize, iosDriver.findElementList("class name", "XCUIElementTypeWindow").size());
        Assert.assertEquals(eleSize, iosDriver.findElementList(IOSSelector.CLASS_NAME, "XCUIElementTypeWindow").size());
    }

    @Test
    public void testScreenshot() throws IOException, SonicRespException {
        byte[] bt = iosDriver.screenshot();
        File output = new File("./" + UUID.randomUUID() + ".png");
        FileImageOutputStream imageOutput = new FileImageOutputStream(output);
        imageOutput.write(bt, 0, bt.length);
        imageOutput.close();
        output.delete();
    }

    @Test
    public void testGetWindowSize() throws SonicRespException {
        WindowSize size = iosDriver.getWindowSize();
        Assert.assertNotNull(size);
        Assert.assertTrue(size.getHeight() > 0);
        Assert.assertTrue(size.getWidth() > 0);
    }

    @Test
    public void testSetAppiumSettings() throws SonicRespException {
        iosDriver.setAppiumSettings(new JSONObject());
    }

    @Test
    public void testIsDisplayed() throws SonicRespException {
        String value = "name CONTAINS 'QDII' AND label CONTAINS 'QDII' AND enabled == true AND visible == true";
        IOSElement element1 = iosDriver.findElement(IOSSelector.PREDICATE, value);
        System.out.println(element1.getUniquelyIdentifies() + ",isDisplayed=" + element1.isDisplayed());
        System.out.println(element1.getUniquelyIdentifies() + ",rect=" + element1.getRect());

        IOSElement element2 = iosDriver.findElement(IOSSelector.ACCESSIBILITY_ID, "QDII");
        System.out.println(element2.getUniquelyIdentifies() + ",isDisplayed=" + element2.isDisplayed());
        System.out.println(element2.getUniquelyIdentifies() + ",rect=" + element2.getRect());
    }

    @AfterClass
    public static void after() throws SonicRespException {
        iosDriver.closeDriver();
    }
}
