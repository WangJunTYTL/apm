package com.peaceful.apm.aggregate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author WangJun
 * @version 1.0 16/6/26
 */
public class BaiduGraphFormat {


    public static Object convert(MetricsSet metricsSet) {
        if (metricsSet == null) {
            return null;
        }
        return JSON.toJSON(metricsSet);
    }

}
