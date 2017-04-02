package com.wxk.framelibrary;

import android.content.Context;

import com.google.gson.Gson;
import com.wxk.baselibrary.http.EngineCallBack;
import com.wxk.baselibrary.http.HttpUtils;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/2
 * 添加公共参数
 */

public abstract class HttpCallBack<T> implements EngineCallBack{
    @Override
    public void onPreExecute(Context context, Map<String, Object> params) {

        params.put("key", "b58dd4390521d74743df3e52cac25642");

        onPreExecute();
    }

    //开始执行
    public void onPreExecute(){

    }

    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        T data = (T) gson.fromJson(result, HttpUtils.analysisClazzInfo(this));
        onSuccess(data);
    }

    public abstract void onSuccess(T result);
}
