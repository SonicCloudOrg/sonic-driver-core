package org.cloud.sonic.core.tool;

public class BaseResp<T> {

    private String sessionId;
    private boolean success;
    private ErrorMsg err;
    private T value;

    public BaseResp(T value) {
        this.success = true;
        this.value = value;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ErrorMsg getErr() {
        return err;
    }

    public void setErr(ErrorMsg err) {
        this.err = err;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
