package org.cloud.sonic.driver.poco;

import com.alibaba.fastjson2.JSON;
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
    }

    @Test
    public void testPageSource() throws SonicRespException {
        Assert.assertTrue(pocoDriver.getPageSource().getPayload().getType().equals("Root"));
    }

    @AfterClass
    public static void afterClass() {
        pocoDriver.closeDriver();
    }
}
