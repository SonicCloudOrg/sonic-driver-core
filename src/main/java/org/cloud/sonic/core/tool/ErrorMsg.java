package org.cloud.sonic.core.tool;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMsg {
    private String error;
    private String message;
    private String traceback;
}
