package com.wxk.baselibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wxk.baselibrary.R;
import com.wxk.baselibrary.ioc.ViewUtils;
import com.wxk.baselibrary.permission.PermissionConstants;
import com.wxk.baselibrary.permission.PermissionFailed;
import com.wxk.baselibrary.permission.PermissionHelper;

/**
 * Created by Administrator on 2017/3/31
 */

public abstract class BaseActivity extends AppCompatActivity{

    protected String TAG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        TAG = getChildName();
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

    protected abstract String getChildName();

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
     */
    protected <T extends View> T viewById(int viewId) {
        return (T) findViewById(viewId);
    }

    /**
     * 统一处理请求权限失败回调
     */
    @PermissionFailed(requestCode = PermissionConstants.REQUEST_CODE_COMMON_FAILED)
    public void requestPermissionFailed(){

        PermissionHelper.showAskRequestDialog(this,
                getString(R.string.perm_tip),
                R.string.setting, R.string.cancel, null);
    }

    /**
     * 权限回调处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        PermissionHelper.requestPermissionsResult(this, requestCode, permissions);
    }
}
