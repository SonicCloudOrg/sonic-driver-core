package org.cloud.sonic.driver.common.models;

import org.cloud.sonic.driver.common.tool.SonicRespException;

public interface BaseElement {
    ElementRect getRect() throws SonicRespException;

    String getAttribute(String name) throws SonicRespException;

    // the xpath or id or csspath...
    String getUniquelyIdentifies() throws SonicRespException;

//    List<BaseElement> getChildren() throws SonicRespException;
    /**
     * Is this element displayed or not?
     * This method avoids the problem of having to parse an element's "style" attribute.
     *
     * @return whether the element is displayed
     */
    boolean isDisplayed() throws SonicRespException;
}
