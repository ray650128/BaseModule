package tw.com.lig.module_base.http;

import android.net.ParseException;
import android.widget.Toast;

import com.google.gson.JsonIOException;
import tw.com.lig.module_base.BuildConfig;
import tw.com.lig.module_base.entity.BaseEntityBig;
import tw.com.lig.module_base.global.AppContext;

import org.json.JSONException;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * 作者：${weihaizhou} on 2018/1/29 13:56
 */
public abstract class RxObserverFilterBig<T> implements Observer<T> {

    public RxObserverFilterBig() {
    }

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(T t) {
        try {
            if (t == null) {
                handleError(new Throwable("callback of data is null"));
            } else {
                if (t instanceof BaseEntityBig) {
                    if (((BaseEntityBig) t).isSuccess()) {
                        onSuccees(t);
                    } else {
                        boolean b = onCodeError(t);
                        if (!b) {//若自己不處理就預設處理
                            handleError(new Throwable(((BaseEntityBig) t).getMessage()));
                        }
                    }
                } else {
                    handleError(new Throwable("T must be BaseEntityBig"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            handleError(e);

        }

    }


    @Override
    public void onError(Throwable e) {
        handleError(e);
    }


    @Override
    public void onComplete() {
    }

    private void handleError(Throwable t) {
        String msg;
        if (t instanceof UnknownHostException) {
            msg = "連接異常，請檢查網路";
        } else if (t instanceof ConnectException) {
            msg = "連接異常，請檢查網路";
        } else if (t instanceof SocketTimeoutException) {
            msg = "請求網路超時";
        } else if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            msg = convertStatusCode(httpException);
        } else if (t instanceof ParseException || t instanceof JSONException || t instanceof JsonIOException) {
            msg = "資料解析錯誤";
        } else if (t instanceof NullPointerException) {
            msg = "未知異常,請聯繫客服人員";
        } else if (t instanceof EOFException) { //伺服器介面沒返回資料
            msg = "伺服器發生錯誤";
        } else {
            msg = t.getMessage();
        }

//        if (BuildConfig.DEBUG_MODEL) {//debug
        if (BuildConfig.DEBUG) {//debug
            if (msg.contains("End of input at line 1 column 1 path $")) {
                msg = "服務端請求沒有返回結果";
            }
        } else {//release
            //友盟錯誤分析
        }
        Toast.makeText(AppContext.getContext(), msg, Toast.LENGTH_SHORT).show();

        try {
            onFailure(new Throwable(t));
//            Logger.i("onError" + t.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertStatusCode(HttpException httpException) {
        String msg;
        if (httpException.code() == 500) {
            msg = "伺服器錯誤";
        } else if (httpException.code() == 504) {
            msg = "連接異常，請檢查網路";
        } else if (httpException.code() == 404) {
            msg = "請求地址不存在";
        } else if (httpException.code() == 403) {
            msg = "請求被伺服器拒絕";
        } else if (httpException.code() == 307) {
            msg = "請求被重定向到其他頁面";
        } else {
            msg = httpException.message();
        }
        return msg;
    }

    /**
     * 返回成功
     *
     * @param responseData
     */
    protected abstract void onSuccees(T responseData) throws Exception;

    /**
     * 返回失敗
     *
     * @param e
     * @throws Exception
     */
    protected abstract void onFailure(Throwable e) throws Exception;

    /**
     * 返回失敗
     *
     * @param responseData
     * @param //           true:自己主動處理；false ：預設處理
     *
     * @throws Exception
     */
    protected boolean onCodeError(T responseData) throws Exception {
        return false;
    }


}
