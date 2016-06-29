package com.peaceful.apm.alert.el;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author WangJun
 * @version 1.0 16/6/21
 */
public class Var {


    public static String getValue(String var, Map<String, String> kv) {
        if (var.matches("^\\$\\{.+\\}$")) {
            var = var.replaceAll("\\$", "");
            var = var.replaceAll("\\{", "");
            var = var.replaceAll("\\}", "");
            return kv.get(var);
        } else {
            return var;
        }
    }

    public static String getContent(String content, Map<String, String> kv) {
        Iterator<String> keys = kv.keySet().iterator();
        while (keys.hasNext()){
            String key = keys.next();
            content = content.replaceAll("\\$\\{" + key + "\\}", kv.get(key));
        }
        return content;
    }
}
