package com.dunn.net.request;

import android.text.TextUtils;

import com.dunn.net.helper.RequestParams;
import com.dunn.net.helper.listener.RequestUploadProgressListener;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author dunn
 * @function 对外提供文件上传/下载 请求
 */
public class FileRequest {
    private static final MediaType FILE_TYPE_FILE = MediaType.parse("application/octet-stream");
    private static final MediaType FILE_TYPE_IMAGE = MediaType.parse("image/jpeg");

    /**
     * 上传文件
     * @param url
     * @param params  key:文件名  value:File & Text
     * @return
     */
    public static Request createUploadRequest_FileAndText(String url, RequestParams params) {
        // 构建请求 Body
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder();
        requestBodyBuilder.setType(MultipartBody.FORM);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.fileParams.entrySet()) {
                if (entry.getValue() instanceof File) {
                    //添加参数
                    String fileName = entry.getKey();
                    File file = (File) entry.getValue();
                    requestBodyBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + fileName + "\""),
                            RequestBody.create(FILE_TYPE_FILE, file));
                } else if (entry.getValue() instanceof String) {
                    //添加参数
                    String fileName = entry.getKey();
                    String value = (String) entry.getValue();
                    requestBodyBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + fileName + "\""),
                            RequestBody.create(null, value));
                }
            }
        }
        return new Request.Builder().url(url).post(requestBodyBuilder.build()).build();
    }

    /**
     * 上传文件
     * @param url
     * @param params  key:文件名  value:File
     * @return
     */
    public static Request createUploadRequest_File(String url, RequestParams params) {
        // 构建请求 Body
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder();
        requestBodyBuilder.setType(MultipartBody.FORM);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.fileParams.entrySet()) {
                if (entry.getValue() instanceof File) {
                    //添加参数
                    String fileName = entry.getKey();
                    File file = (File) entry.getValue();
                    requestBodyBuilder.addFormDataPart("platform", "android").
                            addFormDataPart("file", fileName, RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())),file));
                } else if (entry.getValue() instanceof String) {
//                    RequestBody imageBody2 = RequestBody.create(null, (String) entry.getValue());
//                    requestBody.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""), imageBody2);
                }
            }
        }
        return new Request.Builder().url(url).post(requestBodyBuilder.build()).build();
    }

    /**
     * 上传文件，并且带进度
     * @param url
     * @param params  key:文件名  value:File
     * @return
     */
    public static Request createUploadRequest_File(String url, RequestParams params, RequestUploadProgressListener mRequestUploadProgressListener) {
        // 构建请求 Body
        ExMultipartBody exMultipartBody = null;
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.fileParams.entrySet()) {
                if (entry.getValue() instanceof File) {
                    //添加参数
                    String fileName = entry.getKey();
                    File file = (File) entry.getValue();
                    requestBodyBuilder.addFormDataPart("platform", "android");
                    requestBodyBuilder.addFormDataPart("file", fileName,
                            RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())), file));

                    exMultipartBody = new ExMultipartBody(requestBodyBuilder.build(),mRequestUploadProgressListener);
                } else if (entry.getValue() instanceof String) {
//                    RequestBody imageBody2 = RequestBody.create(null, (String) entry.getValue());
//                    requestBody.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""), imageBody2);
                }
            }
        }

        if(exMultipartBody!=null){
            return new Request.Builder().url(url).post(exMultipartBody).build();
        }else{
            return new Request.Builder().url(url).post(requestBodyBuilder.build()).build();
        }
    }

    /**
     * 上传图片
     * @param url
     * @param params  key:文件名  value:File
     * @return
     */
    public static Request createUploadRequest_Image(String url, RequestParams params) {
        //RequestBody imageBody = RequestBody.create(MediaType.parse("image/jpeg"), new File("/Users/nate/girl.jpg"));

        // 构建请求 Body
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder();
        requestBodyBuilder.setType(MultipartBody.FORM);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.fileParams.entrySet()) {
                if (entry.getValue() instanceof File) {
                    //添加参数
                    String fileName = entry.getKey();
                    File file = (File) entry.getValue();
                    requestBodyBuilder.addFormDataPart("platform", "android").
                            addFormDataPart("filename", fileName, RequestBody.create(FILE_TYPE_IMAGE, file));
                } else if (entry.getValue() instanceof String) {
//                    RequestBody imageBody2 = RequestBody.create(null, (String) entry.getValue());
//                    requestBody.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""), imageBody2);
                }
            }
        }
        return new Request.Builder().url(url).post(requestBodyBuilder.build()).build();
    }

    private static String guessMimeType(String filePath) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();

        String mimType = fileNameMap.getContentTypeFor(filePath);

        if(TextUtils.isEmpty(mimType)){
            return "application/octet-stream";
        }
        return mimType;
    }
}