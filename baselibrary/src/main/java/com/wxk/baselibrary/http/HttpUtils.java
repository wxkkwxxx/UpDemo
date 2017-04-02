package com.wxk.baselibrary.http;

import android.content.Context;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/2
 */

public class HttpUtils{

    private String mUrl;
    private int mType = POST_TYPE;
    private static final int POST_TYPE = 0x0011;
    private static final int GET_TYPE = 0x0012;
    private Map<String, Object> mParams;

    private Context mContext;

    public HttpUtils(Context context){
        this.mContext = context;
        mParams = new HashMap<>();
    }

    public static HttpUtils with(Context context){
        return new HttpUtils(context);
    }

    public HttpUtils url(String url){
        this.mUrl = url;
        return this;
    }

    public HttpUtils post(){
        this.mType = POST_TYPE;
        return this;
    }

    public HttpUtils get(){
        this.mType = GET_TYPE;
        return this;
    }

    public HttpUtils addParams(String key, Object object){
        this.mParams.put(key, object);
        return this;
    }

    public HttpUtils addParams(Map<String, Object> params){
        this.mParams.putAll(params);
        return this;
    }

    public void execute(EngineCallBack callBack){

        if(callBack == null){
            callBack = EngineCallBack.DEFAULT_CALL_BACK;
        }
        if(mType == POST_TYPE){

            post(mContext, mUrl, mParams, callBack);
        }else {

            get(mContext, mUrl, mParams, callBack);
        }
    }

    public void execute(){
        execute(null);
    }

    //默认OkHttp
    private static IHttpEngine mHttpEngine = new OkHttpEngine();

    public void exchangeEngine(IHttpEngine httpEngine){
        mHttpEngine = httpEngine;
    }

    private void get(Context context, String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.get(context, url, params, callBack);
    }

    private void post(Context context, String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.post(context, url, params, callBack);
    }

    public static String jointParams(String url, Map<String, Object> params) {

        if(params == null || params.size() <= 0){
            return url;
        }
        StringBuffer stringBuffer = new StringBuffer(url);
        if(!url.contains("?")){
            stringBuffer.append("?");
        }else {
            if(!url.equals("?")){
                stringBuffer.append("&");
            }
        }

        for (Map.Entry<String, Object> entry: params.entrySet()) {

            stringBuffer.append(entry.getKey() + "=" + entry.getValue() +"&");
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer.toString();
    }

    //解析一个类上面的信息
    public static Class<?> analysisClazzInfo(Object object){

        Type type = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType)type).getActualTypeArguments();
        return (Class<?>)params[0];
    }

    public void cancelRequest(Object tag){

//        mHttpEngine.cancelRequest(tag);
    }
}
