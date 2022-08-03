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
