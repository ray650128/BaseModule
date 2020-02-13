package tw.com.lig.module_base.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import tw.com.lig.module_base.R;


/**
 * 消息气泡顶部的一個三角形的View
 */
public class AngleView extends View{
    private int mWidth,mHeight;
    private Paint  paint;
    private Path path;

    public AngleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(getResources().getColor(R.color.white));
        path=new Path();



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.moveTo(0,mHeight);

        path.lineTo(mWidth/2,0);
        path.lineTo(mWidth,mHeight);
        path.close();
        canvas.drawPath(path,paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth=w;
        mHeight=h;
    }
    public void setColor(int color){
        paint.setColor(color);
        invalidate();
    }
}
