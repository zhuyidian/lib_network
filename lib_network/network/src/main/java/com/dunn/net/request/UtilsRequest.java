package com.dunn.net.request;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author dunn
 * @function 参数自动封装
 */
public class UtilsRequest {
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
        //body增加公共信息
        mFormBodyBuild.add("username", "nate")
                .add("userage", "99");
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
        //使用HttpUrl获得get请求url+参数
        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(url).newBuilder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                httpUrlBuilder.addQueryParameter(entry.getKey(),entry.getValue());
            }
        }
        //urlParam=https://api.heweather.com/x3/weather?city=beijing&key=d17ce22ec5404ed883e1cfcaca0ecaa7
        String urlParam = httpUrlBuilder.build().toString();
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

    public static Request createUploadRequestTest() {
        RequestBody imageBody = RequestBody.create(MediaType.parse("image/jpeg"), new File("/Users/nate/girl.jpg"));
        MultipartBody body = new MultipartBody.Builder().
                setType(MultipartBody.FORM).
                addFormDataPart("name", "nate").
                addFormDataPart("filename", "girl.jpg", imageBody).build();

        Request request = new Request.Builder().
                url("http://192.168.1.6:8080/web/UploadServlet").post(body).build();

        return request;
    }
}