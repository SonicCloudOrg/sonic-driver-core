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

import java.util.Map;

public class pocoJsonToXml {
    /**
     * convert json to xml
     *
     * @param jo JSONObject
     *
     * @return xml
     */
    public static String jsonObjToXml(JSONObject jo){
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+ jsonToXml(jo, "");
        return xml;
    }

    /**
     * json object to xml
     *
     * @param jo JSONObject
     * @param gt "\n" shifter
     *
     * @return XML
     *
     */
    @SuppressWarnings("rawtypes")
    private static String jsonToXml(JSONObject jo, String gt) {
        StringBuffer xmlStr = new StringBuffer();
        try {
            JSONObject payload = jo.getJSONObject("payload");
            JSONArray children = jo.getJSONArray("children");

            xmlStr.append(gt);
            xmlStr.append("<");
            String name = String.join("__",payload.get("name").toString().split(" "));

            if (name.equals("<Root>"))name = "Root";
            name = escapingSpecialCharacters(name);

            xmlStr.append(name);
            xmlStr.append(getAttrStr(payload));
            xmlStr.append(">\n");
            if (children!=null){
                for(int i = 0; i < children.size(); i++) {
                    JSONObject child = children.getJSONObject(i);
                    xmlStr.append(jsonToXml(child, gt + "\t"));
                }
            }
            xmlStr.append(gt);
            xmlStr.append("</");
            xmlStr.append(name);
            xmlStr.append(">\n");
        } catch (Exception e) {
            return "<error>1</error>";
        }
        return xmlStr.toString();
    }

    public static StringBuilder getAttrStr(JSONObject payload){
        StringBuilder sb = new StringBuilder();
        sb.append(" ");
        for (Map.Entry<String, Object> stringObjectEntry : payload.entrySet()) {
            Map.Entry entry = (Map.Entry) stringObjectEntry;
            String key = entry.getKey().toString();
            String val = entry.getValue().toString();
            if (key.equals("zOrders")){
                JSONObject zOrders = JSONObject.parseObject(val);
                sb.append(String.format("global=\"%s\" ", zOrders.get("global")));
                sb.append(String.format("local=\"%s\" ", zOrders.get("local")));
            }else if (key.equals("components")){
                val = val.replace("\"","");
                sb.append(String.format("%s=\"%s\" ", key, val));
            } else {
                val = escapingSpecialCharacters(val);
                sb.append(String.format("%s=\"%s\" ", key, val));
            }
        }
        return sb;
    }

    private static String escapingSpecialCharacters(String originStr){
        if (originStr==null)return null;
        originStr = originStr.replace("&","&amp;");
        originStr = originStr.replace("<","&lt;");
        originStr = originStr.replace(">","&gt;");
        originStr = originStr.replace("\"","&quot;");
        originStr = originStr.replace("'","&apos;");
        return originStr;
    }
}
