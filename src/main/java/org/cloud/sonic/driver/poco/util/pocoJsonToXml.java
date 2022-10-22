package org.cloud.sonic.driver.poco.util;

import com.github.underscore.Json;
import com.github.underscore.U;
import com.github.underscore.Xml;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class pocoJsonToXml extends U{
    private static final String ROOT = "root";

    public pocoJsonToXml(Iterable iterable) {
        super(iterable);
    }

    public static String jsonToXml(String json, Mode mode, String newRootName) {
        return jsonToXml(json, Xml.XmlStringBuilder.Step.TWO_SPACES, mode, newRootName);
    }
    // core code
    public static Map<String, Object> forceAttributeUsageOverride(Map<String, Object> map) {
        Map<String, Object> outMap = newLinkedHashMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            // poco dedicated conversion
            if (entry.getKey().equals("payload")){
                makeAttributePoco(outMap,entry);
            } else if (entry.getKey().equals("children")){
                Map<String, Object> childMap = newLinkedHashMap();
                for (Object mapObject : (List) entry.getValue()){
                    String name  = (String) ((Map<String, Object>) mapObject).get("name");
                    childMap.put(name,forceAttributeUsageOverride((Map<String, Object>) mapObject));
                }
                outMap.put("children",childMap);
            } else{
                outMap.put(
                        entry.getValue() instanceof Map
                                || entry.getValue() instanceof List
                                || entry.getKey().startsWith("-")
                                ? entry.getKey()
                                : "-" + entry.getKey(),
                        makeAttributeUsage(entry.getValue()));
            }
        }
        return outMap;
    }

    public static String valueJoinString(List<Object> objectList){
        if (objectList==null||objectList.isEmpty())return "[]";
        StringBuilder result = new StringBuilder("[");
        for (int i = 0;i<objectList.size()-1;i++){
            result.append(objectList.get(i).toString());
            result.append(",");
        }
        result.append(objectList.get(objectList.size() - 1)).append("]");
        return result.toString();
    }

    @SuppressWarnings("unchecked")
    private static void makeAttributePoco(Map<String, Object> outMap,Map.Entry<String, Object> entry) {
        HashMap<String,Object> tempMap = (HashMap<String, Object>) entry.getValue();
        for (String attributeName:tempMap.keySet()){
            if (tempMap.get(attributeName) instanceof LinkedHashMap){
                if (attributeName.equals("zOrders")){
                    Map map = (Map) tempMap.get("zOrders");
                    outMap.put("-global",String.valueOf(map.get("global")));
                    outMap.put("-local",String.valueOf(map.get("local")));
                    continue;
                }
                Object object = forceAttributeUsageOverride((Map) tempMap.get(attributeName));
                outMap.put(attributeName,object);
            }
            else if (tempMap.get(attributeName) instanceof List){
                // put prefixed "-" with means the value is an attribute
                outMap.put("-" + attributeName,valueJoinString((List<Object>) tempMap.get(attributeName)));
            }else {
                outMap.put("-" + attributeName, String.valueOf(tempMap.get(attributeName)));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static Object makeAttributeUsage(Object value) {
        final Object result;
        if (value instanceof List) {
            List<Object> values = newArrayList();
            for (Object item : (List) value) {
                values.add(item instanceof Map ? forceAttributeUsageOverride((Map) item) : item);
            }
            result = values;
        } else if (value instanceof Map) {
            result = forceAttributeUsageOverride((Map) value);
        } else {
            result = value;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static String jsonToXml(
            String json, Xml.XmlStringBuilder.Step identStep, Mode mode, String newRootName) {
        Object object = Json.fromJson(json);
        final String result;
        if (object instanceof Map) {
            if (mode == Mode.FORCE_ATTRIBUTE_USAGE) {
                result = Xml.toXml(forceAttributeUsageOverride((Map) object), identStep, newRootName);
            } else if (mode == Mode.DEFINE_ROOT_NAME) {
                result = Xml.toXml((Map) object, identStep, newRootName);
            } else if (mode == Mode.REPLACE_NULL_WITH_EMPTY_VALUE) {
                result = Xml.toXml(replaceNullWithEmptyValue((Map) object), identStep, newRootName);
            } else if (mode == Mode.REPLACE_EMPTY_STRING_WITH_EMPTY_VALUE) {
                result =
                        Xml.toXml(
                                replaceEmptyStringWithEmptyValue((Map) object),
                                identStep,
                                newRootName);
            } else if (mode == Mode.FORCE_ADD_ROOT_JSON_TO_XML
                    && !Xml.XmlValue.getMapKey(object).equals(ROOT)) {
                final Map<String, Object> map = U.newLinkedHashMap();
                map.put(newRootName, object);
                result = Xml.toXml(map, identStep);
            } else if (mode == Mode.FORCE_REMOVE_ARRAY_ATTRIBUTE_JSON_TO_XML) {
                result = Xml.toXml((Map) object, identStep, newRootName, Xml.ArrayTrue.SKIP);
            } else if (mode == Mode.FORCE_REMOVE_ARRAY_BOOLEAN_NUMBER_ATTRIBUTES_JSON_TO_XML) {
                result =
                        Xml.toXml(
                                replaceNumberAndBooleanWithString((Map) object),
                                identStep,
                                newRootName,
                                Xml.ArrayTrue.SKIP);
            } else {
                result = Xml.toXml((Map) object, identStep);
            }
            return result;
        }
        return Xml.toXml((List) object, identStep);
    }
}
