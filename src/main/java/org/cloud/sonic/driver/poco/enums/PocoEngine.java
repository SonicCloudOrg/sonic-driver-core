/*
 *  Copyright (C) [SonicCloudOrg] Sonic Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.cloud.sonic.driver.poco.enums;

public enum PocoEngine {
    UNITY_3D("Unity3d", 5001),
    UE4("UE4", 5001),
    COCOS_2DX_JS("Cocos2dx-js", 5003),
    COCOS_2DX_LUA("Cocos2dx-lua", 15004),
    COCOS_2DX_C_PLUS_1("Cocos2dx-c++", 18888),
    COCOS_CREATOR("cocos-creator", 5003),
    EGRET("Egret", 5003);

    private final String name;
    private final int defaultPort;

    PocoEngine(String name, int defaultPort) {
        this.name = name;
        this.defaultPort = defaultPort;
    }

    public int getDefaultPort() {
        return defaultPort;
    }
}
