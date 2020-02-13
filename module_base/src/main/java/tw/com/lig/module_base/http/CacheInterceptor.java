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
 * 網路請求發送時候的攔截器
 */

public class CacheInterceptor implements Interceptor {

    private final Context mContext;
    private final CacheControl cacheControl;//CacheControl是針對Request的

//- noCache();//不使用快取，用網路請求
//- noStore();//不使用快取，也不儲存快取
//- onlyIfCached();//只使用快取
//- noTransform();//禁止轉碼
//- maxAge(10, TimeUnit.MILLISECONDS);//設定超時時間為10ms。
//- maxStale(10, TimeUnit.SECONDS);//超時之外的超時時間為10s
//- minFresh(10, TimeUnit.SECONDS);//超時時間為當前時間加上10秒鐘。


    /**
     * max-age：這個參數告訴瀏覽器將頁面快取多長時間，超過這個時間後才再次向伺服器發起請求檢查頁面是否有更新。對于靜態的頁面，比如圖片、CSS、Javascript，一般都不大變更，因此通常我們將儲存這些內容的時間設定為較長的時間，這樣瀏覽器會不會向瀏覽器反復發起請求，也不會去檢查是否更新了。
     * s-maxage：這個參數告訴快取伺服器(proxy，如Squid)的快取頁面的時間。如果不單獨指定，快取伺服器將使用max-age。對于動態內容(比如文檔的查看頁面)，我們可告訴瀏覽器很快就過時了(max-age=0)，並告訴快取伺服器(Squid)保留內容一段時間(比如，s-maxage=7200)。一旦我們更新文檔，我們將告訴Squid清除老的快取版本。
     * must-revalidate：這告訴瀏覽器，一旦快取的內容過期，一定要向伺服器詢問是否有新版本。
     * proxy-revalidate：proxy上的快取一旦過期，一定要向伺服器詢問是否有新版本。
     * no-cache：不做快取。
     * no-store：資料不在硬碟中臨時保存，這對需要保密的內容比較重要。
     * public：告訴快取伺服器, 即便是對于不該快取的內容也快取起來，比如當用戶已經認證的時候。所有的靜態內容(圖片、Javascript、CSS等)應該是public的。
     * private：告訴proxy不要快取，但是瀏覽器可使用private cache進行快取。一般登錄後的個性化頁面是private的。
     * no-transform: 告訴proxy不進行轉換，比如告訴手機瀏覽器不要下載某些圖片。
     * max-stale指示客戶機可以接收超出超時期間的響應消息。如果指定max-stale消息的值，那麼客戶機可以接收超出超時期指定值之內的響應消息
     *
     * @param context
     */

    public CacheInterceptor(Context context) {
        this.mContext = context;
        CacheControl.Builder cacheBuilder = new CacheControl.Builder();
        cacheBuilder.maxAge(60, TimeUnit.SECONDS);//這個是控制快取的最大生命時間
        cacheBuilder.maxStale(100, TimeUnit.SECONDS);//這個是控制快取的過時時間
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
