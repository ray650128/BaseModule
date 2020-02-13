package tw.com.lig.module_base.utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;

import java.lang.reflect.Method;


/**
 * 輸入鍵盤擋住輸入框的幫助類
 * <p>
 * 華為及google 虛擬鍵盤適配帶解決*******
 */
public class KeyBoardHelper {

    private Context mContext;
    private int screenHeight;
    private View mContentView;
    private View mMoveView;
    private int blankHeight = 0;
    private int distKeyBoardBottom;//距離軟鍵盤的距離預設10dp
    private View content;

    private int[] mMoveViewPosition;


    public KeyBoardHelper(Builder builder) {
        this.mContext = builder.mContext;
        this.mMoveView = builder.mMoveView;
        this.mContentView = builder.mContentView;
        initConfig();
    }

    private void initConfig() {
        screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        //豎屏
        if (((Activity) mContext).getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        content = ((Activity) mContext).findViewById(android.R.id.content);
        content.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);

        distKeyBoardBottom = (int) TDevice.dp2px(10);
        //初始化界面預設不彈出鍵盤
        ((Activity) mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


    }


    private OnGlobalLayoutListener onGlobalLayoutListener = new OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {//當鍵盤彈出隱藏的時候會調用此方法。
            Rect rect = new Rect();
            //獲取當前界面可視部分
            ((Activity) mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            //newBlankheight(鍵盤的高度) = screenHeight-rect.bottom(當前界面可視部分)
            //此處就是用來獲取鍵盤的高度的， 在鍵盤沒有彈出的時候 此高度為0 鍵盤彈出的時候為一個正數
//            int windowVisibleDisplayH = screenHeight - getBottomKeyboardHeight();
            int newBlankheight = screenHeight - rect.bottom;
            if (newBlankheight != blankHeight) {
                if (newBlankheight > blankHeight) {
                    // keyboard 打開
                    onKeyboardOpened(newBlankheight);
                } else {
                    // keyboard 關閉
                    onKeyboardClosed();
                }
            }
            blankHeight = newBlankheight;

        }
    };

    private void onKeyboardClosed() {
        final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mContentView.getLayoutParams();
        if (lp.topMargin != 0) {
            ValueAnimator anim = ValueAnimator.ofInt(offset, 0);
            anim.setDuration(100);
            anim.addUpdateListener(animation -> {
                lp.topMargin = (Integer) animation.getAnimatedValue();
                mContentView.setLayoutParams(lp);
            });
            anim.start();
        }

    }

    private int offset;

    private void onKeyboardOpened(int keyBoardheight) {
        mMoveViewPosition = new int[2];
        mMoveView.getLocationOnScreen(mMoveViewPosition);
        //screenHeight - position[1] -->需要移動的區域距離螢幕底部的距離
        //(screenHeight - position[1]) - mMoveView.getHeight() - distKeyBoardBottom --->再此距離上需要移動的距離
        int windowVisibleDisplayH = screenHeight - getBottomKeyboardHeight();
        int toBottomHeight = screenHeight - mMoveViewPosition[1] - mMoveView.getHeight() - distKeyBoardBottom;
        if (toBottomHeight <= keyBoardheight) {
            offset = toBottomHeight - keyBoardheight;
            final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mContentView.getLayoutParams();
            ValueAnimator anim = ValueAnimator.ofInt(0, offset);
            anim.setDuration(100);
            anim.addUpdateListener(animation -> {
                lp.topMargin = (Integer) animation.getAnimatedValue();
                mContentView.setLayoutParams(lp);

            });
            anim.start();


//            Logger.e("position[1]" + mMoveViewPosition[1] + "-----keyBoardheight" + keyBoardheight + "----windowVisibleDisplayH" + windowVisibleDisplayH + "---getBottomKeyboardHeight()" + getBottomKeyboardHeight());

        }
    }

    /**
     * 移除監聽
     */
    public void onRelease() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            content.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    }


    /**
     * @return 底部的虛擬列的高度
     */
    public int getBottomKeyboardHeight() {
        int screenHeight = getAccurateScreenDpi()[1];
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);//去除底部虛擬列之後的metric
        return screenHeight - dm.heightPixels;
    }

    /**
     * 獲取實際的螢幕尺寸,所有的連同底部虛擬列
     */
    public int[] getAccurateScreenDpi() {
        int[] screenWH = new int[2];
        Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            Class<?> c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            screenWH[0] = dm.widthPixels;
            screenWH[1] = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenWH;
    }


    public static class Builder {
        private Context mContext;
        private View mContentView;
        private View mMoveView;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setContentView(View view) {
            mContentView = view;
            return this;
        }

        public Builder setMoveView(View view) {
            mMoveView = view;
            return this;
        }

        public KeyBoardHelper build() {
            if (mContentView == null)
                throw new IllegalStateException("移動的區域，必須有");

            if (mMoveView == null)
                throw new IllegalStateException("用來參照軟鍵盤之間的距離的，所以是必須有");

            return new KeyBoardHelper(this);
        }

    }


}
