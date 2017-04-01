package com.wxk.framelibrary;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wxk.baselibrary.navigationBar.AbsNavigationBar;

/**
 * Created by Administrator on 2017/4/1
 */

public class CommonNavigationBar extends AbsNavigationBar<CommonNavigationBar.Builder.CommonNavigationBarParams>{

    public CommonNavigationBar(CommonNavigationBar.Builder.CommonNavigationBarParams params) {
        super(params);
    }

    @Override
    public int getLayoutId() {
        return R.layout.navigation_bar;
    }

    @Override
    public void applyView() {

        setText(R.id.title, getParams().mTitle);
        setText(R.id.right_text, getParams().mRightText);
        setRightRes(R.id.right_text, getParams().mRightRes);

        setOnClickListener(R.id.right_text, getParams().mRightClickListener);
        setOnClickListener(R.id.back, getParams().mLeftClickListener);
    }

    public static class Builder extends AbsNavigationBar.Builder{

        public CommonNavigationBarParams P;

        public Builder(Context context) {
            super(context, null);
            P = new CommonNavigationBarParams(context, null);
        }

        public Builder(Context context, ViewGroup parent) {
            super(context, parent);
            P = new CommonNavigationBarParams(context, parent);
        }

        //设置所有效果
        public CommonNavigationBar.Builder setTitle(String title){

            P.mTitle = title;
            return this;
        }

        public CommonNavigationBar.Builder setRightText(String rightText){

            P.mRightText = rightText;
            return this;
        }

        public CommonNavigationBar.Builder setRightIcon(int rightRes){

            P.mRightRes = rightRes;
            return this;
        }

        public CommonNavigationBar.Builder setRightClickListener(View.OnClickListener rightClickListener){

            P.mRightClickListener = rightClickListener;
            return this;
        }

        public CommonNavigationBar.Builder setLeftClickListener(View.OnClickListener leftClickListener){

            P.mLeftClickListener = leftClickListener;
            return this;
        }

        @Override
        public CommonNavigationBar build() {
            CommonNavigationBar navigationBar = new CommonNavigationBar(P);
            return navigationBar;
        }

        public static class CommonNavigationBarParams extends AbsNavigationBar.Builder.AbsNavigationParams{

            public String mTitle;
            public String mRightText;
            public int mRightRes;
            public View.OnClickListener mRightClickListener;
            public View.OnClickListener mLeftClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity)mContext).finish();
                }
            };

            public CommonNavigationBarParams(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }
    }
}
