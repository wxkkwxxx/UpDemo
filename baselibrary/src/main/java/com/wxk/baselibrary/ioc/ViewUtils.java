package com.wxk.baselibrary.ioc;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/3/31
 */

public class ViewUtils {

    public static void inject(Activity activity) {

        inject(activity, new ViewFinder(activity));
    }

    public static void inject(Fragment fragment, View view) {

        inject(fragment, new ViewFinder(view));
    }

    public static void inject(View view) {

        inject(view, new ViewFinder(view));
    }

    private static void inject(Object object, ViewFinder viewFinder) {

        injectField(object, viewFinder);
        injectEvent(object, viewFinder);
    }

    //注入属性
    private static void injectField(Object object, ViewFinder viewFinder) {

        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {

            ViewById viewById = field.getAnnotation(ViewById.class);
            if (viewById != null) {
                //获取到id
                int value = viewById.value();
                View view = viewFinder.findViewById(value);
                if(view != null){

                    field.setAccessible(true);
                    try {
                        field.set(object, view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //注入方法
    private static void injectEvent(Object object, ViewFinder viewFinder) {

        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {

            OnClick onClick = method.getAnnotation(OnClick.class);
            if(onClick != null){

                int[] value = onClick.value();
                for (int viewId : value) {

                    View view = viewFinder.findViewById(viewId);
                    boolean isCheckNet = method.getAnnotation(CheckNet.class) != null;
                    if(view != null){

                        view.setOnClickListener(new DeclaredOnClickListener(object, method, isCheckNet));
                    }
                }
            }
        }
    }

    /**
     * An implementation of OnClickListener that attempts to lazily load a
     * named click handling method from a parent or ancestor context.
     */
    private static class DeclaredOnClickListener implements View.OnClickListener {
        private final Object mObject;
        private final Method mMethod;
        private final boolean mIsCheckNet;

        public DeclaredOnClickListener(Object object, Method method, boolean isCheckNet) {

            this.mObject = object;
            this.mMethod = method;
            this.mIsCheckNet = isCheckNet;
        }

        @Override
        public void onClick(@NonNull View v) {

            if(mIsCheckNet){

                if(!isNetworkAvailable(v.getContext())){

                    Toast.makeText(v.getContext(), "亲，您的网络不太给力哦!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            try {
                mMethod.setAccessible(true);
                mMethod.invoke(mObject, v);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(
                        "Could not execute non-public method for android:onClick", e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException(
                        "Could not execute method for android:onClick", e);
            }catch (Exception e){
                e.printStackTrace();
                try {
                    mMethod.invoke(mObject, new Object[]{});
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    //判断当前是否有网
    private static boolean isNetworkAvailable(Context context){
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
