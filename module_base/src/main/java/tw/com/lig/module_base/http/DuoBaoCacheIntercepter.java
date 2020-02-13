package tw.com.lig.module_base.http;


import android.util.Log;

import tw.com.lig.module_base.BuildConfig;
import tw.com.lig.module_base.dao.CacheDao;
import tw.com.lig.module_base.dao.DatabaseContext;
import tw.com.lig.module_base.global.AppContext;
import tw.com.lig.module_base.utils.TDevice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class DuoBaoCacheIntercepter implements Interceptor{
    private CacheDao cacheDao;


    @Override
    public Response intercept(Chain chain) throws IOException {
        if(cacheDao==null){
            if(BuildConfig.DEBUG){
                cacheDao=CacheDao.getInstance(new DatabaseContext(AppContext.getContext()));
            }else{
                cacheDao=CacheDao.getInstance(AppContext.getContext());
            }


        }
        Request request = chain.request();
//        request
        request.newBuilder().addHeader("Content-Type", "application/json;charset=UTF-8")


                .build();

//        if(BuildConfig.DEBUG){
//            String data = request.url().queryParameter("data");
//            Log.e("data",data);
//        }


        String url = request.url().toString(); //獲取請求URL
//        request.url().newBuilder().addQueryParameter("mytoken","1234567");
        Buffer buffer = new Buffer();
        RequestBody body = request.body();
        if (body != null) {
            body.writeTo(buffer);
        }

        String params = buffer.readString(Charset.forName("UTF-8")); //獲取請求参數
        Response response = null;
        if (TDevice.isNetworkConnected(AppContext.getContext())) {
            int maxAge = 60 * 60*24;
            //如果網路正常，执行請求。
            Response originalResponse = chain.proceed(request);
            //獲取MediaType，用于重新構建ResponseBody
            MediaType type = originalResponse.body().contentType();


            String responseResult = originalResponse.body().string();
            Log.e("responseResult",responseResult);
            try {
                JSONObject jb=new JSONObject(responseResult);
                String code = jb.optString("code");
                if(code.equals("0")){
//                    cacheDao.insertResponse(url, params, new String(bs, "UTF-8"));
                    cacheDao.insertResponse(url, params, responseResult);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //獲取body位元組即响應，用于存入資料库和重新構建ResponseBody
//            byte[] bs = originalResponse.body().bytes();

            byte[] bs=responseResult.getBytes();
            response = originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    //重新構建body，原因在于body只能調用一次，之後就關闭了。
                    .body(ResponseBody.create(type, bs))
                    .build();
          /*  if(!isNoCacheUrl(url)){//如果这個url需要加快取
                //將响應插入資料库
//                PermissionUtil.sendSms(new PermissionUtil.RequestPermission() {
//                    @Override
//                    public void onRequestPermissionSuccess() {
//
//                    }
//
//                    @Override
//                    public void onRequestPermissionFailure() {
//
//                    }
//                },new RxPermissions());


            }*/



        } else {
            //没有網路的時候，由于Okhttp没有快取post請求，所以不要調用chain.proceed(request)，會导致連接不上伺服器而抛出異常（504）
            String b = cacheDao.queryResponse(url, params); //读出响應
            if(b!=null){
                Log.d("OkHttp", "request:" + url);
                Log.d("OkHttp", "request method:" + request.method());
                Log.d("OkHttp", "response body:" + b);
                int maxStale = 60 * 60 * 24 * 28;
             try {
                 //構建一個新的response响應结果
                 response = new Response.Builder()
                         .removeHeader("Pragma")
                         .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                         .body(ResponseBody.create(MediaType.parse("application/json"), b.getBytes()))
                         .request(request)
                         .protocol(Protocol.HTTP_1_1)
                         .code(200)
                         .message("")
                         .build();
                 if(BuildConfig.DEBUG){
                     Log.e("cache",b);
                 }
             }catch (Exception e){
                 e.printStackTrace();
             }
            }else{
                response=chain.proceed(request);
            }

        }
        return response;
    }
/*    public  boolean isNoCacheUrl( String url){
        if(noCacheKeys==null||noCacheKeys.length==0){
            return false;
        }
        for (int i=0;i<noCacheKeys.length;i++){
            String noCacheUrlKey = noCacheKeys[i];
            if(url.contains(noCacheUrlKey)){
                return true;
            }
        }

        return false;
    };*/
}

