package com.wxk.baselibrary.navigationBar;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/4/1
 * 基类
 */

public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.AbsNavigationParams> implements INavigationBar{

    private View mNavigationBarView;
    public P mParams;

    public AbsNavigationBar(P params) {
        this.mParams = params;
        createAndBindView();
    }

    private void createAndBindView() {

        //判断是否制定了父布局,如果未指定则获取系统布局R.layout.screen_simple
        //这里不获取android.R.id.content主要是因为如果activity的根布局是RelativeLayout会出现重叠的效果
        if(mParams.mParent == null){

            ViewGroup root = (ViewGroup) ((Activity) mParams.mContext).getWindow().getDecorView();
            mParams.mParent = (ViewGroup) root.getChildAt(0);
        }

        mNavigationBarView = LayoutInflater.from(mParams.mContext).inflate(getLayoutId(), mParams.mParent, false);
        mParams.mParent.addView(mNavigationBarView, 0);

        applyView();
    }

    public P getParams() {
        return mParams;
    }

    protected void setText(int viewId, String text){

        TextView tv = getView(viewId);
        if (!TextUtils.isEmpty(text)) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }
    }

    protected void setOnClickListener(int viewId, View.OnClickListener listener){

        View view = getView(viewId);
        view.setOnClickListener(listener);
    }

    protected void setRightRes(int viewId, int rightRes){

        View view = getView(viewId);
        if(rightRes != 0){
            view.setVisibility(View.VISIBLE);
            view.setBackgroundResource(rightRes);
        }
    }

    private <V extends View> V getView(int viewId) {

        View view = mNavigationBarView.findViewById(viewId);
        return (V) view;
    }

    public abstract static class Builder{

        public Builder(Context context, ViewGroup parent) {
        }

        public abstract AbsNavigationBar build();

        public static class AbsNavigationParams{

            public Context mContext;
            public ViewGroup mParent;

            public AbsNavigationParams(Context context, ViewGroup parent) {
                this.mParent = parent;
                this.mContext = context;
            }
        }
    }
}
