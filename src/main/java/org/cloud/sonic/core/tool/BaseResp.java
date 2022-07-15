package org.cloud.sonic.core.tool;

import lombok.Data;

@Data
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
}
