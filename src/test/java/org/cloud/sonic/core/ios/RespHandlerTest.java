package org.cloud.sonic.core.ios;

import cn.hutool.http.HttpUtil;
import org.cloud.sonic.core.tool.SonicRespException;
import org.junit.Assert;
import org.junit.Test;

public class RespHandlerTest {
    @Test
    public void testTimeOut() {
        RespHandler respHandler = new RespHandler();
        respHandler.setRequestTimeOut(0);
        Boolean hasThrow = false;
        try {
            respHandler.getResp(HttpUtil.createGet("localhost:1234"));
        } catch (Throwable e) {
            hasThrow = true;
            Assert.assertEquals(SonicRespException.class, e.getClass());
            Assert.assertTrue(e.getMessage().contains("connect"));
        }
        Assert.assertTrue(hasThrow);
    }
}
