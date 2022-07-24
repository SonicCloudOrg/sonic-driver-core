package org.cloud.sonic.core.ios;

import cn.hutool.http.HttpUtil;
import org.cloud.sonic.core.tool.SonicRespException;
import org.junit.Assert;
import org.junit.Test;

public class RespHandlerTest {
    @Test
    public void testTimeOut(){
        RespHandler respHandler = new RespHandler();
        respHandler.setRequestTimeOut(1);
        try {
            respHandler.getResp(HttpUtil.createGet("localhost:1234"));
        }catch (SonicRespException e){
            Assert.assertTrue(e.getMessage().contains("connect timed out"));
        }
    }
}
