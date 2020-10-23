package com.dunn.net.helper;

import com.dunn.net.cookie.SimpleCookieJar;
import com.dunn.net.helper.listener.RequestUploadProgressListener;
import com.dunn.net.request.FileRequest;
import com.dunn.net.utils.HttpsUtils;
import com.dunn.net.helper.listener.DisposeDataListener;
import com.dunn.net.request.CommonRequest;
import com.dunn.net.request.JsonRequest;
import com.dunn.net.response.JsonCallback;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author dunn
 * @function 用来发送get, post请求的工具类，包括设置一些请求的共用参数
 */
public class OkHttpHelper {
    private static final int TIME_OUT = 30;
    private static OkHttpClient mOkHttpClient;

    /**
     * 完成对OkHttpClient的初始化
     */
    static {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        //默认对域名信任
        okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //拿到hostname，判断黑名单，白名单分别返回
                return true;
            }
        });

        /**
         *  为所有请求添加请求头，看个人需求
         */
        okHttpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request =
                        chain.request().newBuilder()
                                .addHeader("User-Agent", "Imooc-Mobile") // 标明发送本次请求的客户端
                                .build();
                return chain.proceed(request);
            }
        });
        okHttpClientBuilder.cookieJar(new SimpleCookieJar());
        //超时时间设置
        okHttpClientBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        //允许重定向
        okHttpClientBuilder.followRedirects(true);
        /**
         * trust all the https point  信任所有https点
         */
        okHttpClientBuilder.sslSocketFactory(HttpsUtils.initSSLSocketFactory(),
                HttpsUtils.initTrustManager());
        mOkHttpClient = okHttpClientBuilder.build();
    }

    /**
     * 另一种OkHttpClient的初始化
     * @return
     */
    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            builder.connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true).build();

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * 发送get请求
     * @param url
     * @param headers
     * @param params
     * @param listener
     */
    public static void getRequest(String url,
                                  RequestParams headers,
                                  RequestParams params,
                                  DisposeDataListener listener) {
        Request request = null;
        Callback callback = new JsonCallback(listener,null);
        if(headers!=null && headers.hasParams()){
            request = CommonRequest.createGetRequest(url, params, headers);
        }else{
            request = CommonRequest.createGetRequest(url, params);
        }
        request(request,callback);
    }

    /**
     * 发送get请求
     * @param url
     * @param headers
     * @param params
     * @param listener
     * @param mClass
     */
    public static void getRequest(String url,
                                  RequestParams headers,
                                  RequestParams params,
                                  DisposeDataListener listener,
                                  Class<?> mClass) {
        Request request = null;
        Callback callback = new JsonCallback(listener,mClass);
        if(headers!=null && headers.hasParams()){
            request = CommonRequest.createGetRequest(url, params, headers);
        }else{
            request = CommonRequest.createGetRequest(url, params);
        }
        request(request,callback);
    }

    /**
     * 发送get请求
     * @param url
     * @param headers
     * @param json  json格式参数
     * @param listener
     */
    public static void getRequest(String url,
                                  HashMap<String, String> headers,
                                  String json,
                                  DisposeDataListener listener) {
        Request request = null;
        Callback callback = new JsonCallback(listener,null);
        if(headers!=null && headers.size()>0){
            request = JsonRequest.createRequest(url, JsonRequest.HttpMethodType.GET, json, headers);
        }else{
            request = JsonRequest.createRequest(url, JsonRequest.HttpMethodType.GET, json);
        }
        request(request,callback);
    }

    /**
     * 发送get请求
     * @param url
     * @param headers
     * @param json  json格式参数
     * @param listener
     * @param mClass
     */
    public static void getRequest(String url,
                                  HashMap<String, String> headers,
                                  String json,
                                  DisposeDataListener listener,
                                  Class<?> mClass) {
        Request request = null;
        Callback callback = new JsonCallback(listener,mClass);
        if(headers!=null && headers.size()>0){
            request = JsonRequest.createRequest(url, JsonRequest.HttpMethodType.GET, json, headers);
        }else{
            request = JsonRequest.createRequest(url, JsonRequest.HttpMethodType.GET, json);
        }
        request(request,callback);
    }

    /**
     * 发送post请求
     * @param url
     * @param headers
     * @param params
     * @param listener
     */
    public static void postRequest(String url,
                                   RequestParams headers,
                                   RequestParams params,
                                   DisposeDataListener listener) {
        Request request = null;
        Callback callback = new JsonCallback(listener,null);
        if(headers!=null && headers.hasParams()){
            request = CommonRequest.createPostRequest(url, params, headers);
        }else{
            request = CommonRequest.createPostRequest(url, params);
        }
        request(request,callback);
    }

    /**
     * 发送post请求
     * @param url
     * @param headers
     * @param params
     * @param listener
     * @param mClass
     */
    public static void postRequest(String url,
                                   RequestParams headers,
                                   RequestParams params,
                                   DisposeDataListener listener,
                                   Class<?> mClass) {
        Request request = null;
        Callback callback = new JsonCallback(listener,mClass);
        if(headers!=null && headers.hasParams()){
            request = CommonRequest.createPostRequest(url, params, headers);
        }else{
            request = CommonRequest.createPostRequest(url, params);
        }
        request(request,callback);
    }

    /**
     * 发送post请求
     * @param url
     * @param headers
     * @param json  json格式参数
     * @param listener
     */
    public static void postRequest(String url,
                                   HashMap<String, String> headers,
                                   String json,
                                   DisposeDataListener listener) {
        Request request = null;
        Callback callback = new JsonCallback(listener,null);
        if(headers!=null && headers.size()>0){
            request = JsonRequest.createRequest(url, JsonRequest.HttpMethodType.POST, json, headers);
        }else{
            request = JsonRequest.createRequest(url, JsonRequest.HttpMethodType.POST, json);
        }
        request(request,callback);
    }

    /**
     * 发送post请求
     * @param url
     * @param headers
     * @param json  json格式参数
     * @param listener
     * @param mClass
     */
    public static void postRequest(String url,
                                   HashMap<String, String> headers,
                                   String json,
                                   DisposeDataListener listener,
                                   Class<?> mClass) {
        Request request = null;
        Callback callback = new JsonCallback(listener,mClass);
        if(headers!=null && headers.size()>0){
            request = JsonRequest.createRequest(url, JsonRequest.HttpMethodType.POST, json, headers);
        }else{
            request = JsonRequest.createRequest(url, JsonRequest.HttpMethodType.POST, json);
        }
        request(request,callback);
    }

    /**
     * 上传文件请求
     * @param url
     * @param params
     * @param listener
     */
    public static void uploadRequest(String url,
                                     RequestParams params,
                                     DisposeDataListener listener) {
        Request request = FileRequest.createUploadRequest_File(url, params);
        Callback callback = new JsonCallback(listener,null);
        request(request,callback);
    }

    /**
     * 上传文件请求，并且带进度
     * @param url
     * @param params
     * @param listener
     * @param processListener
     */
    public static void uploadRequest(String url,
                                     RequestParams params,
                                     DisposeDataListener listener,
                                     RequestUploadProgressListener processListener) {
        Request request = FileRequest.createUploadRequest_File(url, params, processListener);
        Callback callback = new JsonCallback(listener,null);
        request(request,callback);
    }

    /**
     * 通过构造好的Request,Callback去发送请求
     */
    private static Call request(Request request, Callback callback) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
        return call;
    }
}