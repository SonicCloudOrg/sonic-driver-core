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
package org.cloud.sonic.driver.poco.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.driver.common.tool.SonicRespException;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Map;
import java.util.regex.Pattern;

public class pocoJsonToXml {
    /**
     * convert json to xml
     *
     * @param jo JSONObject
     * @return xml
     */
    public static String jsonObjToXml(JSONObject jo) throws SonicRespException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + jsonToXml(jo, "");
        return xml;
    }

    /**
     * json object to xml
     *
     * @param jo JSONObject
     * @param gt "\n" shifter
     * @return XML
     */
    @SuppressWarnings("rawtypes")
    private static String jsonToXml(JSONObject jo, String gt) throws SonicRespException {
        StringBuffer xmlStr = new StringBuffer();
        try {
            JSONObject payload = jo.getJSONObject("payload");
            JSONArray children = jo.getJSONArray("children");

            xmlStr.append(gt);
            xmlStr.append("<");
            String name = String.join("__", payload.get("name").toString().split(" "));

            if (name.equals("<Root>")) name = "Root";
            if (!checkName(name)) {
                name = "invalidName";
            }
            xmlStr.append(name);
            xmlStr.append(getAttrStr(payload));
            xmlStr.append(">\n");
            if (children != null) {
                for (int i = 0; i < children.size(); i++) {
                    JSONObject child = children.getJSONObject(i);
                    xmlStr.append(jsonToXml(child, gt + "\t"));
                }
            }
            addTag(xmlStr, gt, name, true);
        } catch (Exception e) {
            throw new SonicRespException(e.getMessage());
        }
        return xmlStr.toString();
    }

    public static void addTag(StringBuffer xmlStr, String gt, String tagName, boolean isItTheEnd) {
        xmlStr.append(gt);
        if (isItTheEnd) {
            xmlStr.append("</");
        } else {
            xmlStr.append("<");
        }
        xmlStr.append(tagName);
        xmlStr.append(">\n");
    }

    public static StringBuilder getAttrStr(JSONObject payload) {
        StringBuilder sb = new StringBuilder();
        sb.append(" ");
        for (Map.Entry<String, Object> stringObjectEntry : payload.entrySet()) {
            Map.Entry entry = (Map.Entry) stringObjectEntry;
            String key = entry.getKey().toString();
            String val = entry.getValue().toString();
            if (key.equals("zOrders")) {
                JSONObject zOrders = JSONObject.parseObject(val);
                sb.append(String.format("global=\"%s\" ", zOrders.get("global")));
                sb.append(String.format("local=\"%s\" ", zOrders.get("local")));
            } else if (key.equals("components")) {
                val = val.replace("\"", "");
                sb.append(String.format("%s=\"%s\" ", key, val));
            } else {
                val = escapingSpecialCharacters(val);
                sb.append(String.format("%s=\"%s\" ", key, val));
            }
        }
        return sb;
    }

    private static boolean checkName(String name) {
        String re = "^[:A-Z_a-z\\u00C0\\u00D6\\u00D8-\\u00F6\\u00F8-\\u02ff\\u0370-\\u037d"
                + "\\u037f-\\u1fff\\u200c\\u200d\\u2070-\\u218f\\u2c00-\\u2fef\\u3001-\\ud7ff"
                + "\\uf900-\\ufdcf\\ufdf0-\\ufffd\\x10000-\\xEFFFF]"
                + "[:A-Z_a-z\\u00C0\\u00D6\\u00D8-\\u00F6"
                + "\\u00F8-\\u02ff\\u0370-\\u037d\\u037f-\\u1fff\\u200c\\u200d\\u2070-\\u218f"
                + "\\u2c00-\\u2fef\\u3001-\\udfff\\uf900-\\ufdcf\\ufdf0-\\ufffd\\-\\.0-9"
                + "\\u00b7\\u0300-\\u036f\\u203f-\\u2040]*\\Z";
        return name.matches(re);
    }

    private static String escapingSpecialCharacters(String originStr) {
        if (originStr == null) return null;
        originStr = originStr.replace("&", "&amp;");
        originStr = originStr.replace("<", "&lt;");
        originStr = originStr.replace(">", "&gt;");
        originStr = originStr.replace("\"", "&quot;");
        originStr = originStr.replace("'", "&apos;");
        return originStr;
    }
}
