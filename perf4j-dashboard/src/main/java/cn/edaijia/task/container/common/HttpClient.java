package cn.edaijia.task.container.common;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于httpClient 4.3.5,默认请求超时和socket超时均为6s
 * <p/>
 * <p/>
 * Date 14-9-28.
 * Author WangJun
 * Email wangjuntytl@163.com
 */
public abstract class HttpClient {

    final static private Logger logger = LoggerFactory.getLogger(HttpClient.class);

    /**
     * @param url
     * @return
     * @throw HttpClientRuntimeException
     */
    public static String get(String url) {
        return get(url, null);
    }

    private static String innerGet(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(6000).setConnectTimeout(6000).build();//设置请求和传输超时时间
            httpget.setConfig(requestConfig);
            String result = null;
            // The underlying HTTP connection is still held by the response object
            // to allow the response content to be streamed directly from the network socket.
            // In order to ensure correct deallocation of system resources
            // the user MUST call CloseableHttpResponse#close() from a finally clause.
            // Please note that if response content is not fully consumed the underlying
            // connection cannot be safely re-used and will be shut down and discarded
            // by the connection manager.
            CloseableHttpResponse response = null;
            try {
                response = httpclient.execute(httpget);
                StatusLine statusLine = response.getStatusLine();
                logger.info("http protocol:{} statusCode:{} reasonPhrase:{}", new Object[]{statusLine.getProtocolVersion(), statusLine.getStatusCode(), statusLine.getReasonPhrase()});
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, HTTP.UTF_8);
            } catch (IOException e) {
                throw new HttpClientRuntimeException("httpClient get request error", e);
            } finally {
                try {
                    if (response != null)
                        response.close();
                } catch (IOException e) {
                    throw new HttpClientRuntimeException("httpClient get request error", e);
                }
            }
            logger.info("get request url is {}  response data is {}", url, result);
            return result == null ? "" : result.trim();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                throw new HttpClientRuntimeException("httpClient get request error", e);
            }

        }
    }

    public static String post(String url, List<BasicNameValuePair> params, String en) {
        if (url.indexOf("?") == -1) {
            //pass
        } else {
            try {
                String urlParams = urlParamEncoding(url, en);
                url = url.substring(0, url.indexOf("?"));
                return post(url + urlParams, params);
            } catch (UnsupportedEncodingException e) {
                throw new HttpClientRuntimeException("url编码异常", e);
            }
        }
        return null;
    }


    /**
     * post 参数分为url参数和body参数，不会把这两者参数混合到一起，加入url参数含有中文，
     *
     * @param url
     * @param params
     * @return
     * @throw HttpClientRuntimeException
     */
    public static String post(String url, List<BasicNameValuePair> params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(6000).setConnectTimeout(6000).build();//设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
            CloseableHttpResponse response = null;
            String result = null;
            try {
                if (params == null) {
                    // pass
                } else {
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, Consts.UTF_8);
                    httpPost.setEntity(entity);
                }
                response = httpClient.execute(httpPost);
                StatusLine statusLine = response.getStatusLine();
                logger.info("http protocol:{} statusCode:{} reasonPhrase:{}", new Object[]{statusLine.getProtocolVersion(), statusLine.getStatusCode(), statusLine.getReasonPhrase()});
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, Consts.UTF_8);
            } catch (UnsupportedEncodingException e) {
                throw new HttpClientRuntimeException("httpClient post request error", e);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (response != null)
                        response.close();
                } catch (IOException e) {
                    throw new HttpClientRuntimeException("httpClient post request error", e);
                }
            }
            logger.info("post request url is {} params is " + params + " response data is \n{}", url, result);
            return result == null ? "" : result.trim();

        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                throw new HttpClientRuntimeException("httpClient post request error", e);
            }
        }

    }

    private static List<BasicNameValuePair> parseParamsOfUrl(String url) {
        String paramsStr = null;
        if (url.indexOf("?") == -1) {
            return null;
        } else {
            paramsStr = url.substring(url.indexOf("?"));
        }
        if (StringUtils.isNotEmpty(paramsStr)) {
            String[] paramsArr = paramsStr.substring(1).split("&");
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            if (params == null)
                params = new ArrayList<BasicNameValuePair>();
            for (String str : paramsArr) {
                String[] each = str.split("=");
                if (each.length == 0) {

                } else if (each.length == 1) {
                    params.add(new BasicNameValuePair(each[0], null));
                } else {
                    params.add(new BasicNameValuePair(each[0], each[1]));
                }
            }
            return params;
        }
        return null;
    }

    private static String urlParamEncoding(String url, String encoding) throws UnsupportedEncodingException {
        StringBuffer params = new StringBuffer(url.length() + 26);
        List<BasicNameValuePair> basicNameValuePairs = parseParamsOfUrl(url);
        if (basicNameValuePairs != null && basicNameValuePairs.size() != 0) {
            params.append("?");
            for (BasicNameValuePair nv : basicNameValuePairs) {
                params.append(nv.getName()).append("=").append(URLEncoder.encode(nv.getValue(), encoding)).append("&");
            }
        }
        return params.toString();
    }


    /**
     * get 参数 你可以放在url,也可以放在参数 params 集合，你也可以在url和params混合放入post的参数
     *
     * @param url
     * @param params
     * @return
     * @throw HttpClientRuntimeException
     */
    public static String get(String url, List<BasicNameValuePair> params) {
        URI uri = null;
        if (url == null)
            Preconditions.checkNotNull(url, "request url is empty");
        try {
            String scheme;
            if (url.indexOf("://") == -1) {
                scheme = "http";
            } else {
                scheme = url.substring(0, url.indexOf("://"));
                url = url.substring(url.indexOf("://") + 3);
            }
            String path = null;
            if (url.indexOf("?") == -1) {
                if (url.indexOf("/") == -1) {
                    path = "";
                } else {
                    path = url.substring(url.indexOf("/"));
                    url = url.substring(0, url.indexOf("/"));
                }
            } else {
                String paramsStr = url.substring(url.indexOf("?"));
                if (url.indexOf("/") == -1) {
                    path = "";
                    url = url.substring(0, url.indexOf("?"));
                } else {
                    path = url.substring(url.indexOf("/"), url.indexOf("?"));
                    url = url.substring(0, url.indexOf("/"));
                }
                if (StringUtils.isNotEmpty(paramsStr)) {
                    String[] paramsArr = paramsStr.substring(1).split("&");
                    if (params == null)
                        params = new ArrayList<BasicNameValuePair>();
                    for (String str : paramsArr) {
                        String[] each = str.split("=");
                        if (each.length == 0) {

                        } else if (each.length == 1) {
                            params.add(new BasicNameValuePair(each[0], null));
                        } else {
                            params.add(new BasicNameValuePair(each[0], each[1]));
                        }
                    }

                }
            }
            URIBuilder uriBuilder = new URIBuilder()
                    .setScheme(scheme)
                    .setHost(url)
                    .setPath(path);
            if (params != null) {
                for (BasicNameValuePair nv : params) {
                    uriBuilder.setParameter(nv.getName(), nv.getValue());
                }
            }
            uri = uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new HttpClientRuntimeException("get url format error ", e);
        }
        return innerGet(uri.toString());
    }

    /**
     * 允许post请求时把参数放在url里
     *
     * @param url
     * @return
     * @throw HttpClientRuntimeException
     */
    public static String post(String url) {
        return post(url, null);
    }

    public static String postBody(String url, String body) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(6000).setConnectTimeout(6000).build();//设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
            CloseableHttpResponse response = null;
            String result = null;
            try {
                if (body == null) {
                    // pass
                } else {
                    StringEntity entity = new StringEntity(body, "UTF-8");
                    entity.setContentEncoding("UTF-8");
                    entity.setContentType("application/json");
                    httpPost.setEntity(entity);
                }
                response = httpClient.execute(httpPost);
                StatusLine statusLine = response.getStatusLine();
                logger.info("http protocol:{} statusCode:{} reasonPhrase:{}", new Object[]{statusLine.getProtocolVersion(), statusLine.getStatusCode(), statusLine.getReasonPhrase()});
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, Consts.UTF_8);
            } catch (UnsupportedEncodingException e) {
                throw new HttpClientRuntimeException("httpClient post request error", e);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (response != null)
                        response.close();
                } catch (IOException e) {
                    throw new HttpClientRuntimeException("httpClient post request error", e);
                }
            }
            logger.info("post request url is {} body is" + body + "response data is \n{}", url, result);
            return result == null ? "" : result.trim();

        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                throw new HttpClientRuntimeException("httpClient post request error", e);
            }
        }
    }
}

