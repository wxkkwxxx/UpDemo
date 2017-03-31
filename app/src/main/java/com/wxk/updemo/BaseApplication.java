package com.wxk.updemo;

import android.app.Application;

import com.wxk.baselibrary.ExceptionCrashHandler;

/**
 * Created by Administrator on 2017/3/31
 */

public class BaseApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        ExceptionCrashHandler.getInstance().init(this);
    }
}
