package tw.com.lig.module_base.http;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

//import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by ljk on 2017/2/24.
 * 拦截接口返回的狀態碼 3標识需要重新登錄
 */
public class CodeInterceptor implements Interceptor {
    private final Context mContext;
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public CodeInterceptor(Context context) {
        this.mContext = context;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();

        //注意 >>>>>>>>> okhttp3.4.1這裡变成了 !HttpHeader.hasBody(response)
        //if (!HttpEngine.hasBody(response)) {
        if (!HttpHeaders.hasBody(response)) {
            //END HTTP
        } else if (bodyEncoded(response.headers())) {
            //HTTP (encoded body omitted)
        } else {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();


            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    //Couldn't decode the response body; charset is likely malformed.
                    return response;
                }
            }

            if (!isPlaintext(buffer)) {
                return response;
            }

            if (contentLength != 0) {
                //獲取到response的body的string字串
                String result = buffer.clone().readString(charset);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject != null && !jsonObject.isNull("code")) {
                        int code = jsonObject.getInt("code");
                        String message = jsonObject.getString("message");
                        if (code == Api.ATOKEN_INVALID) {//atoken 失效
//                            SPutils.put(AppContext.getContext(), SPConstant.KEY_TOKEN, "");

                           /* ARouter.getInstance().build(ARouterConstant.LOGIN)
                                    .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                    .withBoolean("isJumpToMain", true)     //传值 ，類似于putExtra
                                    .navigation();*/
                            Looper.prepare();
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            Looper.loop();
                            return response;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    static boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

}
