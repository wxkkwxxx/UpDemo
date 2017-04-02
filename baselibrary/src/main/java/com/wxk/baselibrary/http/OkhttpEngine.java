package com.wxk.baselibrary.http;

import android.content.Context;

import com.wxk.baselibrary.log.LogUtils;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/2
 */

public class OkHttpEngine implements IHttpEngine {

    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    @Override
    public void get(Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {

        url = HttpUtils.jointParams(url, params);
        LogUtils.e("OkHttpEngine->get请求路径:", url);

        Request.Builder builder = new Request.Builder().url(url).tag(context);
        builder.method("GET", null);
        Request request = builder.build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.e("OkHttpEngine->get返回参数:", result);

                callBack.onSuccess(result);
            }
        });
    }

    @Override
    public void post(Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {

        final String jointUrl = HttpUtils.jointParams(url, params);
        LogUtils.e("OkHttpEngine->post请求路径:", jointUrl);

        RequestBody requestBody = appendBody(params);
        Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .post(requestBody)
                .build();

        //不在主线程中
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.e("OkHttpEngine->post返回参数:", result);

                callBack.onSuccess(result);
            }
        });
    }

    private RequestBody appendBody(Map<String, Object> params) {

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        addParams(builder, params);
        return builder.build();
    }

    private void addParams(MultipartBody.Builder builder, Map<String, Object> params) {

        if(params != null && !params.isEmpty()){

            for (String key : params.keySet()){

                builder.addFormDataPart(key, params.get(key) + "");
                Object value = params.get(key);
                if(value instanceof File){
                    File file = (File) value;
                    builder.addFormDataPart(key, file.getName(), RequestBody
                            .create(MediaType.parse(guessMimeType(file.
                                    getAbsolutePath())), file));
                }else if(value instanceof List){

                    try {
                        List<File> listFiles = (List<File>) value;
                        for (int i = 0; i < listFiles.size(); i++) {
                            File file = listFiles.get(i);
                            builder.addFormDataPart(key + i, file.getName(), RequestBody
                                    .create(MediaType.parse(guessMimeType(file
                                    .getAbsolutePath())), file));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {

                    builder.addFormDataPart(key, value + "");
                }
            }
        }
    }

    /**
     * 判断文件类型
     */
    private String guessMimeType(String path) {

        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if(contentTypeFor == null){
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}

