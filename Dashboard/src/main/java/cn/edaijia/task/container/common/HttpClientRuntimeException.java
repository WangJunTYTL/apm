package cn.edaijia.task.container.common;

/**
 * httpClient action exception
 * <p/>
 * Created by wangjun on 15/1/29.
 */
public class HttpClientRuntimeException extends RuntimeException {

    public HttpClientRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
