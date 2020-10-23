package com.dunn.net.request;

import com.dunn.net.helper.RequestParams;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * @author dunn
 * @function 对外提供get/post请求
 */
public class CommonRequest {
    /**
     * create the key-value Request
     *
     * @param url
     * @param params
     * @return
     */
    public static Request createPostRequest(String url, RequestParams params) {
        return createPostRequest(url, params, null);
    }

    /**
     * 可以带请求头的Post请求
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Request createPostRequest(String url, RequestParams params, RequestParams headers) {
        FormBody.Builder mFormBodyBuild = new FormBody.Builder();
        //这里可以给body增加公共信息
//        mFormBodyBuild.add("username", "nate")
//                .add("userage", "99");
        //参数遍历
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                mFormBodyBuild.add(entry.getKey(), entry.getValue());
            }
        }
        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }
        FormBody mFormBody = mFormBodyBuild.build();
        Headers mHeader = mHeaderBuild.build();
        Request request = new Request.Builder().url(url)
                .post(mFormBody)
                .headers(mHeader)
                .build();
        return request;
    }

    /**
     * ressemble the params to the url
     *
     * @param url
     * @param params
     * @return
     */
    public static Request createGetRequest(String url, RequestParams params) {

        return createGetRequest(url, params, null);
    }

    /**
     * 可以带请求头的Get请求
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Request createGetRequest(String url, RequestParams params, RequestParams headers) {
        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }
        Headers mHeader = mHeaderBuild.build();
        return new Request.Builder()
                .url(urlBuilder.substring(0, urlBuilder.length() - 1))
                .get()
                .headers(mHeader)
                .build();
    }

    /**
     * 可以带请求头的Get请求，使用HttpUrl封装请求参数
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Request createGetRequest_HttpUrl(String url, RequestParams params, RequestParams headers) {
        //使用HttpUrl获得get请求url+参数
        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(url).newBuilder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                httpUrlBuilder.addQueryParameter(entry.getKey(),entry.getValue());
            }
        }
        String urlParam = httpUrlBuilder.build().toString();
        //得到的请求链接就是这种：urlParam=https://api.heweather.com/x3/weather?city=beijing&key=d17ce22ec5404ed883e1cfcaca0ecaa7
        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }
        Headers mHeader = mHeaderBuild.build();
        return new Request.Builder()
                .url(urlParam)
                .get()
                .headers(mHeader)
                .build();
    }


}