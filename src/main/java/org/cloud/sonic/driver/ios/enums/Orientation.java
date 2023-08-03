package org.cloud.sonic.driver.ios.enums;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

public enum Orientation {
    UNKNOWN(0),
    PORTRAIT(1),
    PORTRAITUPSIDEDOWN(2),
    LANDSCAPELEFT(3),
    LANDSCAPERIGHT(4);

    private final int orientation;

    Orientation(int orientation) {
        this.orientation = orientation;
    }

    public JSONObject getValue() {
        JSONObject value = new JSONObject();
        value.put("x",0);
        value.put("y",0);
        switch (this.orientation) {
            case 1:
                value.put("z",0);
                break;
            case 2:
                value.put("z",180);
                break;
            case 3:
                value.put("z",90);
                break;
            case 4:
                value.put("z",270);
                break;
        }

        return value;
    }

}
