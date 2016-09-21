package com.peaceful.apm.alert;

import java.util.Map;

/**
 * Created by wangjun on 16/9/6.
 */
public interface TermService {

    boolean isTrue(String expression, Map<String,Object> context);
    String frame(String expression, Map<String,Object> context);
}
