package tw.com.lig.module_base.http;


import android.net.ParseException;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonIOException;
import tw.com.lig.module_base.BuildConfig;
import tw.com.lig.module_base.base.BaseView;
import tw.com.lig.module_base.data.SPConstant;
import tw.com.lig.module_base.entity.BaseEntity;
import tw.com.lig.module_base.global.AppContext;
import tw.com.lig.module_base.utils.KJActivityStack;
import tw.com.lig.module_base.utils.SPutils;

import org.json.JSONException;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

//import com.alibaba.android.arouter.launcher.ARouter;

public abstract  class RxConsumer<T> implements Consumer<T> {
    private static final String TAG = "RxConsumer";
    private BaseView mBaseView;
    private boolean isShowContentViewWhenSuccess;//是否在網路請求成功的時候就顯示出contentview
    public RxConsumer(){


    }
    public RxConsumer(BaseView baseView){
        mBaseView=baseView;

    }

    public RxConsumer(BaseView mBaseView, boolean isShowContentViewWhenSuccess) {
        this.mBaseView = mBaseView;
        this.isShowContentViewWhenSuccess = isShowContentViewWhenSuccess;
    }

    @Override
    public void accept(T t) throws Exception {
        if (t == null) {
            handleError(new Throwable("callback of data is null"));
        }else {
            if (t instanceof BaseEntity) {
                if(((BaseEntity) t).isTokenInvakid()){
                    if(mBaseView!=null){
//                            Activity activity=(Activity)mBaseView;
//                            activity.finish();
//                            mBaseView.showErrorView();
                        SPutils.put(AppContext.getContext(), SPConstant.KEY_TOKEN,"");
                        mBaseView.showLoginExceptionDialog(mBaseView);
                        mBaseView.showErrorView();

                    }else{
                        if(BuildConfig.DEBUG&&mBaseView!=null){
                            mBaseView.showToast("token失效，baseView為空");
                        }
                    }
                }else if(((BaseEntity) t).isAccountLocked()){//被凍結
                    SPutils.put(AppContext.getContext(), SPConstant.KEY_TOKEN,"");
                    if(mBaseView!=null){
                        BaseEntity be=(BaseEntity) t;
                        mBaseView.showToast(be.getMessage());

//                        mBaseView.showAccountExceptionDialog();
//                        mBaseView.showEmptyView();
                    }else{

                    }
//                    ARouter.getInstance().build(ARouterConstant.VCODE_LOGIN).navigation();
                    KJActivityStack.create().finishAllActivity();


                }
                else if (((BaseEntity) t).isSuccess()) {
                    if(mBaseView!=null&&isShowContentViewWhenSuccess){
                        mBaseView.showContentView();
                    }

               /*     if(mBaseView!=null){
                        mBaseView.showContentView();
                    }else{
//                            if(BuildConfig.DEBUG&&mBaseView!=null){
//                                mBaseView.showToast("baseView為空");
//                            }
                    }*/
                    onSuccees(t);

                } else {
                    boolean b = onCodeError(t);

                    if (!b) {//若自己不處理就預設處理
                        handleError(new Throwable(((BaseEntity) t).getMessage()));
                    }
                }
            } else {
                handleError(new Throwable("T must be BaseEntity"));
            }
        }
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
     * @param responseData
     * @param //           true:自己主動處理；false ：預設處理
     * @throws Exception
     */
    protected boolean onCodeError(T responseData) throws Exception {
        return false;
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
            msg = StatusCodeUtil.convertStatusCode(httpException);
        } else if (t instanceof ParseException || t instanceof JSONException || t instanceof JsonIOException) {
            msg = "資料解析錯誤";
        } else if (t instanceof NullPointerException) {
            msg = "未知異常,請聯繫客服人員";
        } else if (t instanceof EOFException||t instanceof  Error) { //伺服器介面沒返回資料
            msg = "伺服器發生錯誤";
        } else if(t instanceof NetworkOnMainThreadException) {
            msg = "網路請求不能在主執行緒執行";
        }
//         else if(t instanceof RuntimeException){
//            RuntimeException runtimeException=(RuntimeException)t;
//            msg=runtimeException.ge
//
//        }
        else{
            msg = t.getMessage();
        }

//        if (BuildConfig.DEBUG_MODEL) {//debug
        if (BuildConfig.DEBUG) {//debug
            if (msg!=null&&msg.contains("End of input at line 1 column 1 path $")) {
                msg = "服務端請求沒有返回結果";
            }
        } else {//release
            //友盟錯誤分析
        }
//        if(BuildConfig.DEBUG){
//        Looper.prepare();

        Toast.makeText(AppContext.getContext(), msg, Toast.LENGTH_SHORT).show();
//        Looper.loop();
//        }

        try {
            onFailure(new Throwable(t));

            Log.i(TAG, "handleError()", t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   protected  void onFailure(Throwable e) throws Exception{
        if(mBaseView!=null){
            mBaseView.showErrorView();
        }

    };
    
}
