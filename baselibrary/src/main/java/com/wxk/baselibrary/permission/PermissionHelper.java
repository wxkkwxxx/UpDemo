package com.wxk.baselibrary.permission;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import java.util.List;

/**
 * Created by Administrator on 2017/4/1
 */

public class PermissionHelper {

    public static final int SETTINGS_REQ_CODE = 16061;

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

    public static void showAskRequestDialog(final Object object, String rationale,
                                            @StringRes int positiveButton,
                                            @StringRes int negativeButton,
                                            @Nullable DialogInterface.OnClickListener negativeButtonOnClickListener) {

        final Activity activity = PermissionUtils.getActivity(object);

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setMessage(rationale)
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        startAppSettingsScreen(object, intent);
                    }
                })
                .setNegativeButton(negativeButton, negativeButtonOnClickListener)
                .show();
        Button positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        positive.setTextColor(Color.parseColor("#6699ff"));
        negative.setTextColor(Color.BLACK);
    }

    private static void startAppSettingsScreen(Object object,
                                               Intent intent) {
        if (object instanceof Activity) {
            ((Activity) object).startActivityForResult(intent, SETTINGS_REQ_CODE);
        } else if (object instanceof Fragment) {
            ((Fragment) object).startActivityForResult(intent, SETTINGS_REQ_CODE);
        } else if (object instanceof android.app.Fragment) {
            ((android.app.Fragment) object).startActivityForResult(intent, SETTINGS_REQ_CODE);
        }
    }
}
