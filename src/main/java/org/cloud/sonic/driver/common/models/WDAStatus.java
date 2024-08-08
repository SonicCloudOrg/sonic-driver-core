package org.cloud.sonic.driver.common.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class WDAStatus {
    private WDABuild build;
    
    @Getter
    @ToString
    @AllArgsConstructor
    public static class WDABuild {
    	private String version;
    } // end class
} // end class

/*
References:
https://github.com/SonicCloudOrg/sonic-driver-core/blob/faa0948e11e02be04db3ec9754fb66d613cf8bfa/src/main/java/org/cloud/sonic/driver/ios/service/impl/WdaClientImpl.java#L128
*/
