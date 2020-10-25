package com.sample;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.dunn.net.helper.OkHttpHelper;
import com.dunn.net.helper.RequestParams;
import com.dunn.net.helper.exception.OkHttpException;
import com.dunn.net.helper.listener.DisposeDataListener;
import com.dunn.net.helper.listener.RequestUploadProgressListener;

import java.io.File;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button http_btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initValue();
        initListen();

        getRequest();
    }

    private void initValue(){
        http_btn1 = (Button)findViewById(R.id.http_btn1);
    }

    private void initListen(){
        http_btn1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.http_btn1:
                getRequest();
                break;
        }
    }

    private void getRequest(){
//        String url = "https://api.github.com/repos/square/okhttp/contributors";
        String url = "https://api.heweather.com/x3/weather";
//        String url = "http://www.qq.com";
        RequestParams params = new RequestParams();
        OkHttpHelper.getRequest(url,params,new DisposeDataListener() {
            @Override
            public void onSuccess(int code, Object responseObj) {
                Log.e("lib_network","onSuccess code="+code+", responseObj="+responseObj);
            }

            @Override
            public void onFailure(Object reasonObj) {
                Log.e("lib_network","onFailure="+((OkHttpException)reasonObj).toString());
            }
        });
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
                public void onSuccess(int code,Object responseObj) {
                    Log.e("lib_network","onSuccess code="+code+", responseObj="+responseObj);
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
                public void onSuccess(int code,Object responseObj) {
                    Log.e("lib_network","onSuccess code="+code+", responseObj="+responseObj);
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
