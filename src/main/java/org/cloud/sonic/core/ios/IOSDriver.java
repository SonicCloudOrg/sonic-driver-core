package org.cloud.sonic.core.ios;

public class IOSDriver {
    WDAClient wdaClient = new WDAClient();

    public IOSDriver(String url, int mjpegPort) {
        wdaClient.setRemoteUrl(url);

    }
}
