package org.cloud.sonic.core.ios;

import org.cloud.sonic.core.tool.SonicRespException;
import org.junit.Assert;
import org.junit.Test;

public class IOSDriverTest {
    @Test
    public void testInit() {
        try {
            IOSDriver iosDriver = new IOSDriver("http://localhost:8100");
            Assert.assertEquals("http://localhost:8100", iosDriver.wdaClient.getRemoteUrl());
        } catch (SonicRespException e) {
            e.printStackTrace();
        }
    }
}
