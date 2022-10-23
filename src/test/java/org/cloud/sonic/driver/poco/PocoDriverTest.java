package org.cloud.sonic.driver.poco;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.driver.common.models.WindowSize;
import org.cloud.sonic.driver.common.tool.SonicRespException;
import org.cloud.sonic.driver.poco.enums.PocoEngine;
import org.cloud.sonic.driver.poco.models.PocoElement;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class PocoDriverTest {
    static PocoDriver pocoDriver;

    @BeforeClass
    public static void beforeClass() {
        pocoDriver = new PocoDriver(PocoEngine.UNITY_3D);
        pocoDriver.disableLog();
        pocoDriver.showLog();
    }

    @Test
    public void testPageSource() throws SonicRespException {
        Assert.assertEquals("Root", pocoDriver.getPageSource().getPayload().getType());
        Assert.assertTrue(pocoDriver.getPageSourceForJsonString().length() > 0);
        Assert.assertNotNull(pocoDriver.getPageSourceForXmlElement().toString());
        System.out.println(pocoDriver.getPageSourceForXmlElement().toString());
    }

    @Test
    public void testFindElement() throws SonicRespException {
//        String expression = "poco(type=\"Text\",text=\"Start\")";
//        String expression = "Root > children > MEHolo > children > AnchorManager";
        String expression = "//*[@text=\"Start\" and @type=\"Text\"]";
        List<PocoElement> pocoElements = pocoDriver.findElements(expression);
        for (PocoElement pocoElement:pocoElements){
            Assert.assertNotNull(pocoElement.getPayload().getName());
            System.out.println(pocoElement.getPayload().getName());
            Assert.assertNotNull(pocoElement.currentNodeSelector);
            System.out.println(pocoElement.currentNodeSelector);
        }
    }

    @Test
    public void testFreeze() throws SonicRespException {
        String r = pocoDriver.getPageSourceForJsonString();
        pocoDriver.freezeSource();
        try {
            // change page tree operation
            Thread.sleep(5*1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(r, pocoDriver.getPageSourceForJsonString());
        pocoDriver.thawSource();
    }

    @Test
    public void testWindowsSize() throws SonicRespException {
        WindowSize windowSize = pocoDriver.getScreenSize();
        Assert.assertTrue(windowSize.getHeight() > 0);
        Assert.assertTrue(windowSize.getWidth() > 0);
    }

    @Test
    public void testNodeExist() throws SonicRespException{
        String expression = "Root > children > MEHolo > children > AnchorManager";
        PocoElement pocoElement = pocoDriver.findElement(expression);
        Assert.assertTrue(pocoElement.currentTheNodeExists());
        // mock node does not exist
        pocoElement.currentNodeSelector = "Root > children > MEHolo > children > AnchorManager222";
        Assert.assertTrue(pocoElement.currentTheNodeExists());
    }

    @Test
    public void testGetParent() throws SonicRespException{
        String expression = "Root > children > MEHolo > children > AnchorManager";
        PocoElement pocoElement = pocoDriver.findElement(expression);
        PocoElement parentPocoElement = pocoElement.getParentNode();
        Assert.assertNotNull(parentPocoElement.getPayload().getName());
        System.out.println(parentPocoElement.getPayload().getName());
        Assert.assertNotNull(parentPocoElement.currentNodeSelector);
        System.out.println(parentPocoElement.currentNodeSelector);
    }

    @AfterClass
    public static void afterClass() {
        pocoDriver.closeDriver();
    }
}
