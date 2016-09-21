package com.peaceful.apm.alert.expression;

import com.google.inject.Singleton;
import com.peaceful.apm.alert.TermService;
import org.apache.commons.jexl3.*;

import java.util.Map;

/**
 * Created by wangjun on 16/9/6.
 */
@Singleton
public class JEXLTermService implements TermService {

    private static JexlEngine jexl = null;

    public JEXLTermService() {
        JexlBuilder builder = new JexlBuilder();
        jexl = builder.create();

    }

    public boolean isTrue(String term, Map<String, Object> context) {
        JexlExpression expression = jexl.createExpression(term);
        return (Boolean) expression.evaluate(new MapContext(context));
    }

    public String frame(String template, Map<String, Object> context) {
        try {
            JexlExpression expression = jexl.createExpression("`" + template + "`");
            return (String) expression.evaluate(new MapContext(context));
        }catch (NullPointerException e){
            // ignore  this is a jexl bug
            JexlExpression expression = jexl.createExpression("`" + template + "`");
            return (String) expression.evaluate(new MapContext(context));
        }
    }
}
