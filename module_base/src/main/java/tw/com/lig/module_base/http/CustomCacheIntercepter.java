
package tw.com.lig.module_base.http;


import android.util.Log;

import tw.com.lig.module_base.BuildConfig;
import tw.com.lig.module_base.dao.CacheDao;
import tw.com.lig.module_base.dao.DatabaseContext;
import tw.com.lig.module_base.global.AppContext;
import tw.com.lig.module_base.utils.TDevice;

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

public class CustomCacheIntercepter implements Interceptor{
    private CacheDao cacheDao;


    @Override
    public Response intercept(Chain chain) throws IOException {
        if(cacheDao==null){
            if(BuildConfig.DEBUG){
//                cacheDao=CacheDao.getInstance(AppContext.getContext());

                cacheDao=CacheDao.getInstance(new DatabaseContext(AppContext.getContext()));
            }else{
                cacheDao=CacheDao.getInstance(AppContext.getContext());
            }


        }
        Request request = chain.request();






//        if(BuildConfig.DEBUG){
//            String data = request.url().queryParameter("data");
//            Log.e("data",data);
//        }


        String url = request.url().toString(); //獲取請求URL
//        String token = request.header("token");
//        Log.e("header_token",token);


        RequestBody requestBody = request.body();

//        if (requestBody != null) {//post方法(改為下面的這種方法判斷post方法)
        if (request.method().toLowerCase().equals("post") ){//post方法
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            String params = buffer.readString(Charset.forName("UTF-8")); //獲取請求參數

            Response response = null;
            if (TDevice.isNetworkConnected(AppContext.getContext())) {
                int maxAge = 60 * 60*24;
                //如果網路正常，執行請求。
                Response originalResponse = chain.proceed(request);
                //獲取MediaType，用於重新構建ResponseBody
                MediaType type = originalResponse.body().contentType();


                String responseResult = originalResponse.body().string();
                Log.e("responseResult",responseResult);



                cacheDao.insertResponse(url, params, responseResult);
       /*     try {
//                JSONObject jb=new JSONObject(responseResult);
//                String code = jb.optString("code");
        *//*        if(code.equals("0")){
//                    cacheDao.insertResponse(url, params, new String(bs, "UTF-8"));
                }*//*

            } catch (JSONException e) {
                e.printStackTrace();
            }*/


                //獲取body位元組即響應，用於存入資料庫和重新構建ResponseBody
//            byte[] bs = originalResponse.body().bytes();

                byte[] bs=responseResult.getBytes();
                response = originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        //重新構建body，原因在於body只能調用一次，之後就關閉了。
                        .body(ResponseBody.create(type, bs))
                        .build();


            } else {
//            Looper.prepare();
//            Toast.makeText(AppContext.getContext(), "連接異常，請檢查網路", Toast.LENGTH_SHORT).show();
//            Looper.loop();
                //沒有網路的時候，由於Okhttp沒有快取post請求，所以不要調用chain.proceed(request)，會導致連接不上伺服器而拋出異常（504）
                String b = cacheDao.queryResponse(url, params); //讀出響應
                if(b!=null){
                    Log.d("OkHttp", "request:" + url);
                    Log.d("OkHttp", "request method:" + request.method());
                    Log.d("OkHttp", "response body:" + b);
                    int maxStale = 60 * 60 * 24 * 28;
                    try {
                        //構建一個新的response響應結果
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
        }else{//get方法
            String url_get = request.url().toString();
            Log.e("get請求的Url",url_get);

            Response response = null;
            if (TDevice.isNetworkConnected(AppContext.getContext())) {//有網路
                int maxAge = 60 * 60*24;
                //如果網路正常，執行請求。
                Response originalResponse = chain.proceed(request);
                //獲取MediaType，用於重新構建ResponseBody
                MediaType type = originalResponse.body().contentType();


                String responseResult = originalResponse.body().string();
                Log.e("responseResult",responseResult);
                //注意這裡把用戶的token和urL拼在了一起
                cacheDao.insertGetResponse(url, responseResult);
       /*     try {
//                JSONObject jb=new JSONObject(responseResult);
//                String code = jb.optString("code");
        *//*        if(code.equals("0")){
//                    cacheDao.insertResponse(url, params, new String(bs, "UTF-8"));
                }*//*

            } catch (JSONException e) {
                e.printStackTrace();
            }*/


                //獲取body位元組即響應，用於存入資料庫和重新構建ResponseBody
//            byte[] bs = originalResponse.body().bytes();

                byte[] bs=responseResult.getBytes();
                response = originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        //重新構建body，原因在於body只能調用一次，之後就關閉了。
                        .body(ResponseBody.create(type, bs))
                        .build();


            } else {//沒網路
//            Looper.prepare();
//            Toast.makeText(AppContext.getContext(), "連接異常，請檢查網路", Toast.LENGTH_SHORT).show();
//            Looper.loop();
                //沒有網路的時候，由於Okhttp沒有快取post請求，所以不要調用chain.proceed(request)，會導致連接不上伺服器而拋出異常（504）
                String b = cacheDao.queryGetResponse(url); //讀出響應
                if(b!=null){
                    Log.d("OkHttp", "request:" + url);
                    Log.d("OkHttp", "request method:" + request.method());
                    Log.d("OkHttp", "response body:" + b);
                    int maxStale = 60 * 60 * 24 * 28;
                    try {
                        //構建一個新的response響應結果
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


           /* if (TDevice.isNetworkConnected(AppContext.getContext())) {//有網路
                return chain.proceed(request);

            }else{//沒有網路
                return chain.proceed(request);
            }*/


        }

    }

}


