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
