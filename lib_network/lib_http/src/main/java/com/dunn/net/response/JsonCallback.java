package com.dunn.net.response;

import android.os.Handler;
import android.os.Looper;

import com.dunn.net.helper.exception.OkHttpException;
import com.dunn.net.helper.CodeHelper;
import com.dunn.net.helper.listener.DisposeDataListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author vision
 * @function 专门处理JSON的回调
 */
public class JsonCallback implements Callback {
    private Handler mDeliveryHandler; //将其它线程的数据转发到UI线程
    private DisposeDataListener mListener;
    private Class<?> mClass;

    public JsonCallback(DisposeDataListener mListener,Class<?> mClass) {
        this.mListener = mListener;
        this.mClass = mClass;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(final Call call, final IOException ioexception) {
        /**
         * 此时还在非UI线程，因此要转发
         */
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(CodeHelper.NETWORK_ERROR, ioexception));
            }
        });
    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });
    }

    private void handleResponse(Object responseObj) {
        if (responseObj == null || responseObj.toString().trim().equals("")) {
            mListener.onFailure(new OkHttpException(CodeHelper.NETWORK_ERROR, CodeHelper.EMPTY_MSG));
            return;
        }

        try {
            /**
             * 协议确定后看这里如何修改
             */
            JSONObject result = new JSONObject(responseObj.toString());
            if (mClass == null) {
                mListener.onSuccess(result);
            } else {
                Object obj = new Gson().fromJson(responseObj.toString(), mClass);  //转化成实体对象
                if (obj != null) {
                    mListener.onSuccess(obj);
                } else {
                    mListener.onFailure(new OkHttpException(CodeHelper.JSON_ERROR, CodeHelper.EMPTY_MSG));
                }
            }
        } catch (Exception e) {
            mListener.onFailure(new OkHttpException(CodeHelper.OTHER_ERROR, e.getMessage()));
            e.printStackTrace();
        }
    }
}