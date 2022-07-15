package org.cloud.sonic.core.ios.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMsg {
    private String error;
    private String message;
    private String traceback;
}
