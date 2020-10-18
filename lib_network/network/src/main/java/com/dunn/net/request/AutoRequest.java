package com.dunn.net.request;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author dunn
 * @function get/post自动选择
 */
public class AutoRequest {
    enum HttpMethodType {
        GET,
        POST,
    }
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType TEXT_FILE = MediaType.parse("text/plain; charset=utf-8");

    /**
     * 另一种创建request，不带请求头
     * @param url
     * @param methodType
     * @param json
     * @return
     */
    public static Request createRequestNoHeader(String url, HttpMethodType methodType, String json) {
        Request.Builder build = new Request.Builder().url(url);
        build.addHeader("api-version", "2");
        if (methodType == HttpMethodType.GET) {
        } else if (methodType == HttpMethodType.POST) {
            RequestBody requestBody = RequestBody.create(JSON, json);
            build.post(requestBody);
        }
        return build.build();
    }

    /**
     * 另一种创建request带请求头
     * @param url
     * @param methodType
     * @param json
     * @param headerMap
     * @return
     */
    public static  Request createRequestWithHeader(String url, HttpMethodType methodType, String json, HashMap<String, String> headerMap) {
        if (headerMap==null){
            headerMap=new HashMap<>();
        }
        Request.Builder build = new Request.Builder().url(url);
        build.addHeader("client_version", "版本");
        build.addHeader("Accept-Language", "语言");
        //添加请求头
        if (headerMap != null) {
            Iterator iter = headerMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                build.addHeader((String) entry.getKey(), (String) entry.getValue());
            }
        }
        if (methodType == HttpMethodType.GET) {
        } else if (methodType == HttpMethodType.POST) {
            RequestBody requestBody = RequestBody.create(JSON, json);
            build.post(requestBody);
        }
        return build.build();
    }
}