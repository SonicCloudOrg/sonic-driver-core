package org.cloud.sonic.core.tool;

public class SonicRespException extends Exception{
    public SonicRespException(String message) {
        super(message);
    }

    public SonicRespException(String message, Throwable cause) {
        super(message, cause);
    }
}
