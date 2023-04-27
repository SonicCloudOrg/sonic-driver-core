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
package org.cloud.sonic.driver.ios.enums;

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
    TOOLBAR_BUTTON("XCUIElementTypeToolbarButton"),
    POPOVER("XCUIElementTypePopover"),
    KEYBOARD("XCUIElementTypeKeyboard"),
    NAVIGATION_BAR("XCUIElementTypeNavigationBar"),
    KEY("XCUIElementTypeKey"),
    STATUS_BAR("XCUIElementTypeStatusBar"),
    SWITCH("XCUIElementTypeSwitch"),
    TOGGLE("XCUIElementTypeToggle"),
    LINK("XCUIElementTypeLink"),
    IMAGE("XCUIElementTypeImage"),
    ICON("XCUIElementTypeIcon"),
    SEARCH_FIELD("XCUIElementTypeSearchField"),
    STATIC_TEXT("XCUIElementTypeStaticText"),
    CHECK_BOX("XCUIElementTypeCheckBox"),
    TEXT_VIEW("XCUIElementTypeTextView"),
    SEGMENTED_CONTROL("XCUIElementTypeSegmentedControl"),
    BROWSER("XCUIElementTypeBrowser"),
    COLLECTION_VIEW("XCUIElementTypeCollectionView"),
    SLIDER("XCUIElementTypeSlider"),
    MAP("XCUIElementTypeMap"),
    WEB_VIEW("XCUIElementTypeWebView"),
    TIME_LINE("XCUIElementTypeTimeline"),
    COLOR_WELL("XCUIElementTypeColorWell"),
    HELP_TAG("XCUIElementTypeHelpTag"),
    MATTE("XCUIElementTypeMatte"),
    DOCK_ITEM("XCUIElementTypeDockItem"),
    GRID("XCUIElementTypeGrid"),
    CELL("XCUIElementTypeCell"),
    HANDLE("XCUIElementTypeHandle"),
    STEPPER("XCUIElementTypeStepper"),
    TAB("XCUIElementTypeTab"),
    TOUCH_BAR("XCUIElementTypeTouchBar"),
    STATUS_ITEM("XCUIElementTypeStatusItem"),

    LAYOUT_AREA("XCUIElementTypeLayoutArea"),
    LAYOUT_ITEM("XCUIElementTypeLayoutItem"),

    RULER("XCUIElementTypeRuler"),
    RULER_MARKER("XCUIElementTypeRulerMarker"),

    RADIO_BUTTON("XCUIElementTypeRadioButton"),
    RADIO_GROUP("XCUIElementTypeRadioGroup"),

    TAB_BAR("XCUIElementTypeTabBar"),
    TAB_GROUP("XCUIElementTypeTabGroup"),

    TABLE("XCUIElementTypeTable"),
    TABLE_ROW("XCUIElementTypeTableRow"),
    TABLE_COLUMN("XCUIElementTypeTableColumn"),

    OUTLINE("XCUIElementTypeOutline"),
    OUTLINE_ROW("XCUIElementTypeOutlineRow"),

    PAGE_INDICATOR("XCUIElementTypePageIndicator"),
    PROGRESS_INDICATOR("XCUIElementTypeProgressIndicator"),
    ACTIVITY_INDICATOR("XCUIElementTypeActivityIndicator"),
    RATING_INDICATOR("XCUIElementTypeRatingIndicator"),
    VALUE_INDICATOR("XCUIElementTypeValueIndicator"),
    RELEVANCE_INDICATOR("XCUIElementTypeRelevanceIndicator"),
    LEVEL_INDICATOR("XCUIElementTypeLevelIndicator"),

    SPLIT_GROUP("XCUIElementTypeSplitGroup"),
    SPLITTER("XCUIElementTypeSplitter"),

    PICKER("XCUIElementTypePicker"),
    PICKER_WHEEL("XCUIElementTypePickerWheel"),
    DATE_PICKER("XCUIElementTypeDatePicker"),

    SCROLL_VIEW("XCUIElementTypeScrollView"),
    SCROLL_BAR("XCUIElementTypeScrollBar"),

    TEXT_FIELD("XCUIElementTypeTextField"),
    SECURE_TEXT_FIELD("XCUIElementTypeSecureTextField"),

    MENU("XCUIElementTypeMenu"),
    MENU_ITEM("XCUIElementTypeMenuItem"),
    MENU_BAR("XCUIElementTypeMenuBar"),
    MENU_BAR_ITEM("XCUIElementTypeMenuBarItem"),

    INCREMENT_ARROW("XCUIElementTypeIncrementArrow"),
    DECREMENT_ARROW("XCUIElementTypeDecrementArrow"),
    ;

    private final String type;

    XCUIElementType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
