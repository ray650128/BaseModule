package tw.com.lig.module_base.http;

import android.text.TextUtils;
import android.util.Log;

import tw.com.lig.module_base.data.SPConstant;
import tw.com.lig.module_base.utils.SPutils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

//import com.whyhow.global.ShareData;


public class HeaderIntercepter implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();


        // Request customization: add request headers
        String token = SPutils.getString(SPConstant.KEY_TOKEN);
        Request.Builder requestBuilder = original.newBuilder();
                if(!TextUtils.isEmpty(token)){
                    requestBuilder.addHeader("token", token);
                    Log.d("steven","token:"+ token);
                }

                /*.addHeader("device", GlobalConstant.DEBUG_DEVICE)
                .addHeader("appChannel", "41")
                .addHeader("os", "android")
                .addHeader("model", Build.MODEL)
                .addHeader("version", TDevice.getVersionName())*/;


        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
