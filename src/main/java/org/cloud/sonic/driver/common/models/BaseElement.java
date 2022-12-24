package org.cloud.sonic.driver.common.models;

import org.cloud.sonic.driver.common.tool.SonicRespException;

public interface BaseElement {
    ElementRect getRect() throws SonicRespException;
    String getAttribute(String name) throws SonicRespException;
    // the xpath or id or csspath...
    String getUniquelyIdentifies() throws SonicRespException;

//    List<BaseElement> getChildren() throws SonicRespException;
}
