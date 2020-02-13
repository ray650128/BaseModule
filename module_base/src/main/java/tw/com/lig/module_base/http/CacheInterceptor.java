package tw.com.lig.module_base.http;

import android.content.Context;

import tw.com.lig.module_base.utils.TDevice;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ljk on 2017/2/24.
 * 網路請求发送時候的拦截器
 */

public class CacheInterceptor implements Interceptor {

    private final Context mContext;
    private final CacheControl cacheControl;//CacheControl是针對Request的

//- noCache();//不使用快取，用網路請求
//- noStore();//不使用快取，也不儲存快取
//- onlyIfCached();//只使用快取
//- noTransform();//禁止轉碼
//- maxAge(10, TimeUnit.MILLISECONDS);//設定超時時間為10ms。
//- maxStale(10, TimeUnit.SECONDS);//超時之外的超時時間為10s
//- minFresh(10, TimeUnit.SECONDS);//超時時間為当前時間加上10秒钟。


    /**
     * max-age：这個参數告诉浏览器將頁面快取多长時間，超过这個時間後才再次向伺服器发起請求檢查頁面是否有更新。對于静态的頁面，比如圖片、CSS、Javascript，一般都不大变更，因此通常我们將儲存这些内容的時間設定為较长的時間，这样浏览器會不會向浏览器反复发起請求，也不會去檢查是否更新了。
     * s-maxage：这個参數告诉快取伺服器(proxy，如Squid)的快取頁面的時間。如果不单独指定，快取伺服器將使用max-age。對于动态内容(比如文档的查看頁面)，我们可告诉浏览器很快就过時了(max-age=0)，并告诉快取伺服器(Squid)保留内容一段時間(比如，s-maxage=7200)。一旦我们更新文档，我们將告诉Squid清除老的快取版本。
     * must-revalidate：这告诉浏览器，一旦快取的内容过期，一定要向伺服器询問是否有新版本。
     * proxy-revalidate：proxy上的快取一旦过期，一定要向伺服器询問是否有新版本。
     * no-cache：不做快取。
     * no-store：資料不在硬盘中临時保存，这對需要保密的内容比较重要。
     * public：告诉快取伺服器, 即便是對于不該快取的内容也快取起来，比如当用户已经认证的時候。所有的静态内容(圖片、Javascript、CSS等)應該是public的。
     * private：告诉proxy不要快取，但是浏览器可使用private cache进行快取。一般登錄後的個性化頁面是private的。
     * no-transform: 告诉proxy不进行轉換，比如告诉手機浏览器不要下载某些圖片。
     * max-stale指示客户机可以接收超出超時期間的响應消息。如果指定max-stale消息的值，那么客户机可以接收超出超時期指定值之内的响應消息
     *
     * @param context
     */

    public CacheInterceptor(Context context) {
        this.mContext = context;
        CacheControl.Builder cacheBuilder = new CacheControl.Builder();
        cacheBuilder.maxAge(60, TimeUnit.SECONDS);//这個是控制快取的最大生命時間
        cacheBuilder.maxStale(100, TimeUnit.SECONDS);//这個是控制快取的过時時間
        cacheControl = cacheBuilder.build();
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (TDevice.isNetworkConnected(mContext)) {//網路可用
//            if (TextUtils.isEmpty(request.header("Cache-Control"))) {
//                request = request.newBuilder()
//                        .removeHeader("Pragma")
//                        .cacheControl(cacheControl)
//                        .build();
//            }
        } else {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }


        Response originalResponse = chain.proceed(request);
        if (TDevice.isNetworkConnected(mContext)) {
            int maxAge = 60;// read from cache
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public ,max-age=" + maxAge)
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 28;// tolerate 4-weeks stale

            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
    }
}
