package com.wxk.updemo;

import android.app.Application;

import com.wxk.baselibrary.ExceptionCrashHandler;
import com.wxk.baselibrary.http.HttpUtils;
import com.wxk.baselibrary.log.LogUtils;
import com.wxk.framelibrary.http.OkHttpEngine;

/**
 * Created by Administrator on 2017/3/31
 */

public class BaseApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        //异常捕捉类
        ExceptionCrashHandler.getInstance().init(this);

        //测试true,正式false
        LogUtils.initParam(true);

        HttpUtils.with(this).init(new OkHttpEngine());
    }
}
