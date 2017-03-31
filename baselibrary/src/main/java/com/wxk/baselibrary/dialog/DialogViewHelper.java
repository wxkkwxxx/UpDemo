package com.wxk.baselibrary.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/3/31
 * Dialog view的辅助处理类
 */

class DialogViewHelper {

    //防止内存泄漏
    private SparseArray<WeakReference<View>> mViews;

    private View mContentView = null;

    public DialogViewHelper(Context context, int layoutResId) {

        this();
        mContentView = LayoutInflater.from(context).inflate(layoutResId, null);
    }

    public DialogViewHelper() {
        mViews = new SparseArray<>();
    }

    public void setContentView(View contentView) {

        this.mContentView = contentView;
    }

    public void setText(int viewId, CharSequence text) {

        TextView tv = getView(viewId);
        if(tv != null){
            tv.setText(text);
        }
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {

        View view = getView(viewId);
        if(view != null){
            view.setOnClickListener(listener);
        }
    }

    //优化找id次数,处理内存泄漏
    public <T extends View> T getView(int viewId) {

        WeakReference<View> weakReference = mViews.get(viewId);

        View view = null;
        if(weakReference != null)
            view = weakReference.get();

        if (view == null) {
            view = mContentView.findViewById(viewId);
            if (view != null)
                mViews.put(viewId, new WeakReference<>(view));
        }
        return (T) view;
    }

    public View getContentView() {

        return mContentView;
    }
}
