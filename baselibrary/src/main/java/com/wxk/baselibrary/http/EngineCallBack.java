package com.wxk.baselibrary.http;

/**
 * Created by Administrator on 2017/4/2
 */

public interface EngineCallBack {

    void onError(Exception e);

    void onSuccess(String result);

    //默认
    EngineCallBack DEFAULT_CALL_BACK = new EngineCallBack() {
        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }
    };
}
