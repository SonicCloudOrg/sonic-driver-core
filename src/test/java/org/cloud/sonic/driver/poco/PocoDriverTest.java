package org.cloud.sonic.driver.poco;

import org.cloud.sonic.driver.common.models.WindowSize;
import org.cloud.sonic.driver.common.tool.SonicRespException;
import org.cloud.sonic.driver.poco.enums.PocoEngine;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class PocoDriverTest {
    static PocoDriver pocoDriver;

    @BeforeClass
    public static void beforeClass() {
        pocoDriver = new PocoDriver(PocoEngine.UNITY_3D);
    }

    @Test
    public void testPageSource() throws SonicRespException {
        Assert.assertTrue(pocoDriver.getPageSource().getPayload().getType().equals("Root"));
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
