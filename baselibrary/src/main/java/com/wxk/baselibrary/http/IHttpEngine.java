package com.wxk.baselibrary.http;

import android.content.Context;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/2
 */

public interface IHttpEngine {

    //get请求
    void get(Context context, String url, Map<String, Object> params, EngineCallBack callBack);

    //post请求
    void post(Context context, String url, Map<String, Object> params, EngineCallBack callBack);

    //下载文件

    //上传文件

    //https证书
}
