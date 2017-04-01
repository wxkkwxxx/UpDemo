package com.wxk.baselibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/31
 */

public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler{

    private static final String TAG = "ExceptionCrashHandler";

    private Context mContext;
    private static ExceptionCrashHandler mInstance;

    //系统默认
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    public static ExceptionCrashHandler getInstance(){

        if(mInstance == null){
            synchronized (ExceptionCrashHandler.class){
                if(mInstance == null){
                    mInstance = new ExceptionCrashHandler();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context){
        this.mContext = context;
        //设置全局异常类为本类
        Thread.currentThread().setUncaughtExceptionHandler(this);

        mDefaultExceptionHandler = Thread.currentThread().getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        //崩溃信息 应用信息、版本号 手机信息 保存当前文件
        String crashFileName = saveInfoToSD(e);

        //  缓存崩溃日志文件
        cacheCrashFile(crashFileName);

        //系统默认处理exception
        mDefaultExceptionHandler.uncaughtException(t, e);
    }

    /**
     * 获取崩溃信息文件
     */
    public File getCrashFile(){

        String crashFileName = mContext.getSharedPreferences("crash",
                Context.MODE_APPEND).getString("CRASH_FILE_NAME", "");
        return new File(crashFileName);
    }

    /**
     * 缓存崩溃日志文件
     */
    private void cacheCrashFile(String fileName){
        SharedPreferences sp = mContext.getSharedPreferences("crash", Context.MODE_APPEND);
        sp.edit().putString("CRASH_FILE_NAME", fileName).apply();
    }

    /**
     * 保存获取的 软件信息，设备信息和出错信息保存在SDCard中
     */
    private String saveInfoToSD(Throwable ex) {
        String fileName = null;
        StringBuffer sb = new StringBuffer();

        // 1. 手机信息 + 应用信息   --> obtainSimpleInfo()
        for (Map.Entry<String, String> entry : obtainSimpleInfo(mContext)
                .entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }

        // 2.崩溃的详细信息
        sb.append(obtainExceptionInfo(ex));

        // 保存文件  6.0 以上需要动态申请权限
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            File dir = new File(mContext.getFilesDir() + File.separator + "crash"
                    + File.separator);

            // 先删除之前的异常信息
            if (dir.exists()) {
                // 删除该目录下的所有子文件
                deleteDir(dir);
            }

            // 再从新创建文件夹
            if (!dir.exists()) {
                dir.mkdir();
            }

            try {
                fileName = dir.toString()
                        + File.separator
                        + getAssignTime("yyyy_MM_dd_HH_mm") + ".txt";
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    /**
     * 获取时间
     */
    private String getAssignTime(String dateFormatStr) {
        DateFormat dataFormat = new SimpleDateFormat(dateFormatStr);
        long currentTime = System.currentTimeMillis();
        return dataFormat.format(currentTime);
    }

    /**
     * 获取一些简单的信息,软件版本，手机版本，型号等信息存放在HashMap中
     */
    private HashMap<String, String> obtainSimpleInfo(Context context) {
        HashMap<String, String> map = new HashMap<>();
        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(
                    context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        map.put("versionName", mPackageInfo.versionName);
        map.put("versionCode", "" + mPackageInfo.versionCode);
        map.put("MODEL", "" + Build.MODEL);
        map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
        map.put("PRODUCT", "" + Build.PRODUCT);
        map.put("MOBILE_INFO", getMobileInfo());
        return map;
    }

    /**
     * 获取手机信息
     */
    public static String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        try {
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name + "=" + value);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    /**
     * 获取系统未捕捉的错误信息
     */
    private String obtainExceptionInfo(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }

    /**
     * 删除目录下的所有文件及子目录下所有文件
     */
    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            // 删除目录中的子目录下
            for (File child : children) {
                child.delete();
            }
        }
        return true;
    }
}
