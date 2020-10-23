package com.dunn.net.helper.listener;

/**
 * Created by dunn
 */
public interface RequestUploadProgressListener {
    void onProgress(long total, long current);
}
