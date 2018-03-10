package com.peaceful.apm.dashboard.helper;

import java.io.Serializable;

/**
 * Created by wangjun on 16/9/7.
 */
public class ResponseFormat implements Serializable {


    public int code;
    public String result;
    public Object data;
    public String message;


    public ResponseFormat(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public ResponseFormat(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}

