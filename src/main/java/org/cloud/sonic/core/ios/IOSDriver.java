package org.cloud.sonic.core.ios;

import org.cloud.sonic.core.tool.SonicRespException;

public class IOSDriver {
    WDAClient wdaClient = new WDAClient();

    public IOSDriver(String url) throws SonicRespException {
        wdaClient.setRemoteUrl(url);
        wdaClient.newSession();
    }

    public void closeDriver() {
        wdaClient.closeSession();
    }

    public void tap(int x, int y) {

    }

    public void swipe(int fromX, int fromY, int toX, int toY) throws SonicRespException {
        wdaClient.swipe(fromX, fromY, toX, toY);
    }
}
