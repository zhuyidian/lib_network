package com.dunn.net.response;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;


import com.dunn.net.helper.exception.OkHttpException;
import com.dunn.net.helper.CodeHelper;
import com.dunn.net.helper.listener.DisposeDataListener;
import com.dunn.net.helper.listener.DisposeDownloadListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @文件描述：专门处理文件下载回调
 */
public class FileCallback implements Callback {
    private static final int PROGRESS_MESSAGE = 0x01;
    private Handler mDeliveryHandler;  //将其它线程的数据转发到UI线程
    private DisposeDownloadListener mListener;
    private String mFilePath;
    private int mProgress;

    public FileCallback(DisposeDataListener listener, String filePath) {
        this.mListener = (DisposeDownloadListener) listener;
        this.mFilePath = filePath;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case PROGRESS_MESSAGE:
                        mListener.onProgress((int) msg.obj);
                        break;
                }
            }
        };
    }

    @Override
    public void onFailure(final Call call, final IOException ioexception) {
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(CodeHelper.NETWORK_ERROR, ioexception));
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final int code = response.code();
        final File file = handleResponse(response);
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                if (file != null) {
                    mListener.onSuccess(code,file);
                } else {
                    mListener.onFailure(new OkHttpException(CodeHelper.IO_ERROR, CodeHelper.EMPTY_MSG));
                }
            }
        });
    }

    /**
     * 此时还在子线程中，不则调用回调接口
     *
     * @param response
     * @return
     */
    private File handleResponse(Response response) {
        if (response == null) {
            return null;
        }

        InputStream inputStream = null;
        File file = null;
        FileOutputStream fos = null;
        byte[] buffer = new byte[2048];
        int length;
        int currentLength = 0;
        double sumLength;
        try {
            checkLocalFilePath(mFilePath);
            file = new File(mFilePath);
            fos = new FileOutputStream(file);
            inputStream = response.body().byteStream();
            sumLength = (double) response.body().contentLength();

            while ((length = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
                currentLength += length;
                mProgress = (int) (currentLength / sumLength * 100);
                mDeliveryHandler.obtainMessage(PROGRESS_MESSAGE, mProgress).sendToTarget();
            }
            fos.flush();
        } catch (Exception e) {
            file = null;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (inputStream != null) {

                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                file = null;
            }
        }
        return file;
    }

    private void checkLocalFilePath(String localFilePath) {
        File path = new File(localFilePath.substring(0,
                localFilePath.lastIndexOf("/") + 1));
        File file = new File(localFilePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}