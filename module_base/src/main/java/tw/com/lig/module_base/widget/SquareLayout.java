package tw.com.lig.module_base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 正方形佈局
 * @author howie
 *
 */
public class SquareLayout extends RelativeLayout {
	public SquareLayout(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
    }  
   
    public SquareLayout(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
   
    public SquareLayout(Context context) {  
        super(context);  
    }  
   
    @SuppressWarnings("unused")  
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
   
              setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));  
   
        // Children are just made to fill our space.  
        int childWidthSize = getMeasuredWidth();  
        int childHeightSize = getMeasuredHeight();  
        //高度和寬度一樣  
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);  
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
    }  
}
