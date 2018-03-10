package com.peaceful.apm.test.metric;

/**
 * Created by wangjun38 on 2018/1/28.
 */
public enum WatchType {

    COUNT(1),
    COUNT_AND_TIME(2),
    COUNT_AND_TIME_HISTOGRAMS(3);

    private int typeCode;

    WatchType(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public static WatchType findByTypeCode(int typeCode) {
        for (WatchType w : WatchType.values()) {
            if (w.getTypeCode() == typeCode) {
                return w;
            }
        }
        return COUNT; // default return
    }


}
