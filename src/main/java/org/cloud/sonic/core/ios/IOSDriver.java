package org.cloud.sonic.core.ios;

import org.cloud.sonic.core.tool.SonicRespException;

public class IOSDriver {
    WDAClient wdaClient = new WDAClient();

    public IOSDriver(String url) throws SonicRespException {
        wdaClient.setRemoteUrl(url);
        wdaClient.newSession();
    }
}
