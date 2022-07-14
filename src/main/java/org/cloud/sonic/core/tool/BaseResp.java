package org.cloud.sonic.core.tool;

public class BaseResp<T> {

    private String sessionId;
    private ErrorMsg err;
    private T value;

    public BaseResp(T value) {
        this.value = value;
    }

    public BaseResp(boolean success, T value) {
        this.value = value;
    }

    public BaseResp(ErrorMsg err) {
        this.err = err;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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
