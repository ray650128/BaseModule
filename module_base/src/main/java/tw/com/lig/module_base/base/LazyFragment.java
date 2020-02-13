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
 * 懶載入fragment
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
        // 對于預設 tab 和 間隔 checked tab 需要等到 isViewCreated = true 後才可以通過此通知用戶可見
        // 這種情況下第一次可見不是在這裡通知 因為 isViewCreated = false 成立,等從別的界面回到這裡後會使用 onFragmentResume 通知可見
        // 對于非預設 tab mIsFirstVisible = true 會一直保持到選擇則這個 tab 的時候，因為在 onActivityCreated 會返回 false
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
        // !isHidden() 預設為 true  在調用 hide show 的時候可以使用
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
        // 當前 Fragment 包含子 Fragment 的時候 dispatchUserVisibleHint 內部本身就會通知子 Fragment 不可見
        // 子 fragment 走到這裡的時候自身又會調用一遍 ？
        if (currentVisibleState && getUserVisibleHint()) {
            dispatchUserVisibleHint(false);
        }
    }


    /**
     * 統一處理 顯示隱藏
     *
     * @param visible
     */
    private void dispatchUserVisibleHint(boolean visible) {
        //當前 Fragment 是 child 時候 作為快取 Fragment 的子 fragment getUserVisibleHint = true
        //但當父 fragment 不可見所以 currentVisibleState = false 直接 return 掉
        // 這裡限制則可以限制多層嵌套的時候子 Fragment 的分發
        if (visible && isParentInvisible()) return;

        //此處是對子 Fragment 不可見的限制，因為 子 Fragment 先於父 Fragment回調本方法 currentVisibleState 置位 false
        // 當父 dispatchChildVisibleState 的時候第二次回調本方法 visible = false 所以此處 visible 將直接返回
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
     * 用於分發可見時間的時候父獲取 fragment 是否隱藏
     *
     * @return true fragment 不可見， false 父 fragment 可見
     */
    private boolean isParentInvisible() {
        LazyFragment fragment = (LazyFragment) getParentFragment();
        return fragment != null && !fragment.isSupportVisible();

    }

    private boolean isSupportVisible() {
        return currentVisibleState;
    }

    /**
     * 當前 Fragment 是 child 時候 作為快取 Fragment 的子 fragment 的唯一或者嵌套 VP 的第一 fragment 時 getUserVisibleHint = true
     * 但是由於父 Fragment 還進入可見狀態所以自身也是不可見的， 這個方法可以存在是因為慶幸的是 父 fragment 的生命周期回調總是先於子 Fragment
     * 所以在父 fragment 設定完成當前不可見狀態後，需要通知子 Fragment 我不可見，你也不可見，
     * <p>
     * 因為 dispatchUserVisibleHint 中判斷了 isParentInvisible 所以當 子 fragment 走到了 onActivityCreated 的時候直接 return 掉了
     * <p>
     * 當真正的外部 Fragment 可見的時候，走 setVisibleHint (VP 中)或者 onActivityCreated (hide show) 的時候
     * 從對應的生命周期入口調用 dispatchChildVisibleState 通知子 Fragment 可見狀態
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
        Log.e("LazyFragment",getClass().getSimpleName() + "  對用戶第一次可見");
        initData();

    }

    public void onFragmentResume() {
        Log.e("LazyFragment",getClass().getSimpleName() + "  對用戶可見");
    }

    public void onFragmentPause() {
        Log.e("LazyFragment",getClass().getSimpleName() + "  對用戶不可見");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
        mIsFirstVisible = true;
    }


    /**
     * 返回佈局 resId
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
