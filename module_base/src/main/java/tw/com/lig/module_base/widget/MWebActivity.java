package tw.com.lig.module_base.widget;


import android.content.Context;

import tw.com.lig.module_base.base.BaseView;

public class MWebActivity extends AbsWebActivity {
    @Override
    protected boolean isTitleCentered() {
        return true;
    }

    public static void start(Context context, String title, String url) {
       skipTo(context,MWebActivity.class,title,url);
    }
    public static void startWithoutExtraParams(Context context, String title, String url) {
        skipTo(context,MWebActivity.class,title,url,false,true,true);
    }
    public static void start(Context context, String title, String url,boolean convertUrl,boolean isToolbarEnabled) {
        skipTo(context,MWebActivity.class,title,url,convertUrl,isToolbarEnabled,true);
    }
    public static void start(Context context, String title, String url,boolean convertUrl,boolean isToolbarEnabled,boolean isTitleChangable) {
        skipTo(context,MWebActivity.class,title,url,convertUrl,isToolbarEnabled,isTitleChangable);
    }




    @Override
    public void showAccountExceptionDialog() {

    }

    @Override
    public void showLoginExceptionDialog(BaseView mBaseView) {

    }
    //    public static void start(String title,String url) {
//        skipTo(AppContext.getContext(),MWebActivity.class,title,url);
//    }

}
