package tw.com.lig.module_base.base;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tw.com.lig.module_base.R;
import tw.com.lig.module_base.global.AppContext;
import tw.com.lig.module_base.utils.TDevice;

//import com.airbnb.lottie.LottieAnimationView;
//import com.ldoublem.loadingviewlib.view.LVRingProgress;


/**
 * 作者：${weihaizhou} on 2017/7/31 16:57
 */
public class BaseLayout extends RelativeLayout {
    private TextView tv_msg;
    private TextView mBtn;
    private View mContentView;
    private Context mContext;
    private View mEmptyView;
    private View mErrorView;
    private View mProgressBarView;
    private View mStubView;
    private View mUserDinfedView;
    private int mProgressBarViewBg;
    private boolean addError;
    private boolean addEmpty;
    private boolean addUserDefined;
    private OnBaseLayoutClickListener mOnBaseLayoutClickListener;
//    private LottieAnimationView lottie;
//    private ImageView iv_loading;
    private AnimationDrawable animationDrawable;

    LayoutParams mParams = new LayoutParams(LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT);
//    private  LVRingProgress ring_progress;
//    private RotateLoading rotateLoading;
    private TextView tv_refresh;
    private TextView tv_error;

    public BaseLayout(Builder builder) {
        super(builder.context);
        this.mContentView = builder.view;
        this.mContext = builder.context;
        this.mErrorView = builder.errorView;
        this.mEmptyView = builder.emptyView;
        this.mProgressBarView = builder.progressBarView;
        this.mProgressBarViewBg = builder.progressBarViewBg;
        this.mOnBaseLayoutClickListener = builder.onBaseLayoutClickListener;

        //1.將conentview添加到relativelayout
        if (mContentView == null) {
            throw new IllegalArgumentException("The content view must not null ");
        }
        addView(mContentView, mParams);

        //4.添加进度条
        if (mProgressBarView == null) {
            mProgressBarView = inflate(mContext, R.layout.view_progressbar, null);
        }
//        lottie=mProgressBarView.findViewById(R.id.lottie);
//         rotateLoading=mProgressBarView.findViewById(R.id.rotateloading);
//        rotateLoading.start();
        ImageView iv_loading=mProgressBarView.findViewById(R.id.iv_loading);
        animationDrawable = (AnimationDrawable) iv_loading.getBackground();
        animationDrawable.start();
        //        lottie.useHardwareAcceleration();
//        lottie.playAnimation();

//        ring_progress = mProgressBarView.findViewById(R.id.ring_progress);
//        ring_progress.startAnim();
//        rotateLoading=mProgressBarView.findViewById(R.id.rotateloading);
        if (mProgressBarViewBg > 0) {
            mProgressBarView.setBackgroundResource(mProgressBarViewBg);
        }
        addView(mProgressBarView, mParams);
    }

    public void showContentView() {
//        rotateLoading.stop();
//        lottie.pauseAnimation();
        if (animationDrawable != null) {
            animationDrawable.start();
        }
        mContentView.setVisibility(View.VISIBLE);
        mProgressBarView.setVisibility(View.GONE);
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }
    }

    public void showProgressView() {

//        mProgressBarView.setBackgroundResource(R.color.transparent);
        mProgressBarView.setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.VISIBLE);
