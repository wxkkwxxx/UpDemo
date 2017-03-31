package com.wxk.baselibrary.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/3/31
 */
class AlertController {

    private AlertDialog mDialog;
    private Window mWindow;
    private DialogViewHelper mViewHelper;

    public AlertController(AlertDialog dialog, Window window) {

        this.mDialog = dialog;
        this.mWindow = window;
    }

    public void setDialogViewHelper(DialogViewHelper viewHelper){
        this.mViewHelper = viewHelper;
    }

    public AlertDialog getDialog() {
        return mDialog;
    }

    public Window getWindow() {
        return mWindow;
    }

    public void setText(int viewId, CharSequence text){
        mViewHelper.setText(viewId, text);
    }

    public <T extends View> T getView(int viewId) {
        return mViewHelper.getView(viewId);
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mViewHelper.setOnClickListener(viewId, listener);
    }

    public static class AlertParams {

        public Context mContext;
        public int mThemeResId;

        //点击空白是否可以取消
        public boolean mCancelable = true;

        //dialog cancel监听
        public DialogInterface.OnCancelListener mOnCancelListener;
        //dialog dismiss监听
        public DialogInterface.OnDismissListener mOnDismissListener;
        //dialog key监听
        public DialogInterface.OnKeyListener mOnKeyListener;

        // 布局View
        public View mView;
        // 布局layout id
        public int mViewLayoutResId;

        // 存放字体的修改
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();
        // 存放点击事件
        public SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();

        //默认宽度
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        //默认高度
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        //动画
        public int mAnimations = 0;
        //位置
        public int mGravity = Gravity.CENTER;

        public AlertParams(Context context, int themeResId) {
            mContext = context;
            mThemeResId = themeResId;
        }

        /**
         * 绑定和设置参数
         *
         * @param mAlert
         */
        public void apply(AlertController mAlert) {

            //设置布局
            DialogViewHelper helper = null;
            if (mViewLayoutResId != 0) {

                helper = new DialogViewHelper(mContext, mViewLayoutResId);
            }
            if (mView != null) {

                helper = new DialogViewHelper();
                helper.setContentView(mView);
            }
            if (helper == null) {

                throw new IllegalArgumentException("you need to apply contentView first!");
            }

            //给dialog设置布局
            mAlert.getDialog().setContentView(helper.getContentView());

            //设置辅助类
            mAlert.setDialogViewHelper(helper);

            //设置文本
            int textArraySize = mTextArray.size();
            for (int i = 0; i < textArraySize; i++) {

                mAlert.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }

            //设置点击事件
            int clickArraySize = mClickArray.size();
            for (int i = 0; i < clickArraySize; i++) {

                mAlert.setOnClickListener(mClickArray.keyAt(i), mClickArray.valueAt(i));
            }

            //配置参数、效果
            Window window = mAlert.getWindow();
            window.setGravity(mGravity);

            if (mAnimations != 0) {
                window.setWindowAnimations(mAnimations);
            }

            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = mWidth;
            layoutParams.height = mHeight;
            window.setAttributes(layoutParams);
        }
    }
}
