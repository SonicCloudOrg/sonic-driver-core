/*
 *  Copyright (C) [SonicCloudOrg] Sonic Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License")),
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
package org.cloud.sonic.core.ios.enums;

public enum XCUIElementType {
    ANY("XCUIElementTypeAny"),
    OTHER("XCUIElementTypeOther"),
    APPLICATION("XCUIElementTypeApplication"),
    GROUP("XCUIElementTypeGroup"),
    WINDOW("XCUIElementTypeWindow"),
    SHEET("XCUIElementTypeSheet"),
    DRAWER("XCUIElementTypeDrawer"),
    ALERT("XCUIElementTypeAlert"),
    DIALOG("XCUIElementTypeDialog"),
    BUTTON("XCUIElementTypeButton"),
    DISCLOSURE_TRIANGLE("XCUIElementTypeDisclosureTriangle"),
    POP_UP_BUTTON("XCUIElementTypePopUpButton"),
    COMBO_BOX("XCUIElementTypeComboBox"),
    MENU_BUTTON("XCUIElementTypeMenuButton"),
    RADIO_BUTTON("XCUIElementTypeRadioButton"),
    RADIO_GROUP("XCUIElementTypeRadioGroup"),
    TOOLBAR_BUTTON("XCUIElementTypeToolbarButton"),
    POPOVER("XCUIElementTypePopover"),
    KEYBOARD("XCUIElementTypeKeyboard"),
    NAVIGATION_BAR("XCUIElementTypeNavigationBar"),
    TAB_BAR("XCUIElementTypeTabBar"),
    TAB_GROUP("XCUIElementTypeTabGroup"),
    KEY("XCUIElementTypeKey"),
    CHECK_BOX("XCUIElementTypeCheckBox");

    private final String type;

    XCUIElementType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
