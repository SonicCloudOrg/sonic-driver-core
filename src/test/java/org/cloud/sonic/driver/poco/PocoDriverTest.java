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
        Assert.assertTrue(pocoDriver.getPageSource().getPayload().getType().equals("Root"));
        Assert.assertTrue(pocoDriver.getPageSourceForJson().toJSONString().length() > 0);
    }

    @Test
    public void testFindElement() throws SonicRespException {
        String expression = "poco(type=\"Text\",text=\"Start\")";
        PocoElement pocoElement = pocoDriver.findElement(expression);
        Assert.assertTrue(pocoElement.getPayload().getType().equals("Text"));
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
