package com.peaceful.apm.alert.expression;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * @author WangJun
 * @version 1.0 16/6/20
 */
public class TermQuery {

    public boolean bool(String term, Map<String, String> kvs) {
        List<Term> termList = parse(term);
        if (termList.isEmpty()) {
            return false;
        }
        for (Term t : termList) {
            long x = Long.parseLong(Var.getValue(t.x, kvs));
            long y = Long.parseLong(Var.getValue(t.y, kvs));
            if (term != null) {
                if (TermType.GT == t.op) {
                    if (x > y) {
                        // ignore
                    } else {
                        return false;
                    }
                } else if (TermType.LT == t.op) {
                    if (x < y) {
                        // ignore
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private static List<Term> parse(String query) {
        List<Term> termList = Lists.newArrayList();
        String[] terms = query.split("&");
        for (String term : terms) {
            Term t = new Term();
            int index = term.indexOf(">");
            if (index != -1) {
                t.op = TermType.GT;
            } else if ((index = term.indexOf("<")) != -1) {
                t.op = TermType.LT;
            }
            t.x = term.substring(0, index).trim();
            t.y = term.substring(index + 1, term.length()).trim();
            termList.add(t);
        }
        return termList;
    }


}
