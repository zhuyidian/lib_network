package com.dunn.net;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import com.dunn.net.helper.OkHttpHelper;
import com.dunn.net.helper.RequestParams;
import com.dunn.net.helper.exception.OkHttpException;
import com.dunn.net.helper.listener.DisposeDataListener;
import com.dunn.net.helper.listener.RequestUploadProgressListener;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initValue();
        initListen();

        uploadFileProcessTest();
    }

    private void initValue(){

    }

    private void initListen(){

    }

    private void uploadFileTest(){
        String url = "https://api.saiwuquan.com/api/upload";
        File file = new File(Environment.getExternalStorageDirectory(), "Architect_Day27.rar");
        if(!file.exists()){
            Log.e("lib_network","file is no!!!!!!!!");
            return;
        }
        Log.e("lib_network","url="+url+", file="+file.getPath());
        try {
            RequestParams params = new RequestParams();
            params.put(file.getName(), file);
            OkHttpHelper.uploadRequest(url,params, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    Log.e("lib_network","onSuccess responseObj="+responseObj);
                }

                @Override
                public void onFailure(Object reasonObj) {
                    Log.e("lib_network","onFailure="+((OkHttpException)reasonObj).toString());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.e("lib_network","e="+e);
        }
    }

    private void uploadFileProcessTest(){
        String url = "https://api.saiwuquan.com/api/upload";
        File file = new File(Environment.getExternalStorageDirectory(), "Architect_Day27.rar");
        if(!file.exists()){
            Log.e("lib_network","file is no!!!!!!!!");
            return;
        }
        Log.e("lib_network","url="+url+", file="+file.getPath());
        try {
            RequestParams params = new RequestParams();
            params.put(file.getName(), file);
            OkHttpHelper.uploadRequest(url,params, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    Log.e("lib_network","onSuccess responseObj="+responseObj);
                }

                @Override
                public void onFailure(Object reasonObj) {
                    Log.e("lib_network","onFailure="+((OkHttpException)reasonObj).toString());
                }
            }, new RequestUploadProgressListener() {
                @Override
                public void onProgress(long total, long current) {
                    Log.e("lib_network","onProgress total="+total+", current="+current);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.e("lib_network","e="+e);
        }
    }
}
