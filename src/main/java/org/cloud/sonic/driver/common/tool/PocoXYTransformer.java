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
package org.cloud.sonic.driver.common.tool;

/***
 * poco coordinate system conversion and vertical coordinate system converter
 */
public class PocoXYTransformer {
    /**
     * Convert adb vertical coordinates to poco coordinate system coordinates according to different directions
     *
     * @param x           x poco coordinate point x
     * @param y           y poco coordinate point y
     * @param w           w ScreenWidth
     * @param h           h ScreenHeight
     * @param orientation The device orientation, based on the vertical direction of the mobile device,
     *                    rotate 90° counterclockwise, orientation=90, 180° counterclockwise, orientation=180,
     *                    and so on
     * @return {@link int[]}
     */
    public static double[] PocoTransformerVertical(double x, double y, double w, double h, Integer orientation) {
        if (orientation == 90) {
            double temp = x;
            x = w - y;
            y = temp;
        } else if (orientation == 180) {
            x = w - x;
            y = h - y;
        } else if (orientation == 270) {
            double temp = x;
            x = y;
            y = h - temp;
        }
        return new double[]{x, y};
    }

    /**
     * Convert the coordinate system of poco to the coordinates in the adb vertical coordinate system
     *
     * @param x           x vertical coordinate x
     * @param y           y vertical coordinate y
     * @param w           w ScreenWidth
     * @param h           h ScreenHeight
     * @param orientation The device orientation, based on the vertical direction of the mobile device,
     *                    rotate 90° counterclockwise, orientation=90, 180° counterclockwise, orientation=180,
     *                    and so on
     * @return {@link int[]}
     */
    public static double[] VerticalTransformerPoco(double x, double y, double w, double h, Integer orientation) {
        if (orientation == 90) {
            double temp = x;
            x = y;
            y = w - temp;
        } else if (orientation == 180) {
            x = w - x;
            y = h - y;
        } else if (orientation == 270) {
            double temp = x;
            x = h - y;
            y = temp;
        }
        return new double[]{x, y};
    }
}
