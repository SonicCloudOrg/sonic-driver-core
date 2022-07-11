package org.cloud.sonic.core.ios;

public class IOSDriver {
    WDAClient wdaClient = new WDAClient();

    public IOSDriver(String url, int mjpegPort) {
        wdaClient.setRemoteUrl(url);
        wdaClient.newSession();
    }

    public static void main(String[] args) {
        new IOSDriver("http://localhost:8100", 9100);
    }
}
