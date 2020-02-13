package tw.com.lig.module_base.global;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import tw.com.lig.module_base.utils.TDevice;


/**
 * 作者：${weihaizhou} on 2018/1/26 14:55
 */
public class AppContext extends Application {

    private static AppContext mContext;
    private ApplicationDelegate applicationDelegate;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        applicationDelegate = new ApplicationDelegate();
        applicationDelegate.attachBaseContext(base);
        
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        applicationDelegate.onCreate(this);
        TDevice.syncIsDebug(getApplicationContext());

      /*

        UMConfigure.init(this, "5ab7925ef43e4842160003e6"
                , "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");//58edcfeb310c93091c000be2 5965ee00734be40b580001a0
        UMShareAPI.get(mContext);//初始化sdk
        UMConfigure.setLogEnabled(true);
        PlatformConfig.setWeixin("wxb076a41b95fa0d27", "e78a46683613b5ff1f6cee48f00437ad");

        PlatformConfig.setSinaWeibo("3090365761", "c1c37dd200476445875ce46757b85252",
                "http://jhapit.yqbing.com");
        PlatformConfig.setQQZone("1106571966", "gd874o7wT2tKLWX0");*/

//        FontUtils.getInstance().replaceSystemDefaultFontFromAsset(this, "PingFang-SC-Regular.otf");

        //設定字体
               /* ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/pingfangsc.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());*/

//        //必须調用初始化
//        OkHttpUtils.init(this);
//        //以下都不是必须的，根据需要自行选择
//        OkHttpUtils.getInstance()//
//                .debug("OkHttpUtils")                                              //是否打开調试
//                .setConnectTimeout(*//*OkHttpUtils.DEFAULT_MILLISECONDS*//* 8000)               //全局的連接超時時間
//                .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                  //全局的讀取超時時間
//                .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                 //全局的寫入超時時間
//                .setCookieStore(new MemoryCookieStore()) ;*/
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        applicationDelegate.onTerminate(this);
    }


    public static AppContext getContext() {
        return mContext;
    }

}
