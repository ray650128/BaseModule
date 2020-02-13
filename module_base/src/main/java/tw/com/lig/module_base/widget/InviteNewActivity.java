package tw.com.lig.module_base.widget;


import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

//import com.whyhow.global.bean.UpdateMineStatistics;


public class InviteNewActivity extends AbsWebActivity {
    @Override
    protected boolean isTitleCentered() {
        return true;
    }

    public static void start(Context context, String title, String url) {
       skipTo(context, InviteNewActivity.class,title,url);
    }

    public static void start(Context context, String title, String url,boolean convertUrl,boolean isToolbarEnabled) {
        skipTo(context, InviteNewActivity.class,title,url,convertUrl,isToolbarEnabled,true);
    }

    @Override
    protected void addJavascriptInterface(WebView webView) {
//        super.addJavascriptInterface(webView);
        mWebView.addJavascriptInterface(new WebviewJSData(),"callByJs");
    }

    @Override
    protected boolean hasStatusBar() {
        return false;
    }

  /*  @Override
    protected boolean isToolbarEnable() {
        return false;
    }*/

    private class WebviewJSData {

        @JavascriptInterface
        public void clickOnAndroidClose() {
            finish();
        }

        /*// 定義JS需要調用的方法
        // 被JS調用的方法必須加入@JavascriptInterface註解
        @JavascriptInterface
        public void takePic(String uploadUrl) {
            Message message = Message.obtain();
            message.obj = uploadUrl;
            message.what = MyHandler.MSG_TAKE_PIC;
            myHandler.sendMessage(message);
        }*/


    }

//    @Override
//    protected void onDestroy() {
//        EventBus.getDefault().post(new UpdateMineStatistics());//更新我的頁面的收益統計信息
//
//        super.onDestroy();
//    }
}
