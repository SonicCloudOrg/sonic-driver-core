package org.cloud.sonic.driver.poco.util;

import com.alibaba.fastjson.JSONObject;

public class PocoTool {
    public static boolean checkPocoRpcResultID(String pocoRpcResult,String sendID){
        JSONObject object = JSONObject.parseObject(pocoRpcResult);
        return sendID.equals(object.getString("id"));
    }
}
