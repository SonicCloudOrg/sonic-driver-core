package org.cloud.sonic.driver.ios;

import cn.hutool.http.HttpUtil;
import org.cloud.sonic.driver.tool.SonicRespException;
import org.junit.Assert;
import org.junit.Test;

public class RespHandlerTest {
    @Test
    public void testTimeOut() {
        RespHandler respHandler = new RespHandler();
        respHandler.setRequestTimeOut(0);
        Boolean hasThrow = false;
        try {
            respHandler.getResp(HttpUtil.createGet("http://localhost:1234"));
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertTrue(e.getMessage().length() > 0);
        }
        Assert.assertTrue(hasThrow);
    }
}
