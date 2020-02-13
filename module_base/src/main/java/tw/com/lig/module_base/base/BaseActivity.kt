package tw.com.lig.module_base.base

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import org.greenrobot.eventbus.EventBus
import tw.com.lig.module_base.R
import tw.com.lig.module_base.base.BaseLayout.OnBaseLayoutClickListener
import tw.com.lig.module_base.global.AppContext
import tw.com.lig.module_base.utils.KJActivityStack
import tw.com.lig.module_base.utils.TDevice
import tw.com.lig.module_base.widget.EmptyAdapter
import tw.com.lig.module_base.widget.LoadingView
import tw.com.lig.module_base.widget.ProgressLoading
import tw.com.lig.module_base.widget.ToolbarLayout

/**
 *
 */
abstract class BaseActivity<P : BasePresenter<*, *>?> : RxLifeCycleActivity(),
    OnBaseLayoutClickListener, BaseView, OnRefreshLoadMoreListener {
    //    private Unbinder mUnbinder;
    @JvmField
    protected var mToolbarLayout: ToolbarLayout? = null
    private var loadingView: LoadingView? = null
    private var mBaseLayout: BaseLayout? = null
    protected var savedInstanceState: Bundle? = null
    private var mProgressLoading: ProgressLoading? = null
    var isFirst = true
    private var smartRefreshLayout: SmartRefreshLayout? = null
    private var isStatusBarWhite: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        this.savedInstanceState = savedInstanceState
        TDevice.translucentStatusBar(this, isStatusBarWhite)
        TDevice.translusentNavigationBar(this, isNavigationTrans)
        KJActivityStack.create().addActivity(this)
        super.onCreate(savedInstanceState)

        var inflate = LayoutInflater.from(this).inflate(contentView, null)
        if (isNavigationTrans) {
            inflate.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
        if (hasStatusBar() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            inflate = addStatusBar(inflate)
        }
        setContentView(inflate)
        setupPresenter()
        if (isToolbarEnable && useCustomTitleBackground()) {
            findViewById<View>(R.id.title).setBackgroundResource(
                titleBackgroudResource
            )
        }
        if (isEventBusRegistered) {
            EventBus.getDefault().register(this)
        }
        initWidget(savedInstanceState)
        initData(savedInstanceState)
        if (!isLanscape) { //設定竖屏
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    //是否要用EventBus
    val isEventBusRegistered: Boolean
        get() = false

    protected open fun hasStatusBar(): Boolean {
        return false
    }

    private fun translusentNavigationBar() {
        if (isNavigationTrans) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = window
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.navigationBarColor = Color.TRANSPARENT
            }
        }
    }

    private val isNavigationTrans: Boolean
        get() = false

    private val titleBackgroudResource: Int
        get() = R.drawable.title_gradient

    private fun useCustomTitleBackground(): Boolean {
        return false
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        //        if (isSwipeBackEnable()){
//
//            SwipeBackHelper.onPostCreate(this);
//        }
    }

    /**
     * 是否横屏
     *
     * @return
     */
    private val isLanscape: Boolean
        get() = false

    /*protected boolean isSwipeBackEnable() {
        return true;
    }*/
    override fun setContentView(view: View) { //1.將view轉換為baseLayout
        var view: View? = view
        view = buildContentView(view)
        view = buildSmartRefreshLayout(view)
        //2.將view轉換成帶標題列的view
        view = buildToolbarLayout(view)
        super.setContentView(view)
    }

    private fun buildSmartRefreshLayout(view: View?): View? {
        if (isSmartRefreshLayoutEnabled) {
            smartRefreshLayout = SmartRefreshLayout(this)
            smartRefreshLayout!!.setEnableLoadMore(isLoadMoreEnabled)
            smartRefreshLayout!!.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            smartRefreshLayout!!.addView(
                view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            smartRefreshLayout!!.setOnRefreshListener(this)
            smartRefreshLayout!!.setOnLoadMoreListener(this)
            return smartRefreshLayout
        }
        return view
    }

    private fun buildContentView(view: View?): View? {
        if (hasBaseLayout()) {
            val builder = layoutBuilder
            mBaseLayout = builder.setContentView(view)
                .build()
            //                mBaseLayout.getProgressView().setLoadingColor(getResources().getColor(getLoadingColor()));
            return mBaseLayout
        }
        return view
    }

    private fun addStatusBar(contentView: View): View {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.topMargin = statusBarHeight.toInt()
        contentView.layoutParams = layoutParams
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(contentView, layoutParams)
        return linearLayout
        //        LayoutParams toolbarParams = new LayoutParams(
//                LayoutParams.MATCH_PARENT,
//                LayoutParams.WRAP_CONTENT);
//        toolbarParams.addRule(ALIGN_PARENT_TOP, TRUE);
//        addView(mTitlebar, toolbarParams);
    }

    fun setRightText(text: String?) {
        if (isToolbarEnable) {
            mToolbarLayout!!.setRightText(text)
            mToolbarLayout!!.setRightTextColor(rightTextColor)
        }
    }

    fun setRightTextInvisible(show: Boolean?) {
        if (isToolbarEnable) {
            mToolbarLayout!!.setRightTextInvisible(show)
        }
    }

    /**
     *
     *  獲取布局Builder，主要用于自定义每個頁面的progress、empty、error等View. <br></br>
     * 需要自定义的頁面需自行覆盖實現.
     *
     * @return
     */
    private val layoutBuilder: BaseLayout.Builder
        get() = BaseLayout.Builder(this)
            .setProgressBarViewBg(progressBg)
            .setOnBaseLayoutClickListener(this)

    private fun buildToolbarLayout(view: View?): View? {
        if (isToolbarEnable) {
            mToolbarLayout = ToolbarLayout.Builder(this)
                .setContentView(view)
                .setTitleWhite(isTitleWhite)
                .setLeftBack(hasLeftBack())
                .setIsTitleRightIconVisible(isTitleRightIconVisible)
                .setStatusBarHeight(statusBarHeight)
                .setToolbarTranparent(isToolbarTransparent)
                .setTitleCentered(isTitleCentered)
                .build()
            mToolbarLayout?.setOnClickListener(object : ToolbarLayout.OnClickListener {
                override fun onLeftClick() {
                    onToolbarLeftClick()
                }

                override fun onRightClick() {
                    onToolbarRightClick()
                }

                override fun onRightClick2() {
                    onToolbarRightClick2()
                }
            })
            if (isTitleDividerVisible) {
                mToolbarLayout?.setTitleDividerVisible()
            }
            return mToolbarLayout
        }
        return view
    }

    fun switchTheme(isStatusBarWhite: Boolean) {
        this.isStatusBarWhite = isStatusBarWhite
        TDevice.switchTheme(this, isStatusBarWhite)
    }

    /**
     * Get status bar height.
     *
     * @return
     */
    val statusBarHeight: Float
        get() {
            val height = resources.getIdentifier("status_bar_height", "dimen", "android")
            return resources.getDimension(height)
        }

    fun setRightIcon(drawableId: Int) {
        if (isToolbarEnable) {
            mToolbarLayout!!.setRightIcon(drawableId)
        }
    }

    fun setRightIcon2(drawableId: Int) {
        if (isToolbarEnable) {
            mToolbarLayout!!.setRightIcon2(drawableId)
        }
    }

    fun setRightIcon2Invisible() {
        if (isToolbarEnable) {
            mToolbarLayout!!.setRightIcon2Invisible()
        }
    }

    fun setLeftIcon(drawableId: Int) {
        if (isToolbarEnable) {
            mToolbarLayout!!.setLeftTextIcon("", drawableId)
        }
    }

    fun setLeftBackText(text: String?) {
        if (isToolbarEnable) {
            mToolbarLayout!!.setLeftText(text)
        }
    }

    override fun hasBaseLayout(): Boolean {
        return false
    }

    protected fun onToolbarRightClick() {}
    protected fun onToolbarRightClick2() {}
    protected fun onToolbarLeftClick() {
        finish()
    }

    override fun onClickRetry() {
        showProgressView() //显示載入进度条
        initData(savedInstanceState)
    }

    override fun onClickEmpty() {}

    /**
     * 是否有返回按钮 ，默认有返回按钮
     *
     * @return
     */
    private fun hasLeftBack(): Boolean {
        return true
    }

    /**
     * 載入dialog 的背景颜色
     *
     * @return
     */
    val progressBg: Int
        get() = R.color.color_eee

    /**
     * Is Toolbar enable.
     * 是否显示標題列
     *
     * @return
     */
    protected open val isToolbarEnable: Boolean
        get() = true

    private val isToolbarTransparent: Boolean
        get() = false

    /**
     * 載入布局
     *
     * @return
     */
    protected abstract val contentView: Int

    /**
     * 初始化view
     *
     * @param savedInstanceState
     */
    protected abstract fun initWidget(savedInstanceState: Bundle?)

    /**
     * 初始化資料
     *
     * @param savedInstanceState
     */
    protected abstract fun initData(savedInstanceState: Bundle?)

    /**
     * 初始化presenter
     */
    protected abstract fun setupPresenter()

    /**
     * 显示無資料頁面
     */
    override fun showEmptyView() {
        if (isSmartRefreshLayoutEnabled) {
            val childAt = smartRefreshLayout!!.getChildAt(0)
            if (childAt is RecyclerView) {
                childAt.layoutManager = LinearLayoutManager(this)
                childAt.adapter = EmptyAdapter(this, emptyTipMsg)
                if (mBaseLayout != null) {
                    showContentView()
                }
            } else if (childAt is BaseLayout) {
                val childAt1 = childAt.getChildAt(0)
                if (childAt1 is RecyclerView) {
                    childAt1.layoutManager = LinearLayoutManager(this)
                    childAt1.adapter = EmptyAdapter(this, emptyTipMsg)
                    if (mBaseLayout != null) {
                        showContentView()
                    }
                }
            }
        } else {
            if (null != mBaseLayout) {
                mBaseLayout!!.showEmptyView(emptyTipMsg)
            }
        }
    }

    private val emptyTipMsg: String
        get() = "亲~~暫無记錄"
    //    protected int getEmptyDrawable(){
//        return R.drawable.no_banner
//    }
    /**
     * 显示載入失败的頁面
     */
    override fun showErrorView() {
        if (null != mBaseLayout) {
            mBaseLayout!!.showErrorView()
            mBaseLayout!!.getmErrorMsgView().text = resources.getString(R.string.retry)
            mBaseLayout!!.retryButton.background = resources.getDrawable(retryButtonBackground)
        }
    }

    override fun showErrorView(msg: String) {
        if (null != mBaseLayout) {
            mBaseLayout!!.showErrorView()
            mBaseLayout!!.getmErrorMsgView().text = msg
            //            mBaseLayout.
            mBaseLayout!!.retryButton.background = resources.getDrawable(retryButtonBackground)
        }
    }

    /**
     * 显示載入进度条，若有網路請求，頁面初始化就載入
     */
    fun showProgressView() {
        if (null != mBaseLayout) {
            mBaseLayout!!.showProgressView()
        }
    }

    //显示自定义頁面
    override fun showUserDefinedView(view: View) {
        mBaseLayout!!.showUserDefinedView(view)
    }

    /**
     * 显示網路請求成功的頁面
     */
    override fun showContentView() {
        if (null != mBaseLayout) {
            mBaseLayout!!.showContentView()
        }
    }

    override fun startProgressDialog(msg: String) {
        if (loadingView == null) {
            loadingView = LoadingView.Builder(this)
                .setLoadMsg(msg)
                .setKeyBackCancelable(true)
                .setOutSideEnable(true)
                .build()
        }
        if (!isFinishing) {
            loadingView!!.showLoading()
        }
    }

    override fun startProgressDialog() { //这是之前的dialog，用新的仿ios的替代了
/*   if (loadingView == null) {
            loadingView = new LoadingView
                    .Builder(this)
                    .setKeyBackCancelable(true)
                    .setOutSideEnable(true)
                    .build();
        }


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingView.getRotateLoading().setLoadingColor(getResources().getColor(getLoadingColor()));
                loadingView.getRotateLoading().start();

                if (!isFinishing()) {

                    loadingView.showLoading();
                }
            }
        });*/
        showDialogLoading()
    }

    override fun stopProgressDialog() { //这是之前的dialog,現在用新的替代了2019年3月13日
/* if (loadingView != null) {
            loadingView.getRotateLoading().stop();
            loadingView.dismissLoading();
            loadingView = null;
        }*/
        hideDialogLoading()
    }

    override fun onDestroy() {
        if (isEventBusRegistered) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
        //        if (null != mUnbinder && mUnbinder != Unbinder.EMPTY) {
//            mUnbinder.unbind();
//        }
//        mUnbinder = null;
        if (loadingView != null) {
            loadingView!!.dismissLoading()
        }
        loadingView = null
        //        if (mPresenter != null) {
//            mPresenter.onDestroy();//释放资源
//        }
//        this.mPresenter = null;
//        if (isSwipeBackEnable()){
//
//            SwipeBackHelper.onDestroy(this);
//        }
        KJActivityStack.create().finishActivity(this)
    }

    override fun showToast(message: String) {
        runOnUiThread {
            //                showToast(message);
            Toast.makeText(
                AppContext.getContext(),
                message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun showInstantToast(message: String?) {
        InstantToast.show(AppContext.getContext(), message)
    }

    override fun showToastCenter(message: String) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    public override fun onResume() {
        super.onResume()
        //        MobclickAgent.onPageStart(getClass().getName());
//        MobclickAgent.onResume(this);
    }

    public override fun onPause() {
        super.onPause()
        //        MobclickAgent.onPageEnd(getClass().getName());
//        MobclickAgent.onPause(this);
    }

    fun setTitle(title: String?) {
        if (mToolbarLayout != null) {
            mToolbarLayout!!.setTitle(title)
        }
    }

    private val isTitleDividerVisible: Boolean
        get() = false

    /**
     * 標題是否居中
     *
     * @return
     */
    protected open val isTitleCentered: Boolean
        get() = true

    private val retryButtonBackground: Int
        get() =
            R.drawable.btn_oval_selector

    override fun showAccountExceptionDialog() {}
    override fun showLoginExceptionDialog(mBaseView: BaseView) {}
    private fun showDialogLoading() {
        if (mProgressLoading == null) {
            mProgressLoading = ProgressLoading(this)
        }
        mProgressLoading!!.showLoading()
    }

    private fun hideDialogLoading() {
        if (mProgressLoading != null && mProgressLoading!!.isShowing) {
            mProgressLoading!!.hideLoading()
        }
    }

    override fun isSmartRefreshLayoutEnabled(): Boolean {
        return false
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {}
    override fun onRefresh(refreshLayout: RefreshLayout) {
        smartRefreshLayout!!.setEnableLoadMore(true)
    }

    protected fun finishRefreshing() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout!!.finishRefresh()
        }
    }

    protected fun finishLoadingMore() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout!!.finishLoadMore()
        }
    }

    protected fun finishLoadMoreWithNoMoreData() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout!!.finishLoadMoreWithNoMoreData()
        }
    }

    private var isLoadMoreEnabled: Boolean
        get() = isSmartRefreshLayoutEnabled
        set(isLoadMoreEnabled) {
            if (smartRefreshLayout != null) {
                smartRefreshLayout!!.setEnableLoadMore(isLoadMoreEnabled)
            }
        }

    /**
     * 標題右侧的圖標是否可见
     *
     * @return
     */
    val isTitleRightIconVisible: Boolean
        get() = false

    private val isTitleWhite: Boolean
        get() = false

    private var rightTextColor: Int
        get() = resources.getColor(R.color.colorPrimary)
        set(color) {
            if (isToolbarEnable) {
                mToolbarLayout!!.setRightTextColor(color)
            }
        }
}