package com.wxk.framelibrary.http;

import android.content.Context;
import android.text.TextUtils;

import com.wxk.baselibrary.http.EngineCallBack;
import com.wxk.baselibrary.http.HttpUtils;
import com.wxk.baselibrary.http.IHttpEngine;
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
    public void get(final boolean cache, Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {

        final String finalUrl = HttpUtils.jointParams(url, params);
        LogUtils.e("OkHttpEngine->get请求路径:", finalUrl);

        //判断是否需要缓存
        if(cache){

            String resultJson = CacheUtils.getCacheResultJson(finalUrl);
            if(!TextUtils.isEmpty(resultJson)){

                LogUtils.e("已读到缓存");
                callBack.onSuccess(resultJson);
            }
        }

        Request.Builder builder = new Request.Builder().url(finalUrl).tag(context);
        builder.method("GET", null);
        Request request = builder.build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resultJson = response.body().string();

                // 获取数据之后会执行成功方法
                if (cache) {
                    String cacheResultJson = CacheUtils.getCacheResultJson(finalUrl);
                    if (!TextUtils.isEmpty(resultJson)) {
                        // 比对内容
                        if (resultJson.equals(cacheResultJson)) {
                            // 内容一样，不需要执行成功成功方法刷新界面
                            LogUtils.e("数据和缓存一致：", resultJson);
                            return;
                        }
                    }
                }

                callBack.onSuccess(resultJson);
                LogUtils.e("OkHttpEngine->get返回数据: ", resultJson);
                if (cache) {
                    CacheUtils.cacheData(finalUrl, resultJson);
                }
            }
        });
    }

    @Override
    public void post(final boolean cache, Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {

        final String finalUrl = HttpUtils.jointParams(url, params);
        LogUtils.e("OkHttpEngine->post请求路径:", finalUrl);

        //判断是否需要缓存
        if(cache){

            String resultJson = CacheUtils.getCacheResultJson(finalUrl);
            if(!TextUtils.isEmpty(resultJson)){

                LogUtils.e("已读到缓存");
                callBack.onSuccess(resultJson);
            }
        }

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
                String resultJson = response.body().string();
                // 获取数据之后会执行成功方法
                if (cache) {
                    String cacheResultJson = CacheUtils.getCacheResultJson(finalUrl);
                    if (!TextUtils.isEmpty(resultJson)) {
                        // 比对内容
                        if (resultJson.equals(cacheResultJson)) {
                            // 内容一样，不需要执行成功成功方法刷新界面
                            LogUtils.e("数据和缓存一致：", resultJson);
                            return;
                        }
                    }
                }

                callBack.onSuccess(resultJson);
                LogUtils.e("OkHttpEngine->post返回数据: ", resultJson);
                if (cache) {
                    CacheUtils.cacheData(finalUrl, resultJson);
                }
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

