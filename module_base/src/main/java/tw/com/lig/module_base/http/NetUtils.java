package tw.com.lig.module_base.http;

import android.content.Context;

import tw.com.lig.module_base.data.GlobalConstant;
import tw.com.lig.module_base.global.AppContext;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by ljk on 2017/2/23.
 */

public class NetUtils {

    private static Retrofit retrofit;
    private static Interceptor mCodeInter;
    private Context mContext;
    private static Interceptor mTokenInter;
    private static Interceptor mCacheInter;

    private static Retrofit getRetrofit(Context context) {

        File httpCacheDirectory = new File(context.getCacheDir(), "cache");
        int cacheSize = 10 * 1024 * 1024;// 10 MB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

//        builder.addInterceptor(ApiClient.addQueryParameterInterceptor);//这個是机呼項目注释的
        builder.addInterceptor(new CodeInterceptor(AppContext.getContext()));
        builder.addInterceptor(new CacheInterceptor(AppContext.getContext()));
//        if (TDevice.isDebug()) {
//            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            builder.addInterceptor(interceptor);
//        }

        builder.readTimeout(GlobalConstant.READ_TIME_OUT, TimeUnit.SECONDS);
        builder.connectTimeout(GlobalConstant.CONNECT_TIME_OUT, TimeUnit.SECONDS);
        //添加快取策略
        builder.cache(cache);
        OkHttpClient okHttpClient = builder
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL) // 定义访問的主机地址
                .addConverterFactory(GsonConverterFactory.create())  //解析方法
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    public static void addCacheINterceptor(Interceptor cacheInterceptor) {
        mCacheInter = cacheInterceptor;
    }


    public static void addTokenInterceptor(Interceptor interceptor) {
        mTokenInter = interceptor;
    }

    /**
     * 单例模式
     *
     * @return
     */
    public static Retrofit getInstance(Context context) {

        if (retrofit == null) {
            synchronized (NetUtils.class) {
                if (retrofit == null) {
                    retrofit = getRetrofit(context.getApplicationContext());
                }
            }
        }
        return retrofit;
    }

    //IP 地址
    static String BASE_URL = "http://api.qingke.cc/";
}
