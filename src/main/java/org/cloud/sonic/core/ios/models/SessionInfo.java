package org.cloud.sonic.core.ios.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionInfo {
    private String sessionId;
    private Capabilities capabilities;
}
