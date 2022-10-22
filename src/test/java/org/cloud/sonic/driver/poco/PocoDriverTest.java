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
        Assert.assertTrue(pocoDriver.getPageSourceForJson().toJSONString().length() > 0);
        Assert.assertNotNull(pocoDriver.getPageSourceForXmlElement().data());
    }

    @Test
    public void testFindElement() throws SonicRespException {
//        String expression = "poco(type=\"Text\",text=\"Start\")";
        String expression = "Root > children > MEHolo > children > AnchorManager";
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
        JSONObject r = pocoDriver.getPageSourceForJson();
        pocoDriver.freezeSource();
        Assert.assertEquals(r, pocoDriver.getPageSourceForJson());
        pocoDriver.thawSource();
    }

    @Test
    public void testWindowsSize() throws SonicRespException {
        WindowSize windowSize = pocoDriver.getScreenSize();
        Assert.assertTrue(windowSize.getHeight() > 0);
        Assert.assertTrue(windowSize.getWidth() > 0);
    }

    @AfterClass
    public static void afterClass() {
        pocoDriver.closeDriver();
    }
}
