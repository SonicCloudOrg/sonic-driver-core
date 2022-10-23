package org.cloud.sonic.driver.poco.models;

import org.jsoup.nodes.Element;

public class RootElement {
    private Element XmlElement;
    private long version;

    public RootElement(){}

    public RootElement(Element XmlElement){
        this.XmlElement = XmlElement;
    }

    public RootElement(Element XmlElement, long version){
        this(XmlElement);
        this.version = version;
    }


    public Element getXmlElement() {
        return XmlElement;
    }

    public void setXmlElement(Element xmlElement) {
        this.XmlElement = xmlElement;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public synchronized void updateVersion(Element rootXmlElement){
        this.XmlElement = rootXmlElement;
        this.version+=1;
    }
}
