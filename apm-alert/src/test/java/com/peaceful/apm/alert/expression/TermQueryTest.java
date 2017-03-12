package com.peaceful.apm.alert.expression;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

/**
 * @author WangJun
 * @version 1.0 16/6/21
 */
public class TermQueryTest {
    TermQuery termQuery = new TermQuery();

    @Test
    public void bool() throws Exception {
        Map<String, String> map = Maps.newHashMap();
        map.put("count", "111");
        map.put("min", "6");
        System.out.println(termQuery.bool("${count}>100&${min}<6", map));
    }

}