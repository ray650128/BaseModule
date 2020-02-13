package tw.com.lig.module_base.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tw.com.lig.module_base.R;
import tw.com.lig.module_base.utils.TDevice;


/**
 * 作者：${weihaizhou} on 2017/6/26 17:12
 */
public class ToolbarLayout extends RelativeLayout {

    protected LinearLayout mTitlebar;
    private TextView mTvTitle;
    private OnClickListener mOnClickListener;
    private ImageView mIvBack;
    private TextView mTvLeft;
//    private ViewGroup mRlLeft;
//
//    private ViewGroup mRlRight;
    private TextView mTvRight;
    private ImageView mIvRight,mIvRight2;

    private LinearLayout mStatusBarHeight;
    private LinearLayout mStatusBar;
    private View titleDivider;

    /*public ToolbarLayout(Context context, View mContentView, boolean hasLeftBack, float statusBarHeight, boolean isToolbarTranparent, boolean isTitleCentered, boolean isTitleWhite) {
        super(context);
    }*/

    public ToolbarLayout(Context context, View contentView, boolean hasLeftBack, float statusBarHeight, boolean isToolbarTransparent, boolean isTitleCentered, boolean isTitleWhite, boolean isTitleRightIconVisible) {
        super(context);
        if (null == contentView) {
            throw new IllegalArgumentException("The content view can not be null.");
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        // titlebar
        mTitlebar = (LinearLayout) inflater.inflate(isTitleCentered?R.layout.layout_center_title:R.layout.layout_house_title, this, false);//todo  把null 改為了 this
        mTvTitle = mTitlebar.findViewById(R.id.tv_title);

        titleDivider = mTitlebar.findViewById(R.id.title_divider);

        mIvBack = mTitlebar.findViewById(R.id.iv_back);

//        if (isTitleWhite){
//            mTvTitle.setTextColor(getResources().getColor(R.color.white));
//            mIvBack.setImageResource(R.drawable.btn_white_nor_black);
//        }
        if (isToolbarTransparent){
            mIvBack.setImageResource(R.drawable.ic_back_trans);
            mTvTitle.setTextColor(getResources().getColor(R.color.white));

        }else{
            mIvBack.setImageResource(R.drawable.ic_back_blue);
            mTvTitle.setTextColor(getResources().getColor(R.color.grey3));


        }
        mTvLeft = mTitlebar.findViewById(R.id.tv_back);
//        mRlLeft = mTitlebar.findViewById(R.id.rl_left);
//
//        mRlRight = mTitlebar.findViewById(R.id.rl_right);
        mTvRight = mTitlebar.findViewById(R.id.tv_right);
        mIvRight = mTitlebar.findViewById(R.id.iv_right);
        mIvRight2=mTitlebar.findViewById(R.id.iv_right2);
        View iv_title_right = mTitlebar.findViewById(R.id.iv_title_right);//title右側的一個小圖標
        if (isTitleRightIconVisible){
            iv_title_right.setVisibility(View.VISIBLE);
        }else{
            iv_title_right.setVisibility(View.GONE);

        }


        mIvBack.setVisibility(hasLeftBack ? VISIBLE : GONE);
        mIvBack.setOnClickListener(v -> {
            if (mOnClickListener != null) {
                mOnClickListener.onLeftClick();
            }
        });
        mIvRight.setOnClickListener(v -> {
            if (mOnClickListener != null) {
                mOnClickListener.onRightClick();
            }
        });
        mTvRight.setOnClickListener(v -> {
            if (mOnClickListener != null) {
                mOnClickListener.onRightClick();
            }
        });

        mStatusBar = mTitlebar.findViewById(R.id.title);

        mStatusBarHeight = mTitlebar.findViewById(R.id.ll_title);


        // content view
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        if(isToolbarTransparent){
            addView(contentView, params);
            mStatusBar.setBackgroundColor(Color.TRANSPARENT);

            addToolbar((int) statusBarHeight);
        }else{
            addToolbar((int) statusBarHeight);
            params.addRule(BELOW, R.id.title);//content below toolbar
            addView(contentView, params);
        }
    }
    private void addToolbar(int statusBarHeight) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mStatusBarHeight.getLayoutParams();
        layoutParams.topMargin = statusBarHeight;
        LayoutParams toolbarParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        toolbarParams.addRule(ALIGN_PARENT_TOP, TRUE);
        addView(mTitlebar, toolbarParams);
    }


    public TextView getmTvRight() {
        return mTvRight;
    }

    /**
     * 居中顯示標題
     *
     * @param title
     */
    public void setTitle(String title) {
        mTvTitle.setVisibility(VISIBLE);
        mTvTitle.setText(title);
//        if (is)
    }
    public  void setTitleTextColor(int color){
        mTvTitle.setTextColor(getResources().getColor(color));
    }
    public void setTitleDividerVisible(){
        titleDivider.setVisibility(View.VISIBLE);
    }

