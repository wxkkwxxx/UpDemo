package com.wxk.baselibrary.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/4/1
 */

public class PermissionUtils {

    private static final String TAG = "PermissionUtils";

    private PermissionUtils(){
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isOverMarshmallow(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 获取到要执行的成功的方法
     */
    public static void executeSuccessMethod(Object object, int requestCode) {

        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {

            PermissionSucceed succeed = method.getAnnotation(PermissionSucceed.class);
            if(succeed != null){
                int methodCode = succeed.requestCode();
                if(methodCode == requestCode){

                    executeMethod(object, method);
                }
            }
        }
    }

    /**
     * 获取到要执行的失败的方法
     */
    public static void executeFailedMethod(Object object, int requestCode){

        List<Method> methods = new ArrayList<>();
        Class<?> tempClass = object.getClass();
        while (tempClass != null && !tempClass.getSimpleName().equals("AppCompatActivity")) {
            methods.addAll(Arrays.asList(tempClass.getDeclaredMethods()));
            tempClass = tempClass.getSuperclass();
        }

        for (Method method : methods) {
            PermissionFailed failed = method.getAnnotation(PermissionFailed.class);
            if(failed != null){

                int methodCode = failed.requestCode();
                if(methodCode == requestCode){ //执行重写的失败回调方法

                    executeMethod(object, method);
                    break;
                }else { //执行父类的失败回调方法

                    executeMethod(object, method);
                }
            }
        }
    }

    /**
     * 执行方法
     */
    private static void executeMethod(Object object, Method method) {

        try {
            method.setAccessible(true);
            method.invoke(object, new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有未通过的权限
     */
    public static List<String> getDeniedPermissions(Object object, String[] requestPermissions){

        List<String> deniedPermissions = new ArrayList<>();

        for (String permission : requestPermissions) {
            int status = ContextCompat.checkSelfPermission(getActivity(object), permission);
            if(status == PackageManager.PERMISSION_DENIED){
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions;
    }

    public static Activity getActivity(Object object) {

        if(object instanceof Activity){
            return (Activity) object;
        }
        if(object instanceof Fragment){
            return ((Fragment) object).getActivity();
        }
        return null;
    }
}
