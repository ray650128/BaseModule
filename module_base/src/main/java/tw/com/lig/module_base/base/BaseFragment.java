package tw.com.lig.module_base.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import tw.com.lig.module_base.R;
import tw.com.lig.module_base.global.AppContext;
import tw.com.lig.module_base.utils.TDevice;
import tw.com.lig.module_base.widget.LoadingView;

import org.greenrobot.eventbus.EventBus;

//import com.umeng.analytics.MobclickAgent;
//import javax.inject.Inject;  //这個是注入presenter用到的

//import butterknife.ButterKnife;
//import butterknife.Unbinder;

public abstract class BaseFragment<P extends BasePresenter> extends RxLifeCycleFragment implements BaseLayout.OnBaseLayoutClickListener, OnRefreshLoadMoreListener ,BaseView{

    private boolean isViewCreated;
//    private Unbinder mUnbinder;
    protected View mRootView;
    protected Bundle mBundle;
    protected BaseActivity mActivity;
    private BaseLayout mBaseLayout;
    private LoadingView loadingView;
//    public ToolbarLayout mToolbarLayout;
    public boolean isFirst=true;

//    @Inject
    protected P mPresenter;
    protected SmartRefreshLayout smartRefreshLayout;

    @Override
    public void onAttach(Activity activity) {
        mActivity = (BaseActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }
    //是否要用EventBus
    public boolean isEventBusRegistered(){
        return false;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
        initBundle(mBundle);
    }

    protected abstract void setupPresenter();



    /**
     * 是否进行懒載入
     * @return
     */
    protected boolean isLazy(){
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      /*  LayoutInflaterCompat.setFactory2(inflater, new LayoutInflater.Factory2() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                return null;
            }

            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                return null;
            }
        });*/
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null)
                parent.removeView(mRootView);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&!isToolbarEnable()){
                if(needBuildTransparentStatusbar()){
                    LinearLayout ll=new LinearLayout(getContext());
                    ll.setOrientation(LinearLayout.VERTICAL);
                    View view_status=new View(AppContext.getContext());
                    view_status.setBackgroundColor((getResources().getColor(android.R.color.transparent)));
                    view_status.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)TDevice.getStatusBarHeight(AppContext.getContext())));
                    ll.addView(view_status);
                    ll.addView(inflater.inflate(getLayoutId(), container, false));
                    mRootView=ll;
                }else{
                    mRootView= inflater.inflate(getLayoutId(), container, false);
                }



            }else{
                mRootView = inflater.inflate(getLayoutId(), container, false);
            }
            if (isSmartRefreshLayoutEnabled()){
                smartRefreshLayout = new SmartRefreshLayout(getContext());
                smartRefreshLayout.setEnableLoadMore(isLoadMoreEnabled());
                smartRefreshLayout.setOnRefreshListener(this);
                smartRefreshLayout.setOnLoadMoreListener(this);
                smartRefreshLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                 smartRefreshLayout.addView(mRootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mRootView= smartRefreshLayout;
            }

            if (hasBaseLayout()) {
                BaseLayout.Builder builder = getLayoutBuilder();
                if (null != builder) {
                    mBaseLayout = builder.setContentView(mRootView).build();
//                    mBaseLayout.getProgressView().setLoadingColor(getResources().getColor(getLoadingColor()));
//                    mBaseLayout.getProgressView().setPorBarStartColor(getProgressStartColor());
//                    mBaseLayout.getProgressView().setPorBarEndColor(getProgressEndColor());
                    mRootView = mBaseLayout;
                }
            }
            mRootView = onBindViewBefore(mRootView);
//            mUnbinder = ButterKnife.bind(this, mRootView);
            if (savedInstanceState != null)
                onRestartInstance_(savedInstanceState);
            // init
            setupPresenter();
            initWidget(mRootView);
            if(!isLazy()){
                initData();
            }


        }
        return mRootView;
    }
    protected  boolean isLoadMoreEnabled(){
        if (isSmartRefreshLayoutEnabled()){
            return true;
        }
        return false;

    }



    protected int getRetryButtonBackground() {
        if(isOrangeTheme()){
            return R.drawable.orange_btn_oval_selector;
        }
        return R.drawable.btn_oval_selector;
    }

    protected boolean isOrangeTheme() {
        return false;
    }


    protected int getLoadingColor() {
        if(isOrangeTheme()){
            return R.color.purple;
        }
        return  R.color.colorAccent;
    }


    /**
     * <p>
     * 獲取布局Builder，主要用于自定义每個頁面的progress、empty、error等View.
     * 需要自定义的頁面需自行覆盖實現.
     * </p>
     *
     * @return
     */
    protected BaseLayout.Builder getLayoutBuilder() {
        return new BaseLayout.Builder(mActivity)
                .setProgressBarViewBg(getProgressBg())
                .setOnBaseLayoutClickListener(this);
    }

    /**
     * 是否包含基本view如progress、empty、error等.
     *
     * @return
     */
    @Override
    public boolean hasBaseLayout() {
        return false;
    }

    protected void initBundle(Bundle bundle) {

    }


    @Override
    public void onClickRetry() {
        showProgressView();//显示載入进度条
        initData();

    }

    @Override
    public void onClickEmpty() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        if (null != mUnbinder && mUnbinder != Unbinder.EMPTY) {
//            mUnbinder.unbind();
//        }
    }

    @Override
    public void onDestroy() {
        if (isEventBusRegistered()){
            EventBus.getDefault().unregister(this);
        }
//        mUnbinder = null;
        if (mPresenter != null) {
            mPresenter.onDestroy();//释放资源
        }

        this.mPresenter = null;

//        if (null != mUnbinder && mUnbinder != Unbinder.EMPTY) {
//            mUnbinder.unbind();
//        }
        super.onDestroy();

    }

    /**
     * 显示無資料頁面
     */
    public void showEmptyView() {
        if (null != mBaseLayout) {
            mBaseLayout.showEmptyView(getEmptyTipMsg());
        }
    }
    protected String getEmptyTipMsg() {
        return "亲~~暫無记錄";
    }

    /**
     * 显示載入失败的頁面
     */
    public void showErrorView() {
        if (null != mBaseLayout) {
            mBaseLayout.showErrorView();
            mBaseLayout.getmErrorMsgView().setText(getResources().getString(R.string.retry));

            mBaseLayout.getRetryButton().setBackground(getResources().getDrawable(getRetryButtonBackground()));

        }
    }
    //显示自定义頁面
    public void showUserDefinedView(View view){
        if (null != view) {
            mBaseLayout.showUserDefinedView(view);
        }
    }

    /**
     * 显示載入进度条，若有網路請求，頁面初始化就載入
     */
    public void showProgressView() {
        if (null != mBaseLayout) {
            mBaseLayout.showProgressView();
        }
    }

    /**
     * 显示網路請求成功的頁面
     */
    public void showContentView() {
        if (null != mBaseLayout) {
            mBaseLayout.showContentView();
        }
    }

    /**
     * 載入dialog 的背景颜色
     *
     * @return
     */
    public int getProgressBg() {
        return R.color.color_eee;
    }


    public void startProgressDialog() {
        if(!useOwnProgressDialog()){

            ((BaseActivity) getActivity()).startProgressDialog();
        }else{
            if (loadingView == null) {
                loadingView = new LoadingView
                        .Builder(getActivity())
                        .setKeyBackCancelable(true)
                        .setOutSideEnable(true)
                        .build();
            }
            loadingView.getRotateLoading().setLoadingColor(getResources().getColor(getLoadingColor()));
            loadingView.getRotateLoading().start();


            loadingView.showLoading();
        }
    }

    public void startProgressDialog(String msg) {
        ((BaseActivity) getActivity()).startProgressDialog(msg);
    }

    public void stopProgressDialog() {
        if(!useOwnProgressDialog()){

            ((BaseActivity) getActivity()).stopProgressDialog();
        }else{
            if (loadingView != null) {
                loadingView.getRotateLoading().stop();
                loadingView.dismissLoading();
                loadingView = null;
            }
        }
    }
