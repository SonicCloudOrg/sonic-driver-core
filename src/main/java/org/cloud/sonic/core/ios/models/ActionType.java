package org.cloud.sonic.core.ios.models;

public enum ActionType {
    PRESS("press"),
    WAIT("wait"),
    MOVE("moveTo"),
    RELEASE("release");

    private final String type;
    ActionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
