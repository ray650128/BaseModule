package tw.com.lig.module_base.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.ZoomButtonsController;

import tw.com.lig.module_base.R;
import tw.com.lig.module_base.base.BaseActivity;
import tw.com.lig.module_base.entity.WebParamBuilder;
import tw.com.lig.module_base.utils.TDevice;

//import tw.com.lig.module_base.bean.ClearInfo;
//import com.whyhow.global.ShareData;


/**
 * 作者：${weihaizhou} on 2017/11/8 15:40
 */
public abstract class AbsWebActivity extends BaseActivity {
    private boolean isToolbarEnabled=true;

    private static final String TAG="AbsWebActivity";

    public static final String KEY_WEB_PARAM = "web_param";

    private RelativeLayout mLayWebview;

    protected WebView mWebView;
    protected String webUrl;
    private boolean isLoadError;
    private boolean convertUrl=true;
//    private AddJavascriptInterfaceListener addJavascriptInterfaceListener;
    private boolean isTitleChangable=true; //標題是否可以改变

    @Override
    protected int getContentView() {
        return R.layout.activity_webview;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isToolbarEnabled=getIntent().getBooleanExtra("isToolbarEnabled", true);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        mLayWebview = findViewById(R.id.fl_content);
        mWebView = new WebView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(params);
        mLayWebview.addView(mWebView);
        initWebView(mWebView);
        webUrl = getParams().getUrl();
        convertUrl = getIntent().getBooleanExtra("convertUrl", true);
        isTitleChangable = getIntent().getBooleanExtra("isTitleChangeable", true);
        if(convertUrl){
            webUrl = convertUrl(webUrl);
        }

        mWebView.loadUrl(webUrl);
        Log.i(TAG,"webUrl" + webUrl);
        mWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mWebView.setVerticalScrollBarEnabled(false);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // KITKAT
        {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }*/

        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        addJavascriptInterface(mWebView);




    }

    public interface AddJavascriptInterfaceListener{
        void addJavascriptInterface(WebView webView);
    }
    protected void addJavascriptInterface(WebView webView){

    }

    /**
     * 載入失败回調
     *
     * @param view
     * @param request
     * @param error
     */
    protected void onUrlError(WebView view, WebResourceRequest request, WebResourceError error) {
        isLoadError = true;
    }


    /**
     * 当前WebView显示頁面的標題
     *
     * @param view  WebView
     * @param title web頁面標題
     */
    protected void onWebTitle(WebView view, String title) {
        if (!TextUtils.isEmpty(title)) {
            if (title.toLowerCase().contains("error")
                    || title.toLowerCase().contains("找不到网頁")) {
                isLoadError = true;
                if (isToolbarEnabled){

                    mToolbarLayout.setTitle("載入出错啦");
                }
            } else {
                if (isToolbarEnabled&&isTitleChangable){

                    mToolbarLayout.setTitle(TextUtils.isEmpty(getParams().getTitle()) ? title : getParams().getTitle());
                }
            }
        }
    }

    /**
     * 链接载入成功後會被調用
     *
     * @param view WebView
     * @param url  链接地址
     */
    protected void onUrlFinished(WebView view, String url) {
        if (isLoadError) {
            showErrorView();
        } else {
            showContentView();
        }
    }

    /**
     * 载入链接之前會被調用
     *
     * @param view WebView
     * @param url  链接地址
     */
    protected void onUrlLoading(WebView view, String url) {
    /*    if (!TextUtils.isEmpty(url) && url.toLowerCase().contains("/user/login")) {//token 过期 跳轉登錄
//            startActivity(new Intent(this, LoginActivity.class));
        }*/
    }

    private String convertUrl(String webUrl) {
        String url = webUrl + (webUrl.contains("?") ? "&" : "?") + "token=" /*+ SPutils.get(AppContext.getContext(), ShareData.token, "")*/ + "&version=" + TDevice.getVersionName() + "&device=" + TDevice.getIMEI() + "&appId=41&appChannel=41&platformId=1";
        return url;
    }


