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
package org.cloud.sonic.core.ios.models;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class TouchActions {

    private List<TouchAction> actions;

    @Getter
    @ToString
    public class TouchAction {
        private String action;
        private Options options;

        @Getter
        @ToString
        public class Options {
            private Integer x;
            private Integer y;
            private Integer ms;
        }

        public TouchAction(ActionType actionType){
            this.action = actionType.getType();
            this.options = new Options();
        }
    }

    public TouchActions(){
        actions = new ArrayList<>();
    }

    public TouchActions press(int x, int y) {
        TouchAction touchAction = new TouchAction(ActionType.PRESS);
        touchAction.options.x = x;
        touchAction.options.y = y;
        actions.add(touchAction);
        return this;
    }

    public TouchActions wait(int ms) {
        TouchAction touchAction = new TouchAction(ActionType.WAIT);
        touchAction.options.ms = ms;
        actions.add(touchAction);
        return this;
    }

    public TouchActions move(int x, int y) {
        TouchAction touchAction = new TouchAction(ActionType.MOVE);
        touchAction.options.x = x;
        touchAction.options.y = y;
        actions.add(touchAction);
        return this;
    }

    public TouchActions release() {
        TouchAction touchAction = new TouchAction(ActionType.RELEASE);
        touchAction.options = null;
        actions.add(touchAction);
        return this;
    }
}
