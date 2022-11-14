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
     * json对象转xml
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
            xmlStr.append(payload.get("name").toString());
            xmlStr.append(getAttrStr(payload));
            xmlStr.append(">\n");
            if (children!=null){
                for(int i = 0; i < children.size(); i++) {
                    JSONObject child = children.getJSONObject(i);
                    xmlStr.append(jsonToXml(child, gt + "\t"));
                }
            }
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
                sb.append(String.format("global:\"%s\" ", zOrders.get("global")));
                sb.append(String.format("local:\"%s\" ", zOrders.get("local")));
            }else {
                sb.append(String.format("%s:\"%s\" ", key, val));
            }
        }
        return sb;
    }
}
