package tw.com.lig.module_base.widget;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import tw.com.lig.module_base.R;

import java.lang.ref.WeakReference;

/**
 * 作者：${weihaizhou} on 2017/7/21 14:47
 * <p>
 * 載入提示框
 */
public class LoadingView {

    WeakReference<Context> contextWeak;
    private ViewGroup decorView;//activity的根View
    private ViewGroup loadingView;//
    private boolean isShowing;
    private boolean dismissing;
    private Context mWeakContext;
    private Animation enterAnim;
    private Animation exitAnim;

    private boolean isKeyBackCancelable;
    private boolean isOutSideCancelable;
    private boolean isOutSideEnable;
    private String mLoadMsg;
    private RotateLoading rotateLoading;

    public LoadingView(Builder builder) {
        this.contextWeak = new WeakReference<>(builder.mContext);
        mWeakContext = contextWeak.get();
        if (mWeakContext != null)
            this.isKeyBackCancelable = builder.isKeyBackCancelable;
        this.isOutSideCancelable = builder.isOutSideCancelable;
        this.isOutSideEnable = builder.isOutSideEnable;
        this.mLoadMsg = builder.loadMsg;

        initView();
        initAnim();
    }


    private void initView() {

        LayoutInflater layoutInflater = LayoutInflater.from(mWeakContext);
        decorView = ((Activity) mWeakContext).getWindow().getDecorView().findViewById(android.R.id.content);
        loadingView = (ViewGroup) layoutInflater.inflate(R.layout.layout_dialog_progress, decorView, false);
        rotateLoading = loadingView.findViewById(R.id.progressBar1);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        loadingView.setLayoutParams(layoutParams);

        TextView loadText = loadingView.findViewById(R.id.tv_loading_msg);
        if (mLoadMsg != null) {
            loadText.setVisibility(View.VISIBLE);
            loadText.setText(mLoadMsg);
        }

        setKeyBackCancelable(isKeyBackCancelable);//监听返回键，取消loading;ture 取消：false 不取消
        setOutSideCancelable(isOutSideCancelable);//點击螢幕取消loading ;ture 取消：false 不取消
        setOutSideEnable(isOutSideEnable);//loading 載入中是否拦截螢幕控件點击事件； true 拦截 ：false 不拦截
    }

    private void initAnim() {
        enterAnim = AnimationUtils.loadAnimation(mWeakContext, R.anim.fade_in_center);
        exitAnim = AnimationUtils.loadAnimation(mWeakContext, R.anim.fade_out_center);
    }


    /**
     * 添加View到根视圖
     */
    public void showLoading() {
        if (isShowing()) {
            return;
        }
        onAttached();
        //让View獲取到焦點,測试过程中发現會先执行View的onKeyDown事件，然後再执行Activity的
        loadingView.requestFocus();

    }

    private void onAttached() {
        decorView.addView(loadingView);
        enterAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isShowing = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        loadingView.startAnimation(enterAnim);
    }

    public void dismissLoading() {

        if (dismissing) {
            return;
        }

        //消失動畫
        exitAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                dismissing = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismissImmediately();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        loadingView.startAnimation(exitAnim);

    }

    public void dismissImmediately() {
        decorView.post(() -> {
            //從根视圖移除
            loadingView.clearFocus();
            loadingView.setFocusable(false);
            setKeyBackCancelable(false);
            setOutSideEnable(false);
            decorView.removeView(loadingView);
            isShowing = false;
            dismissing = false;

        });


    }


    /**
     * 檢測該View是不是已经添加到根视圖
     *
     * @return 如果视圖已经存在該View返回true
     */
    public boolean isShowing() {
        return loadingView.getParent() != null || isShowing;

    }

    public void setKeyBackCancelable(boolean isCancelable) {

        loadingView.setFocusable(isCancelable);
        loadingView.setFocusableInTouchMode(isCancelable);
        if (isCancelable) {
            loadingView.setOnKeyListener(onKeyBackListener);
        } else {
            loadingView.setOnKeyListener(null);
        }
    }


    private View.OnKeyListener onKeyBackListener = (v, keyCode, event) -> {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_DOWN
                && LoadingView.this.isShowing()) {
            LoadingView.this.dismissLoading();
            return true;
        }
        return false;
    };

    /**
     * Called when the user touch on black overlay in order to dismiss the dialog
     */
    private final View.OnTouchListener onCancelableTouchListener = (v, event) -> {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            dismissLoading();
        }
        return true;
    };

    private final View.OnTouchListener onEnableTouchListener = (v, event) -> true;

    /**
     * 點击載入框外面是否dimissLoading ;ture 取消：fasle 不取消
     *
     * @param isCancelable
     */
    public void setOutSideCancelable(boolean isCancelable) {
        if (loadingView != null) {
            View view = loadingView.findViewById(R.id.loading_contain);

            if (isCancelable) {
                view.setOnTouchListener(onCancelableTouchListener);
            } else {
                view.setOnTouchListener(null);
            }
        }

    }

    /**
     * loading 載入中是否其拦截当前頁面的點击事件;true 拦截：false 不拦截
     *
     * @param isEnable
     */
    public void setOutSideEnable(boolean isEnable) {
        if (loadingView != null) {
            View view = loadingView.findViewById(R.id.loading_contain);

            if (isEnable) {
                view.setOnTouchListener(onEnableTouchListener);
            } else {
                view.setOnTouchListener(null);
            }
        }

    }

    public static class Builder {
        private Context mContext;
        private boolean isKeyBackCancelable;
        private boolean isOutSideCancelable;
        private boolean isOutSideEnable;
        private String loadMsg;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setKeyBackCancelable(boolean isCancel) {
            isKeyBackCancelable = isCancel;
            return this;
        }

        public Builder setLoadMsg(String msg) {
            loadMsg = msg;
            return this;
        }

        public Builder setOutSideCancelable(boolean isCancel) {
            isOutSideCancelable = isCancel;
            return this;
        }

        public Builder setOutSideEnable(boolean isEnable) {
            isOutSideEnable = isEnable;
            return this;
        }

        public LoadingView build() {
            return new LoadingView(this);
        }
    }

    public RotateLoading getRotateLoading() {
        return rotateLoading;
    }
}