    public void setRightText(String text) {
        mTvRight.setText(text);
        mTvRight.setVisibility(VISIBLE);
    }
    public void setRightTextColor(int  color) {
        mTvRight.setTextColor(color);
        mTvRight.setVisibility(VISIBLE);
    }
    public void setRightTextInvisible(Boolean show){
        if(show){
            mTvRight.setVisibility(VISIBLE);
        }else {
            mTvRight.setVisibility(INVISIBLE);
        }
    }


    public void setLeftTextIcon(String text, int res) {
        if (!TextUtils.isEmpty(text)) {
            mTvLeft.setText(text);
            mTvLeft.setVisibility(VISIBLE);
        }
        if (res != 0) {
            mIvBack.setImageResource(res);
            mIvBack.setVisibility(VISIBLE);
        }
    }
    public void setLeftText(String text){
        if (!TextUtils.isEmpty(text)) {
            mTvLeft.setText(text);
            mTvLeft.setVisibility(VISIBLE);
        }
    }

    /**
     * 這是title 右側文字,圖標在文字左側
     *
     * @param txt
     * @param res
     */
    public void setRightTextLeftIcon(String txt, int res) {
        if (res != 0) {
            Drawable drawable = getResources().getDrawable(res);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvRight.setCompoundDrawables(drawable, null, null, null);
            mTvRight.setCompoundDrawablePadding((int) TDevice.dp2px(5));
            mTvRight.setText(txt);
            mTvRight.setGravity(Gravity.CENTER);
            mTvRight.setVisibility(VISIBLE);
        }
    }

    public void setRightIcon(int icon) {
        if (icon != 0) {
           /* Drawable drawable = getResources().getDrawable(icon);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvRight.setCompoundDrawables(drawable, null, null, null);
            mTvRight.setCompoundDrawablePadding((int) TDevice.dp2px(5));
            mTvRight.setText("");
            mTvRight.setGravity(Gravity.CENTER);
            mTvRight.setVisibility(VISIBLE);*/
           mIvRight.setImageResource(icon);
            mIvRight.setVisibility(View.VISIBLE);
           mIvRight.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (mOnClickListener != null) {
                       mOnClickListener.onRightClick();
                   }
               }
           });
        }
    }
    public void setRightIcon2Invisible(){
        if (mIvRight2 != null) {
            mIvRight2.setVisibility(View.GONE);
        }
    }

    public void setRightIcon2(int icon) {
        if (icon != 0) {
           /* Drawable drawable = getResources().getDrawable(icon);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvRight.setCompoundDrawables(drawable, null, null, null);
            mTvRight.setCompoundDrawablePadding((int) TDevice.dp2px(5));
            mTvRight.setText("");
            mTvRight.setGravity(Gravity.CENTER);
            mTvRight.setVisibility(VISIBLE);*/
            if (mIvRight2 == null) {
                return;
            }
            mIvRight2.setImageResource(icon);
            mIvRight2.setVisibility(View.VISIBLE);
            mIvRight2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onRightClick2();
                    }
                }
            });
        }
    }


    /**
     * 給toolbar 設定顏色
     *
     * @param color
     */
    public void setToolBarColor(int color) {
        mStatusBar.setBackgroundColor(color);
        if (color == Color.TRANSPARENT) {
            mTvTitle.setTextColor(getResources().getColor(R.color.color_999));
        }
    }

    public static class Builder {
        private Context mContext;
        private View mContentView;
        private boolean hasLeftBack;
        private float statusBarHeight;
        private  boolean isToolbarTranparent;
        private boolean isTitleCentered;
        private boolean isTitleWhite;
        private boolean isTitleRightIconVisible;


        public Builder(Context context) {
            mContext = context;
        }

        public Builder setContentView(View view) {
            mContentView = view;
            return this;
        }

        public Builder setLeftBack(boolean b) {
            hasLeftBack = b;
            return this;
        }


        public Builder setStatusBarHeight(float statusBarHeight) {
            this.statusBarHeight = statusBarHeight;
            return this;
        }

        public ToolbarLayout build() {
            return new ToolbarLayout(mContext, mContentView, hasLeftBack, statusBarHeight,isToolbarTranparent,isTitleCentered,isTitleWhite,isTitleRightIconVisible);
        }


        public Builder setToolbarTranparent(boolean isToolbarTranparent) {
            this.isToolbarTranparent = isToolbarTranparent;
            return this;
        }
        public Builder  setTitleCentered(boolean isTitleCentered){
            this.isTitleCentered=isTitleCentered;
            return  this;
        }

        public Builder setTitleWhite(boolean titleWhite) {
            this.isTitleWhite=titleWhite;
            return  this;
        }

        //設定標題旁邊的小圖標
        public Builder setIsTitleRightIconVisible(boolean isTitleRightIconVisible) {
           this. isTitleRightIconVisible=isTitleRightIconVisible;
           return  this;

        }
    }


    public interface OnClickListener {
        void onLeftClick();

        void onRightClick();

        void onRightClick2();
    }

    public void setOnClickListener(OnClickListener listener) {
        this.mOnClickListener = listener;
    }


}
