package org.cloud.sonic.core.ios.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Capabilities {
    private String device;
    private String browserName;
    private String sdkVersion;
    private String CFBundleIdentifier;
}