//    public void stopProgressDialog() {
//
//    }
    /**是使用activity的progressDialog还是使用自己的progressDialog*/
    protected boolean useOwnProgressDialog(){
        return false;
    }
/*    public void startProgressDialog(String msg) {
        if (loadingView == null) {
            loadingView = new LoadingView
                    .Builder(getActivity())
                    .setLoadMsg(msg)
                    .setKeyBackCancelable(true)
                    .setOutSideEnable(true)
                    .build();
        }
        if (!isFinishing()) {
            loadingView.showLoading();
        }

    }*/
//    public void startProgressDialog() {
//
//
//    }

    public void showToast(String message) {
        ((BaseActivity) getActivity()).showToast(message);
    }


    public void showToastCenter(String message) {
        ( (BaseActivity) getActivity()).showToastCenter(message);
    }


    protected abstract void initWidget(View mRootView);

    protected  abstract void initData();
    public void showInstantToast(String message){
        ( (BaseActivity) getActivity()).showInstantToast(message);
    }

    protected void onRestartInstance_(Bundle bundle) {

    }

    protected View onBindViewBefore(View root) {
        // ...
        return root;
    }

    protected abstract int getLayoutId();


    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPause(mActivity);//應該不需要調用这個，在baseactivity裡面已经調用了
//        MobclickAgent.onPageEnd(this.getClass().getName());
    }


    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onResume(mActivity);
//        MobclickAgent.onPageStart(this.getClass().getName());
    }
    protected boolean isToolbarEnable() {
        return false;
    }
    public void setTitle(String title){
        if(isToolbarEnable()){

        }

    }
    public void showAccountExceptionDialog(){
        BaseActivity activity = (BaseActivity) getActivity();
        if(activity!=null){
            activity.showAccountExceptionDialog();
        }
    }
    public void showLoginExceptionDialog(BaseView mBaseView){
        BaseActivity activity = (BaseActivity) getActivity();
        if(activity!=null){
            activity.showLoginExceptionDialog(mBaseView);
        }
    }
    protected boolean needBuildTransparentStatusbar(){
        return true;
    }
//
//    protected  int getProgressStartColor(){
//        return  R.color.blue;
//    }
//    protected  int getProgressEndColor(){
//        return  R.color.light_blue;
//    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isViewCreated){
            isViewCreated=true;
            if (isEventBusRegistered()){
                EventBus.getDefault().register(this);
            }
            initKtWidgets();
        }
    }
    protected void initKtWidgets(){

    }
    public View findViewById(int id){
        return mRootView.findViewById(id);
    }

    @Override
    public boolean isSmartRefreshLayoutEnabled(){
        return false;
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }

    protected void finishRefreshing(){
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh();
        }

    }
    protected void finishLoadingMore(){
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishLoadMore();
        }

    }
    /**
     * 没有更多資料了
     */
    protected void finishLoadMoreWithNoMoreData(){
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishLoadMoreWithNoMoreData();
        }
    }
    public void showErrorView(String msg) {
        if (null != mBaseLayout) {
            mBaseLayout.showErrorView();
            mBaseLayout.getmErrorMsgView().setText(msg);
//            mBaseLayout.
            mBaseLayout.getRetryButton().setBackground(getResources().getDrawable(getRetryButtonBackground()));

        }
    }

}
