package com.dunn.net.interceptor;

import android.content.Context;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * */

public class CacheRequestInterceptor implements Interceptor{
    public CacheRequestInterceptor(){

    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // 模拟无网环境，只是用缓存
        if(true){
            // 只读缓存
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE).build();
        }
        return chain.proceed(request);
    }
}
