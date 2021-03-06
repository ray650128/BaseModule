package tw.com.lig.module_base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

//import android.support.v4.view.ViewPager;

/**
 * Created by ljk on 2017/7/21.
 */

public class MyViewPager extends ViewPager {
    int startX = 0;
    int startY = 0;
    private boolean isScrollable=false;

    public MyViewPager(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);// 不要攔截,
                // 這樣是為了保證ACTION_MOVE調用
                startX = (int) ev.getRawX();
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:

                int endX = (int) ev.getRawX();
                int endY = (int) ev.getRawY();

                if (Math.abs(endX - startX) > Math.abs(endY - startY)) {// 左右滑動
                    if (endX > startX) {// 右劃
                        if (getCurrentItem() == 0) {// 第一個頁面, 需要父控件攔截
//                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else {// 左劃
                        if (getCurrentItem() == 2) {// (話題)
                            // 需要攔截
//                            getParent().requestDisallowInterceptTouchEvent(false);
                            if (!isScrollable){
                                return false;
                            }
                        }
                    }
                } else {// 上下滑動
                  /*  if (mListener != null) {
                        if (endY >= startY) {// 向下滑動
                            mListener.canPullDown();
                        } else {// 向上滑動
                            mListener.canPullUp();
                        }

                    }*/

//                    getParent().requestDisallowInterceptTouchEvent(false);

                }

                break;

            default:
                break;
        }
        // getParent().requestDisallowInterceptTouchEvent(true);

        return super.dispatchTouchEvent(ev);
    }

    public boolean isScrollable() {
        return isScrollable;
    }

    public void setScrollable(boolean scrollable) {
        isScrollable = scrollable;
    }
}
