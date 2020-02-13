package tw.com.lig.module_base.base;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;

/**
 * Created by jpf on 2017/12/28.
 * 懒載入fragment
 */

public abstract  class LazyFragment<P extends BasePresenter>  extends BaseFragment {
    protected View rootView = null;
    private static final String TAG = LazyFragment.class.getSimpleName();


    private boolean mIsFirstVisible = true;

    private boolean isViewCreated = false;

    private boolean currentVisibleState = false;

   /* @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
        }
        initView(rootView);
        return rootView;
    }*/


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 對于默认 tab 和 間隔 checked tab 需要等到 isViewCreated = true 後才可以通过此通知用户可见
        // 这种情況下第一次可见不是在這裡通知 因為 isViewCreated = false 成立,等從别的界面回到這裡後會使用 onFragmentResume 通知可见
        // 對于非默认 tab mIsFirstVisible = true 會一直保持到选择则这個 tab 的時候，因為在 onActivityCreated 會返回 false
        if (isViewCreated) {
            if (isVisibleToUser && !currentVisibleState) {
                dispatchUserVisibleHint(true);
            } else if (!isVisibleToUser && currentVisibleState) {
                dispatchUserVisibleHint(false);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isViewCreated = true;
        // !isHidden() 默认為 true  在調用 hide show 的時候可以使用
        if (!isHidden() && getUserVisibleHint()) {
            dispatchUserVisibleHint(true);
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("LazyFragment",getClass().getSimpleName() + "  onHiddenChanged dispatchChildVisibleState  hidden " + hidden);

        if (hidden) {
            dispatchUserVisibleHint(false);
        } else {
            dispatchUserVisibleHint(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsFirstVisible) {
            if (!isHidden() && !currentVisibleState && getUserVisibleHint()) {
                dispatchUserVisibleHint(true);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // 当前 Fragment 包含子 Fragment 的時候 dispatchUserVisibleHint 内部本身就會通知子 Fragment 不可见
        // 子 fragment 走到這裡的時候自身又會調用一遍 ？
        if (currentVisibleState && getUserVisibleHint()) {
            dispatchUserVisibleHint(false);
        }
    }


    /**
     * 統一处理 显示隐藏
     *
     * @param visible
     */
    private void dispatchUserVisibleHint(boolean visible) {
        //当前 Fragment 是 child 時候 作為快取 Fragment 的子 fragment getUserVisibleHint = true
        //但当父 fragment 不可见所以 currentVisibleState = false 直接 return 掉
        // 這裡限制则可以限制多层嵌套的時候子 Fragment 的分发
        if (visible && isParentInvisible()) return;

        //此处是對子 Fragment 不可见的限制，因為 子 Fragment 先于父 Fragment回調本方法 currentVisibleState 置位 false
        // 当父 dispatchChildVisibleState 的時候第二次回調本方法 visible = false 所以此处 visible 將直接返回
        if (currentVisibleState == visible) {
            return;
        }

        currentVisibleState = visible;

        if (visible) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false;
                onFragmentFirstVisible();
            }
            onFragmentResume();
            dispatchChildVisibleState(true);
        } else {
            dispatchChildVisibleState(false);
            onFragmentPause();
        }
    }

    /**
     * 用于分发可见時間的時候父獲取 fragment 是否隐藏
     *
     * @return true fragment 不可见， false 父 fragment 可见
     */
    private boolean isParentInvisible() {
        LazyFragment fragment = (LazyFragment) getParentFragment();
        return fragment != null && !fragment.isSupportVisible();

    }

    private boolean isSupportVisible() {
        return currentVisibleState;
    }

    /**
     * 当前 Fragment 是 child 時候 作為快取 Fragment 的子 fragment 的唯一或者嵌套 VP 的第一 fragment 時 getUserVisibleHint = true
     * 但是由于父 Fragment 还进入可见狀態所以自身也是不可见的， 这個方法可以存在是因為庆幸的是 父 fragment 的生命周期回調总是先于子 Fragment
     * 所以在父 fragment 設定完成当前不可见狀態後，需要通知子 Fragment 我不可见，你也不可见，
     * <p>
     * 因為 dispatchUserVisibleHint 中判断了 isParentInvisible 所以当 子 fragment 走到了 onActivityCreated 的時候直接 return 掉了
     * <p>
     * 当真正的外部 Fragment 可见的時候，走 setVisibleHint (VP 中)或者 onActivityCreated (hide show) 的時候
     * 從對應的生命周期入口調用 dispatchChildVisibleState 通知子 Fragment 可见狀態
     *
     * @param visible
     */
    private void dispatchChildVisibleState(boolean visible) {
        FragmentManager childFragmentManager = getChildFragmentManager();
        List<Fragment> fragments = childFragmentManager.getFragments();
        if (!fragments.isEmpty()) {
            for (Fragment child : fragments) {
                if (child instanceof LazyFragment && !child.isHidden() && child.getUserVisibleHint()) {
                    ((LazyFragment) child).dispatchUserVisibleHint(visible);
                }
            }
        }
    }

    public void onFragmentFirstVisible() {
        Log.e("LazyFragment",getClass().getSimpleName() + "  對用户第一次可见");
        initData();

    }

    public void onFragmentResume() {
        Log.e("LazyFragment",getClass().getSimpleName() + "  對用户可见");
    }

    public void onFragmentPause() {
        Log.e("LazyFragment",getClass().getSimpleName() + "  對用户不可见");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
        mIsFirstVisible = true;
    }


    /**
     * 返回布局 resId
     *
     * @return layoutId
     */
//    protected abstract int getLayoutRes();


 /*   *//**
     * 初始化view
     *
//     * @param rootView
     *//*
    protected abstract void initView(View rootView);*/
    ///////////////////////////////////////////////////////
    @Override
    protected boolean isLazy() {
        return true;
    }
    protected  boolean ignoreUIVisibility(){
        return false;
    }

    @Override
    protected boolean isToolbarEnable() {
        return false;
    }
//    protected  boolean isLazyToolBarEnable(){
//        return false;
//    }
//    protected  void setLazyTitle(String title){
//        if(isLazyToolBarEnable()){
//
//        }
//    }

    @Override
    public  boolean hasBaseLayout() {
        return true;
    }




    //    @Override
//    public void onSaveInstanceState(Bundle outState) {
////        super.onSaveInstanceState(outState);
//    }


//    public void setFirstSelect(boolean firstSelect) {
//        isFirstSelect = firstSelect;
//    }

//    public void setIndex(int index) {
//        this.index = index;
//    }


    @Override
    protected void initWidget(View mRootView) {

    }
}
