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
package org.cloud.sonic.driver.poco.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.underscore.U;
import org.cloud.sonic.driver.common.models.WindowSize;
import org.cloud.sonic.driver.common.tool.Logger;
import org.cloud.sonic.driver.common.tool.SonicRespException;
import org.cloud.sonic.driver.poco.enums.PocoEngine;
import org.cloud.sonic.driver.poco.models.PocoElement;
import org.cloud.sonic.driver.poco.service.PocoClient;
import org.cloud.sonic.driver.poco.service.PocoConnection;
import org.cloud.sonic.driver.poco.util.pocoJsonToXml;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.*;

import static org.cloud.sonic.driver.poco.enums.PocoEngine.*;

public class PocoClientImpl implements PocoClient {
    private Logger logger;
    private PocoConnection pocoConnection;
    private PocoEngine engine;
    private String source;

    private Element rootXmlNode;
    private boolean isFrozen = false;

    public PocoClientImpl() {
        logger = new Logger();
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public void showLog() {
        logger.showLog();
    }

    @Override
    public void disableLog() {
        logger.disableLog();
    }

    @Override
    public void newClient(PocoEngine engine, int port) {
        this.engine = engine;
        if (engine.equals(COCOS_2DX_JS) || engine.equals(COCOS_CREATOR) || engine.equals(EGRET)) {
            pocoConnection = new WebSocketClientImpl(port, logger);
        } else {
            pocoConnection = new SocketClientImpl(port, logger);
        }
        pocoConnection.connect();
    }

    @Override
    public void closeClient() {
        pocoConnection.disconnect();
    }

    @Override
    public PocoElement pageSource() throws SonicRespException {
        PocoElement pocoElement = new PocoElement(pageSourceForXmlElement());
        return pocoElement;
    }

    @Override
    public String pageSourceForJsonString() throws SonicRespException {
        if (isFrozen) {
            return source;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jsonrpc", "2.0");
        jsonObject.put("params", Arrays.asList(true));
        jsonObject.put("id", UUID.randomUUID().toString());
        jsonObject.put("method", "Dump");
        if (engine.equals(COCOS_CREATOR) || engine.equals(COCOS_2DX_JS)) {
            jsonObject.put("method", "dump");
        }
        source = pocoConnection.sendAndReceive(jsonObject);
        return source;
    }

    @Override
    public Element pageSourceForXmlElement() throws SonicRespException {
        pageSourceForJsonString();
        String pocoJson = "{\"Root\""+source.substring("{\"result\"".length());
        rootXmlNode = Jsoup.parse(pocoJsonToXml.jsonToXml(pocoJson, U.Mode.FORCE_ATTRIBUTE_USAGE,
                "result"), "", Parser.xmlParser());
        return rootXmlNode;
    }

    @Override
    public PocoElement findElement(String expression) throws SonicRespException {
        List<PocoElement> pocoElements = findElements(expression);
        return pocoElements.size() <= 0 ? null : findElements(expression).get(0);
    }

    @Override
    public List<PocoElement> findElements(String expression) throws SonicRespException {
        if (rootXmlNode == null) {
            pageSourceForXmlElement();
        }
        Elements xmlNodes = rootXmlNode.selectXpath(expression);
        List<PocoElement> result = new ArrayList<>();
        for (Element node : xmlNodes) {
            PocoElement pocoElement = new PocoElement(rootXmlNode, node);
            result.add(pocoElement);
        }
        return result;
    }

    private List<PocoElement> parseAttr(PocoElement pocoElement, String express) {
        String attrExpression = express.substring(express.indexOf("(") + 1, express.indexOf(")"));
        if (attrExpression.startsWith("\"") && attrExpression.endsWith("\"")) {
            attrExpression = "name=" + attrExpression.replace("\"", "");
        }
        String[] attrs = attrExpression.split(",");
        return findElementsByAttr(pocoElement, new ArrayList<>(), attrs);
    }

    private List<PocoElement> findElementsByAttr(PocoElement sourceElement, List<PocoElement> result, String[] attrs) {
        Boolean predicate = null;
        for (String attr : attrs) {
            boolean p;
            String field = attr.substring(0, attr.indexOf("="));
            String value = attr.substring(attr.indexOf("=") + 1).replace("\"", "");
            switch (field) {
                case "name":
                    p = value.equals(sourceElement.getPayload().getName());
                    break;
                case "text":
                    p = value.equals(sourceElement.getPayload().getText());
                    break;
                case "type":
                    p = value.equals(sourceElement.getPayload().getType());
                    break;
                case "layer":
                    p = value.equals(sourceElement.getPayload().getLayer());
                    break;
                case "tag":
                    p = value.equals(sourceElement.getPayload().getTag());
                    break;
                case "texture":
                    p = value.equals(sourceElement.getPayload().getTexture());
                    break;
                case "_instanceId":
                    p = value.equals(sourceElement.getPayload().get_instanceId().toString());
                    break;
                case "_ilayer":
                    p = value.equals(sourceElement.getPayload().get_ilayer().toString());
                    break;
                case "visible":
                    p = value.toLowerCase(Locale.ROOT).equals(sourceElement.getPayload().getVisible().toString());
                    break;
                case "clickable":
                    p = value.toLowerCase(Locale.ROOT).equals(sourceElement.getPayload().getClickable().toString());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + field);
            }
            if (predicate == null) {
                predicate = p;
            } else {
                predicate = predicate & p;
            }
        }
        if (predicate != null && predicate) {
            result.add(sourceElement);
        }
        List<PocoElement> children = sourceElement.getChildren();
        if (children != null && children.size() > 0) {
            for (PocoElement child : children) {
                result.addAll(findElementsByAttr(child, result, attrs));
            }
        }
        return result;
    }

    @Override
    public void freezeSource() {
        isFrozen = true;
    }

    @Override
    public void thawSource() {
        isFrozen = false;
    }

    @Override
    public WindowSize getScreenSize() throws SonicRespException {
        if (engine.equals(UNITY_3D) || engine.equals(COCOS_2DX_LUA)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("jsonrpc", "2.0");
            jsonObject.put("params", Arrays.asList(true));
            jsonObject.put("id", UUID.randomUUID().toString());
            jsonObject.put("method", "GetScreenSize");
            List<Integer> result = ((JSONArray) JSONObject.parseObject(
                    pocoConnection.sendAndReceive(jsonObject))
                    .get("result"))
                    .toJavaList(Integer.class);
            return new WindowSize(result.get(0), result.get(1));
        }
        return null;
    }

}
