package tw.com.lig.module_base.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class AndroidBug5497Workaround {
    private  HeightChangeListener heightChangeListener;
    public static void assistActivity(View content,HeightChangeListener heightChangeListener) {
        new AndroidBug5497Workaround(content);
    }
    public static void assistActivity(View content) {
        new AndroidBug5497Workaround(content);
    }
//    public  void assistActivity(View content,HeightChangeListener heightChangeListener) {
//        new AndroidBug5497Workaround(content);
//        this.heightChangeListener=heightChangeListener;
//    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private ViewGroup.LayoutParams frameLayoutParams;
    public AndroidBug5497Workaround(View content, HeightChangeListener heightChangeListener) {
        this.heightChangeListener=heightChangeListener;
        if (content != null) {
            mChildOfContent = content;
            mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    possiblyResizeChildOfContent();
                }
            });
            frameLayoutParams = mChildOfContent.getLayoutParams();
        }
    }

    public AndroidBug5497Workaround(Activity activity, HeightChangeListener heightChangeListener) {
        this.heightChangeListener=heightChangeListener;
        if (activity != null) {
            mChildOfContent = activity.findViewById(android.R.id.content);
            mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    possiblyResizeChildOfContent();
                }
            });
            frameLayoutParams = mChildOfContent.getLayoutParams();
        }
    }


    public AndroidBug5497Workaround(View content) {
        if (content != null) {
            mChildOfContent = content;
            mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    possiblyResizeChildOfContent();
                }
            });
            frameLayoutParams = mChildOfContent.getLayoutParams();
        }
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            if(heightChangeListener!=null){
                if(usableHeightPrevious>usableHeightNow){
                    heightChangeListener.onKeyboardOpen();
                }else{
                    heightChangeListener.onKeyboardClose();
                }
            }
            //如果兩次高度不一致
            //將計算的可視高度設定成視圖的高度
            frameLayoutParams.height = usableHeightNow;
            mChildOfContent.requestLayout();//請求重新佈局
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        //計算視圖可視高度
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom);
    }
    public interface HeightChangeListener{
        void onKeyboardOpen();
        void onKeyboardClose();
    }


}
