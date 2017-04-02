package com.wxk.baselibrary.http;

import android.content.Context;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/2
 */

public interface EngineCallBack {

    void onPreExecute(Context context, Map<String, Object> params);

    void onError(Exception e);

    void onSuccess(String result);

    //默认
    EngineCallBack DEFAULT_CALL_BACK = new EngineCallBack() {
        @Override
        public void onPreExecute(Context context, Map<String, Object> params) {

        }

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }
    };
}