    private void initWebView(WebView mWebView) {
        WebSettings settings = mWebView.getSettings();
        settings.setDefaultFontSize(15);
        settings.setJavaScriptEnabled(true);//
        settings.setSupportZoom(false);// 是否支持缩放，配合方法setBuiltInZoomControls使用，默认true
        settings.setBuiltInZoomControls(false);//是否使用WebView内置的缩放组件,默认false
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适應螢幕
        if (TDevice.hasInternet()) {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否從網路上取資料。
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则從本地獲取，即離线載入
        }
        settings.setAllowFileAccess(true);// 可以访問文件
        settings.setLoadWithOverviewMode(true);//当頁面宽度超过WebView显示宽度時，缩小頁面适應WebView。默认false
        settings.setDomStorageEnabled(true); // 開啟 DOM storage API 功能
        settings.setDatabaseEnabled(true);   //開啟 database storage API 功能
        settings.setAppCacheEnabled(true);//開啟 Application Caches 功能
        String cacheDirPath = getFilesDir().getAbsolutePath() + "webview_cache";
        settings.setAppCachePath(cacheDirPath); //設定  Application Caches 快取目錄
        int sysVersion = Build.VERSION.SDK_INT;
        if (sysVersion >= 11) {
            settings.setDisplayZoomControls(false);
        } else {
            ZoomButtonsController zbc = new ZoomButtonsController(mWebView);
            zbc.getZoomControls().setVisibility(View.GONE);
        }

        //5.0+設定混合模式。允许https 访問http
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    @Override
    public void onClickRetry() {
        super.onClickRetry();
        isLoadError = false;
        mWebView.reload();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mWebView != null && mWebView.canGoBack()) {  //表示按返回键
                mWebView.goBack();   //後退
                return true;
            } else {
                finish();
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
//        EventBus.getDefault().post(new ClearInfo());
        if (mWebView != null) {
            mWebView.stopLoading();
            mWebView.removeAllViewsInLayout();
            mWebView.removeAllViews();
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    /**
     * 封装了url/ 標題
     *
     * @return
     */
    public WebParamBuilder getParams() {
        return getIntent().getParcelableExtra(KEY_WEB_PARAM);
    }

    @Override
    protected boolean isToolbarEnable() {
        return isToolbarEnabled;
    }

    @Override
    public boolean hasBaseLayout() {
        return true;
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();//接受证书
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showProgressView();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            onUrlFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url == null) return false;

            try {
                if(url.startsWith("weixin://") || url.startsWith("alipays://") ||
                        url.startsWith("mailto://") || url.startsWith("tel://")
                    //其他自定义的scheme
                ) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            } catch (Exception e) { //防止crash (如果手機上没有安装处理某個scheme开头的url的APP, 會导致crash)
                return false;
            }



            onUrlLoading(view, url);

            if (url.startsWith("tel:")) {//拨打電話
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(intent);
                view.reload();
                return true;
            }else {
                view.loadUrl(url);
            }

            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
//            onUrlError(view, request, error);//這裡先注释掉这行代碼，发現有時候會影响載入

        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            onWebTitle(view, title);
        }

    }


    public static void skipTo(Activity a, WebParamBuilder builder, Class clz) {

        Intent intent = new Intent(a, clz);
        intent.putExtra(KEY_WEB_PARAM, builder);
        a.startActivity(intent);
    }
    public static void skipTo(Context a, Class clz, String title, String url) {
        WebParamBuilder builder = WebParamBuilder.create().setTitle(title).setUrl(url);
        Intent intent = new Intent(a, clz);
        intent.putExtra(KEY_WEB_PARAM, builder);
        a.startActivity(intent);
    }
    public static void skipTo(Context a, Class clz, String title, String url,boolean convertUrl,boolean isToolbarEnabled,boolean isTitleChangable) {
        WebParamBuilder builder = WebParamBuilder.create().setTitle(title).setUrl(url);
        Intent intent = new Intent(a, clz);
        intent.putExtra("convertUrl",convertUrl);
        intent.putExtra("isToolbarEnabled",isToolbarEnabled);
        intent.putExtra("isTitleChangable",isTitleChangable);
        intent.putExtra(KEY_WEB_PARAM, builder);
        a.startActivity(intent);
    }

//    WebParamBuilder builder = WebParamBuilder.create().setTitle("借款协议").setUrl(URLConstant.URL_REGISTER);

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void setupPresenter() {

    }
    protected boolean isNeedConvertUrl(){
        return true;
    }

}
