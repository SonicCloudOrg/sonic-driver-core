package org.cloud.sonic.driver.webview;

import org.cloud.sonic.driver.common.tool.SonicRespException;
import org.junit.Test;

public class WebViewDriverTest {
    static WebViewDriver webViewDriver;
    static String url = "ws://localhost:1234/devtools/page/2CFE3A51A00E0A8DCAFB3A2D3E190209";

    @Test
    public void testDriver() throws SonicRespException {
        webViewDriver = new WebViewDriver(url);
        System.out.println(webViewDriver.getPageSource());
    }
}
