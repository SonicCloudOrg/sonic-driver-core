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
package org.cloud.sonic.core.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private SimpleDateFormat formatter;
    private boolean isShowLog;

    public Logger() {
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        isShowLog = true;
    }

    public void showLog() {
        isShowLog = true;
    }

    public void disableLog() {
        isShowLog = false;
    }

    private void print(String level, String msg, Object... args) {
        if (isShowLog) {
            System.out.println(String.format("[sonic-driver-core] %s [%s] %s",
                    formatter.format(new Date()), level, String.format(msg, args)));
        }
    }

    public void info(String msg, Object... args) {
        print("INFO", msg, args);
    }

    public void error(String msg, Object... args) {
        print("ERROR", msg, args);
    }
}
