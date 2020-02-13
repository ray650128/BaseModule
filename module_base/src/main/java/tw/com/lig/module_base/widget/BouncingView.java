package tw.com.lig.module_base.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import tw.com.lig.module_base.R;


/**
 * 描述：
 * 作者： 天天童话丶
 * 時間： 2017/4/10.
 */
public class BouncingView extends View {

    private Paint mPaint;
    private int mArcHeight; //当前的弧高
    private int mMaxArcHeight; //弧高最大高度
    private Status mStatus = Status.NONE;
    private Path mPath = new Path();
    private MyAnimationListener animationListener;

    public enum Status {
        NONE,
        STATUS_SMOOTH_UP,
        //        STATUS_UP,
        STATUS_DOWN,
    }

    public BouncingView(Context context) {
        this(context, null);
    }

    public BouncingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BouncingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(android.R.color.white));
        mMaxArcHeight = getResources().getDimensionPixelSize(R.dimen.arc_max_height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int currentPointY = 0;
        //不断控制这個Y值的變化
        switch (mStatus) {
            case NONE:
                currentPointY = 0;
                break;
            case STATUS_SMOOTH_UP:
                /**currentPointY的值 -- 跟mArcHeight的變化是一样的
                 * getHeight()~0        0~mMaxArcHeight
                 * currentPointY/getHeight = 1 - mArcHeight/mMaxArcHeight
                 */
                currentPointY = (int) (getHeight() * (1 - (float) mArcHeight / mMaxArcHeight) + mMaxArcHeight);
                break;
            case STATUS_DOWN:
                currentPointY = mMaxArcHeight;
                break;
        }

        mPath.reset();
        mPath.moveTo(0, currentPointY);
        mPath.quadTo(getWidth() / 2, currentPointY - mArcHeight, getWidth(), currentPointY);//贝塞尔曲线 锚點座標 终點座標
        mPath.lineTo(getWidth(), getHeight());
        mPath.lineTo(0, getHeight());
        mPath.close();

        canvas.drawPath(mPath, mPaint);
    }

    public void show(){
        if (animationListener != null){
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //recyclerView 動畫显示
                    animationListener.showContent();
                }
            }, 500);
        }

        mStatus = Status.STATUS_SMOOTH_UP;
        final ValueAnimator animator = ValueAnimator.ofInt(0, mMaxArcHeight);
        animator.setDuration(700);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mArcHeight = (int) valueAnimator.getAnimatedValue();
                if (mArcHeight == mMaxArcHeight){
                    bounce();
                }
                invalidate();
            }
        });
        animator.start();
    }

    /**
     * 回彈效果
     */
    private void bounce() {
        mStatus = Status.STATUS_DOWN;
        final ValueAnimator animator = ValueAnimator.ofInt(mMaxArcHeight, 0);
        animator.setDuration(400);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mArcHeight = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    public void setMyAnimationListener(MyAnimationListener listener){
        this.animationListener = listener;
    }

    public interface MyAnimationListener{
        void showContent();
    }
}
