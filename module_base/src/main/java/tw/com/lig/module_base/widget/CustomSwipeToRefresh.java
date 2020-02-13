package tw.com.lig.module_base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Created by ljk on 2017/8/28.
 */

public class CustomSwipeToRefresh extends SwipeRefreshLayout {

    private int mTouchSlop;
    private float mPrevX;

    public CustomSwipeToRefresh(Context context) {
        super(context);
        //判断用户在进行滑动操作的最小距離
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public CustomSwipeToRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
        //判断用户在进行滑动操作的最小距離
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(event).getX();
                break;

            case MotionEvent.ACTION_MOVE:
                float eventX = event.getX();
                //獲取水平移动距離
                float xDiff = Math.abs(eventX - mPrevX);
                //当水平移动距離大于滑动操作的最小距離的時候就认為进行了横向滑动
                //不进行事件拦截,并將这個事件交给子View处理
                if (xDiff > mTouchSlop) {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(event);
    }

}
