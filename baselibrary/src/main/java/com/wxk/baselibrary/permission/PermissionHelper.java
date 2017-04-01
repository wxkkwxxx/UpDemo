package com.wxk.baselibrary.permission;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Created by Administrator on 2017/4/1
 */

public class PermissionHelper {

    private Object mObject;
    private int mRequestCode;
    private String[] mRequestPermissions;

    public PermissionHelper(Object object) {
        this.mObject = object;
    }

    public static void requestPermission(Activity activity, int requestCode, String[] requestPermission){

        PermissionHelper.with(activity).requestCode(requestCode).requestPermissions(requestPermission);
    }

    public static void requstPermission(Fragment fragment, int requestCode, String[] requestPermission){

        PermissionHelper.with(fragment).requestCode(requestCode).requestPermissions(requestPermission);
    }

    public static PermissionHelper with(Activity activity){

        return new PermissionHelper(activity);
    }

    public static PermissionHelper with(Fragment fragment){

        return new PermissionHelper(fragment);
    }

    public PermissionHelper requestCode(int requestCode){

        this.mRequestCode = requestCode;
        return this;
    }

    public PermissionHelper requestPermissions(String... requestPermissions){

        this.mRequestPermissions = requestPermissions;
        return this;
    }

    public void request(){

        if(!PermissionUtils.isOverMarshmallow()){

            PermissionUtils.executeSuccessMethod(mObject, mRequestCode);
            return;
        }else {

            List<String> permissions = PermissionUtils.getDeniedPermissions(mObject, mRequestPermissions);
            if(permissions.isEmpty()){

                PermissionUtils.executeSuccessMethod(mObject, mRequestCode);
            }else {

                ActivityCompat.requestPermissions(PermissionUtils.getActivity(mObject), mRequestPermissions, mRequestCode);
            }
        }
    }

    public static void requestPermissionsResult(Object object, int requestCode, String[] requestPermissions){

        List<String> permissions = PermissionUtils.getDeniedPermissions(object, requestPermissions);
        if(permissions.isEmpty()){

            PermissionUtils.executeSuccessMethod(object, requestCode);
        }else {

            PermissionUtils.executeFailedMethod(object, requestCode);
        }
    }
}