//        rotateLoading.start();
//        lottie.playAnimation();
        if (animationDrawable != null) {

            animationDrawable.start();
        }

        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }
    }

    public void showEmptyView(String emptyTipMsg) {
//        rotateLoading.stop();
//        lottie.pauseAnimation();
        if (animationDrawable != null) {
            animationDrawable.stop();
        }
        mContentView.setVisibility(View.GONE);
        mProgressBarView.setVisibility(View.GONE);
        if (mErrorView != null) {
            mErrorView.setVisibility(GONE);
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView = inflate(mContext, R.layout.view_empty_normal, null);
            tv_msg=mEmptyView.findViewById(R.id.tv_msg);
            tv_msg.setText(emptyTipMsg);
        }
        if (!addEmpty) {
            addView(mEmptyView, mParams);
            addEmpty = true;
        }
        //规定空白頁面的點击id為tv_empty
        if (mEmptyView != null && mEmptyView.findViewById(R.id.tv_empty) != null) {
            mEmptyView.findViewById(R.id.tv_empty).setOnClickListener(v -> {
                if (mOnBaseLayoutClickListener != null) {
                    mOnBaseLayoutClickListener.onClickEmpty();
                }
            });
        }

    }

    public void showErrorView() {
//        rotateLoading.stop();
//        lottie.pauseAnimation();
        if (animationDrawable != null) {
            animationDrawable.stop();
        }
        mContentView.setVisibility(View.GONE);
        mProgressBarView.setVisibility(View.GONE);
        if (mEmptyView != null) {
            mEmptyView.setVisibility(GONE);
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(View.VISIBLE);
        } else {
            mErrorView = inflate(mContext, R.layout.view_error, null);


//            tv_refresh.setBackgroundDrawable(getRetryButton);

        }
        tv_refresh = mErrorView.findViewById(R.id.tv_refresh);
        tv_error = mErrorView.findViewById(R.id.tv_error);
        ImageView iv_error = mErrorView.findViewById(R.id.iv_error);
        if (!TDevice.isNetworkConnected(AppContext.getContext())){
            tv_error.setText(getResources().getString(R.string.retry));
            iv_error.setImageResource(R.drawable.http_error);
        }else{
            tv_error.setText(getResources().getString(R.string.error_unknown));
            iv_error.setImageResource(R.drawable.error_cloud);
        }
        if (!addError) {
            addView(mErrorView, mParams);
            addError = true;
        }
        if (mErrorView != null && mErrorView.findViewById(R.id.tv_refresh) != null) {
            mErrorView.findViewById(R.id.tv_refresh).setOnClickListener(v -> {
                if (mOnBaseLayoutClickListener != null) {
                    mOnBaseLayoutClickListener.onClickRetry();
                    retry();
                }
            });
        }
    }

    //自定义view（）
    public void showUserDefinedView(View view) {
        mContentView.setVisibility(View.GONE);
        mProgressBarView.setVisibility(View.GONE);
        if (mEmptyView != null) {
            mEmptyView.setVisibility(GONE);
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }

        if (view != null) {
            //没有mUserDinfedView 才创建
            if (mUserDinfedView == null) {
                mUserDinfedView = view;
            }

            if (mUserDinfedView != null) {
                //只添加一次
                if (!addUserDefined) {
                    addView(mUserDinfedView, mParams);
                    addUserDefined = true;
                }

            }
        } else {
            //传入view==null表示隐藏自定义view頁面 销毁自定义頁面
            //先判断是否有mUserDinfedView
            if (mUserDinfedView != null) {
                mUserDinfedView.setVisibility(GONE);
                removeView(mUserDinfedView);
                mUserDinfedView = null;
                addUserDefined = false;//置換標志位
            }
        }
    }


    public static class Builder {
        private Context context;
        private View view;
        private View emptyView;
        private View errorView;
        private View progressBarView;
        private int progressBarViewBg;
        private LayoutInflater inflater;
        private OnBaseLayoutClickListener onBaseLayoutClickListener;

        public Builder(Context context) {
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public Builder setContentView(View view) {
            this.view = view;
            return this;
        }


        public Builder setEmptyView(int resId) {
            emptyView = inflater.inflate(resId, null);
            return this;
        }

        /*  public Builder setEmptyView(View view) {
              emptyView = view;
              return this;
          }

          public Builder setErrorView(int resId) {
              errorView = inflater.inflate(resId, null);
              return this;
          }

          public Builder setErrorView(View view) {
              errorView = view;
              return this;
          }

          public Builder setProgressView(int resId) {
              progressBarView = inflater.inflate(resId, null);
              return this;
          }

          public Builder setProgressView(View view) {
              progressBarView = view;
              return this;
          }
  */
        public Builder setProgressBarViewBg(int resId) {
            progressBarViewBg = resId;
            return this;
        }

        public Builder setOnBaseLayoutClickListener(OnBaseLayoutClickListener onBaseLayoutClickListener) {
            this.onBaseLayoutClickListener = onBaseLayoutClickListener;
            return this;
        }

        public BaseLayout build() {
            return new BaseLayout(this);
        }
    }

    public interface OnBaseLayoutClickListener {
        /**
         * 错误頁面點击重试
         */
        void onClickRetry();

        /**
         * 空頁面點击
         */
        void onClickEmpty();
    }


    /**
     * 點击重试
     */
    private void retry() {
        mProgressBarView.setBackgroundResource(R.color.color_eee);
        mProgressBarView.setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.GONE);

        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }

    }

    public View getmEmptyView() {
        return mEmptyView;
    }
//    public RotateLoading getProgressView(){
//        return rotateLoading;
//    }
    public TextView getRetryButton(){
        return tv_refresh;
    }

    public TextView getmErrorMsgView() {
        return tv_error;
    }
}
