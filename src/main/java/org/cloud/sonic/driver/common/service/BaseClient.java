package org.cloud.sonic.driver.common.service;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.driver.ios.models.TouchActions;
import org.cloud.sonic.driver.ios.models.WindowSize;
import org.cloud.sonic.driver.ios.service.WebElement;
import org.cloud.sonic.driver.tool.Logger;
import org.cloud.sonic.driver.tool.RespHandler;
import org.cloud.sonic.driver.tool.SonicRespException;

import java.util.List;

public interface BaseClient {
    //Client Setting
    void setGlobalTimeOut(int timeOut);

    RespHandler getRespHandler();

    void setRespHandler(RespHandler respHandler);

    Logger getLogger();

    void showLog();

    void disableLog();

    //Session handler.
    String getRemoteUrl();

    void setRemoteUrl(String remoteUrl);

    String getSessionId();

    void setSessionId(String sessionId);

    void newSession(JSONObject capabilities) throws SonicRespException;

    void closeSession() throws SonicRespException;

    void checkSessionId() throws SonicRespException;

    //window handler.
    WindowSize getWindowSize() throws SonicRespException;

    //lock handler.
    boolean isLocked() throws SonicRespException;

    void lock() throws SonicRespException;

    void unlock() throws SonicRespException;

    //perform handler.
    void performTouchAction(TouchActions touchActions) throws SonicRespException;

    //button handler.
    void pressButton(String buttonName) throws SonicRespException;

    //keyboard handler.
    void sendKeys(String text, Integer frequency) throws SonicRespException;

    void setPasteboard(String contentType, String content) throws SonicRespException;

    byte[] getPasteboard(String contentType) throws SonicRespException;

    //source handler.
    String pageSource() throws SonicRespException;

    //siri handler.
    void sendSiriCommand(String command) throws SonicRespException;

    //app handler.
    void appActivate(String bundleId) throws SonicRespException;

    boolean appTerminate(String bundleId) throws SonicRespException;

    void appRunBackground(int duration) throws SonicRespException;

    void appAuthReset(int resource) throws SonicRespException;

    //element handler.
    void setDefaultFindElementInterval(Integer retry, Integer interval);

    WebElement findElement(String selector, String value, Integer retry, Integer interval) throws SonicRespException;

    List<WebElement> findElementList(String selector, String value, Integer retry, Integer interval) throws SonicRespException;

    //screen handler.
    byte[] screenshot() throws SonicRespException;

    //appium setting handler.
    void setAppiumSettings(JSONObject settings) throws SonicRespException;
}
