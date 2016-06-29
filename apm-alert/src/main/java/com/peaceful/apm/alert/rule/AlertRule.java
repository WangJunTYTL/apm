package com.peaceful.apm.alert.rule;

/**
 * @author WangJun
 * @version 1.0 16/6/19
 */
public interface AlertRule {


    void notice();
    boolean meet();

}
