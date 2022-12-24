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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.cloud.sonic.driver.common.models.BaseElement;
import org.cloud.sonic.driver.common.models.ElementRect;
import org.cloud.sonic.driver.common.tool.SonicRespException;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class PocoElement implements BaseElement {
    public String currentNodeSelector = "Root";
    private Payload payload;
    private List<PocoElement> children;
    RootElement rootElement;
    private Element currentNodeXmlElement;

    private long version;

    @Override
    // the poco given pos is a point where the origin of the coordinates will change with
    // the rotation of the screen. The function and others are not uniform, so the implementation
    // is not considered, you can get the pos point through the payload, and then convert it at
    // the application layer
    public ElementRect getRect() throws SonicRespException {
        throw new SonicRespException("poco element unrealized");
    }

    @Override
    public String getAttribute(String name) throws SonicRespException {
        return currentNodeXmlElement.attr(name);
    }

    @Override
    public String getUniquelyIdentifies() throws SonicRespException {
        return currentNodeSelector;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public class Payload {
        private String layer;
        private String name;
        private String tag;
        private String text;
        private String texture;
        private Integer _instanceId;
        private Integer _ilayer;
        private String type;
        private Boolean visible;
        private ZOrders zOrders;
        private List<String> components;
        private List<Float> anchorPoint;
        private List<Float> scale;
        private List<Float> size;
        private List<Float> pos;
        private Boolean clickable;

        @Getter
        @ToString
        @AllArgsConstructor
        public class ZOrders {
            private Float global;
            private Float local;

            public ZOrders() {
            }
        }

        public Payload() {
            zOrders = new ZOrders();
        }
    }

    public PocoElement(RootElement root) {
        this.rootElement = root;
        this.currentNodeSelector = root.getXmlElement().cssSelector();
        payload = new Payload();
        children = new ArrayList<>();
    }

    public PocoElement(RootElement root, Element currentNodeXmlElement) {
        this(root);
        this.currentNodeXmlElement = currentNodeXmlElement;
        this.currentNodeSelector = currentNodeXmlElement.cssSelector();
        parseXmlNode(currentNodeXmlElement);
    }

    public Payload getPayload() {

        if (rootElement.getVersion()!=getVersion()){
            Element xmlPocoNode = rootElement.getXmlElement().select(currentNodeSelector).first();
            if (xmlPocoNode == null) {
                return null;
            }
            parseXmlNode(xmlPocoNode);
            version = rootElement.getVersion();
        }
        return payload;
    }

    public List<PocoElement> getChildren() {

        if (rootElement.getVersion()!=getVersion()){
            Element xmlPocoNode = rootElement.getXmlElement().select(currentNodeSelector).first();

            if (xmlPocoNode == null) {
                return null;
            }

            children.clear();

            currentNodeXmlElement = xmlPocoNode;

            Elements childList = xmlPocoNode.children();

            for (Element child:childList) {
                children.add(new PocoElement(rootElement, child));
            }

            version = rootElement.getVersion();
        }
        return children;
    }


    public void parseXmlNode(Element xmlNode) {

        payload.layer = xmlNode.attr("layer");
        payload.name = xmlNode.attr("name");
        payload.tag = xmlNode.attr("tag");
        payload.text = xmlNode.attr("text");
        payload.texture = xmlNode.attr("texture");

        String _instanceId = xmlNode.attr("_instanceId");
        payload._instanceId = _instanceId.isEmpty()? 0 : Integer.parseInt(_instanceId);

        String _ilayer = xmlNode.attr("_ilayer");
        payload._ilayer = _ilayer.isEmpty() ? 0 : Integer.parseInt(_ilayer);

        payload.type = xmlNode.attr("type");

        String visible = xmlNode.attr("visible");
        payload.visible = Boolean.parseBoolean(visible);

        String global = xmlNode.attr("global");
        payload.zOrders.global = global.isEmpty() ? 0 : Float.parseFloat(global);
        String local = xmlNode.attr("local");
        payload.zOrders.local = local.isEmpty() ? 0 : Float.parseFloat(local);

        String components = xmlNode.attr("components");
        if (components.length() > 2) {
            components = components.substring(1, components.length() - 1);
            payload.components = Arrays.asList(components.split(","));
        }
        payload.anchorPoint = parseFloatAttrList(xmlNode.attr("anchorPoint"));
        payload.scale = parseFloatAttrList(xmlNode.attr("scale"));
        payload.size = parseFloatAttrList(xmlNode.attr("size"));
        payload.pos = parseFloatAttrList(xmlNode.attr("pos"));
        payload.clickable = Boolean.parseBoolean(xmlNode.attr("clickable"));
    }

    private List<Float> parseFloatAttrList(String floatAttrStr) {
        if (floatAttrStr.length() <= 2) return null;
        List<Float> result = new ArrayList<Float>();
        floatAttrStr = floatAttrStr.substring(1, floatAttrStr.length() - 1);
        for (String numStr : floatAttrStr.split(",")) {
            result.add(Float.parseFloat(numStr));
        }
        return result;
    }

    public Boolean currentTheNodeExists(){
        return rootElement.getXmlElement().select(currentNodeSelector).first()!=null;
    }

    public PocoElement getParentNode(){
        Element xmlPocoNode = rootElement.getXmlElement().select(currentNodeSelector).first();

        if (xmlPocoNode == null) {
            return null;
        }
        Element parentNode = xmlPocoNode.parent();
        if (parentNode==null){
            return null;
        }
        return new PocoElement(rootElement,parentNode);
    }
}
