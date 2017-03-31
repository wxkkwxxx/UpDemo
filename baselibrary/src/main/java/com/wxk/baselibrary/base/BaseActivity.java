package com.wxk.baselibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wxk.baselibrary.ioc.ViewUtils;

/**
 * Created by Administrator on 2017/3/31
 */

public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置布局layout
        setContentView(savedInstanceState);

        ViewUtils.inject(this);

        // 初始化头部
        initTitle();

        // 初始化界面
        initView();

        // 初始化数据
        initData();
    }

    // 设置布局layout
    protected abstract void setContentView(@Nullable Bundle savedInstanceState);

    // 初始化头部
    protected abstract void initTitle();

    // 初始化界面
    protected abstract void initView();

    // 初始化数据
    protected abstract void initData();

    /**
     * 启动Activity
     */
    protected void startActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * findViewById
     *
     * @return View
     */
    protected <T extends View> T viewById(int viewId) {
        return (T) findViewById(viewId);
    }
}