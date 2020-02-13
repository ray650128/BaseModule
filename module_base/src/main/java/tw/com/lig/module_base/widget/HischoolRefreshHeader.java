package tw.com.lig.module_base.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import tw.com.lig.module_base.R;

public class HischoolRefreshHeader extends RelativeLayout implements RefreshHeader {
    private TextView tvRefresh;

    private ImageView iv_loading;
    private AnimationDrawable mAnimDrawable;
    public HischoolRefreshHeader(Context context) {
        this(context,null);

    }

    public HischoolRefreshHeader(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HischoolRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
   /*     View inflate =*/ inflate(context, R.layout.layout_irecyclerview_classic_refresh_header_view3, this);
        tvRefresh = (TextView)findViewById(R.id.tv_state);
        tvRefresh.setTextColor(context.getResources().getColor(android.R.color.black));
//        ivArrow = (ImageView) findViewById(R.id.ivArrow);

//        ivSuccess = (ImageView) findViewById(R.id.ivSuccess);
        iv_loading=findViewById(R.id.iv_loading);
        mAnimDrawable= (AnimationDrawable) iv_loading.getBackground();
//        addView(inflate,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);

    }


    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
         return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        mAnimDrawable.start();
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        mAnimDrawable.stop();//停止動畫
        if (success){
            tvRefresh.setText("刷新完成");
        } else {
            tvRefresh.setText("刷新失败");
        }
        return 200;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        if (newState==RefreshState.None){
            tvRefresh.setText("下拉刷新");
        }else if(newState==RefreshState.PullDownToRefresh){

        }else if(newState==RefreshState.Refreshing){
            tvRefresh.setText("正在載入");

        }else if(newState==RefreshState.ReleaseToRefresh){
            tvRefresh.setText("鬆開刷新資料");
        }

    }
}
