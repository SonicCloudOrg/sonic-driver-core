package org.cloud.sonic.core.ios;

import org.cloud.sonic.core.tool.SonicRespException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IOSDriverTest {
    IOSDriver iosDriver;

    @Before
    public void before() {
        try {
            iosDriver = new IOSDriver("http://localhost:8100");
            Assert.assertEquals("http://localhost:8100", iosDriver.wdaClient.getRemoteUrl());
        } catch (SonicRespException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSwipe() {
        for (int i = 0; i < 100; i++) {
            try {
                iosDriver.swipe(50, 256, 100, 256);
                iosDriver.swipe(100, 256, 50, 256);
            } catch (SonicRespException e) {
                e.printStackTrace();
            }
        }
    }

    @After
    public void after() {
        iosDriver.closeDriver();
    }
}
