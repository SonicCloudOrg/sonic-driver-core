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
package org.cloud.sonic.driver.ios.models;

import cn.hutool.core.map.MapUtil;
import lombok.Getter;
import lombok.ToString;
import org.cloud.sonic.driver.ios.enums.ActionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@ToString
public class TouchActions {

    private List<FingerTouchAction> actions;

    @Getter
    @ToString
    public static class FingerTouchAction {
        private final String id;
        private final String type = "pointer";
        private final Map<String, String> parameters = MapUtil.of("pointerType", "touch");
        private final List<TouchAction> actions;

        public FingerTouchAction(String fingerName) {
            this.id = "finger-" + fingerName;
            actions = new ArrayList<>();
        }

        public FingerTouchAction() {
            this("0");
        }

        public FingerTouchAction press(int x, int y) {
            move(x, y);
            TouchAction touchAction = new TouchAction(ActionType.PRESS);
            actions.add(touchAction);
            return this;
        }

        public FingerTouchAction wait(int ms) {
            PauseAction touchAction = new PauseAction();
            touchAction.duration = ms;
            actions.add(touchAction);
            return this;
        }

        public FingerTouchAction move(int x, int y) {
            MoveAction touchAction = new MoveAction();
            touchAction.x = x;
            touchAction.y = y;
            actions.add(touchAction);
            return this;
        }

        public FingerTouchAction release() {
            TouchAction touchAction = new TouchAction(ActionType.RELEASE);
            actions.add(touchAction);
            return this;
        }
    }

    @Getter
    @ToString
    public static class TouchAction {
        private final String type;

        public TouchAction(ActionType actionType) {
            this.type = actionType.getType();
        }
    }

    @Getter
    @ToString(callSuper = true)
    public static class MoveAction extends TouchAction {
        private int x;
        private int y;

        public MoveAction() {
            super(ActionType.MOVE);
        }
    }

    @Getter
    @ToString(callSuper = true)
    public static class PauseAction extends TouchAction {
        private int duration;

        public PauseAction() {
            super(ActionType.WAIT);
        }
    }

    public TouchActions() {
        actions = new ArrayList<>();
    }

    public TouchActions(FingerTouchAction finger) {
        this();
        this.actions.add(finger);
    }

    public FingerTouchAction finger(String name) {
        FingerTouchAction finger = new FingerTouchAction(name);
        actions.add(finger);
        return finger;
    }
}
