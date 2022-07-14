package org.cloud.sonic.core.tool;

public class ErrorMsg {
    private String error;
    private String message;
    private String traceback;

    public ErrorMsg(){}

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTraceback() {
        return traceback;
    }

    public void setTraceback(String traceback) {
        this.traceback = traceback;
    }
}
