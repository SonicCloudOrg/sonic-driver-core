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
package org.cloud.sonic.driver.ios.enums;

public enum AuthResource {
    CONTACTS(1),
    CALENDAR(2),
    REMINDERS(3),
    PHOTOS(4),
    MICROPHONE(5),
    CAMERA(6),
    MEDIA_LIBRARY(7),
    HOME_KIT(8),
    SYSTEM_ROOT_DIRECTORY(0x40000000),
    USER_DESKTOP_DIRECTORY(0x40000001),
    USER_DOWNLOADS_DIRECTORY(0x40000002),
    USER_DOCUMENTS_DIRECTORY(0x40000003),
    BLUETOOTH(-0x40000000),
    KEYBOARD_NETWORK(-0x40000001),
    LOCATION(-0x40000002),
    HEALTH(-0x40000003);

    private final int resource;

    AuthResource(int resource) {
        this.resource = resource;
    }

    public int getResource() {
        return resource;
    }
}
