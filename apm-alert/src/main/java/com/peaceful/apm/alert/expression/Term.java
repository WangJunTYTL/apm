package com.peaceful.apm.alert.expression;

/**
 * 支持简单的条件表达式，把两个值进行大小比较运算,例如x>y
 *
 * @author WangJun
 * @version 1.0 16/6/19
 */
public class Term {

    public String x;
    public String y;
    public TermType op;

}
