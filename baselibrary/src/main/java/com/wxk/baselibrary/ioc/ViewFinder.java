package com.wxk.baselibrary.ioc;

import android.app.Activity;
import android.view.View;

/**
 * Created by Administrator on 2017/3/31
 */

public class ViewFinder {

    private Activity mActivity;
    private View mView;

    public ViewFinder(Activity activity) {

        this.mActivity = activity;
    }

    public ViewFinder(View view){

        this.mView = view;
    }

    public View findViewById(int viewId){

        return mActivity != null ? mActivity.findViewById(viewId) : mView.findViewById(viewId);
    }
}
