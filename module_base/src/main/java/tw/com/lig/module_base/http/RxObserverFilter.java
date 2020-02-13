package tw.com.lig.module_base.http;

import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.os.NetworkOnMainThreadException;
import android.text.TextUtils;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import tw.com.lig.module_base.BuildConfig;
import tw.com.lig.module_base.base.BaseView;
import tw.com.lig.module_base.data.SPConstant;
import tw.com.lig.module_base.entity.BaseEntity;
import tw.com.lig.module_base.global.AppContext;
import tw.com.lig.module_base.utils.KJActivityStack;
import tw.com.lig.module_base.utils.SPutils;
import tw.com.lig.module_base.utils.TDevice;

import org.json.JSONException;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

//import android.support.v4.app.Fragment;
//import com.whyhow.global.ShareData;
//import com.umeng.analytics.MobclickAgent;

//import com.alibaba.android.arouter.launcher.ARouter;

public abstract class RxObserverFilter<T> implements Observer<T> {
    private BaseView mBaseView;
    private boolean showErrorIfNetDisconnect =true;  //是否在没網路的時候就显示errowView
    private boolean showContentIfSuccess =true;//是否在網路請求成功的時候就显示出contentview
    private boolean showErrorIfCodeNotSuccess =true;//是否在網路請求code不為0并且有baseLayout的時候就显示未知異常，(可能需要重寫onCodeError方法)

    private static final String TAG = RxObserverFilter.class.getName();
    private static final String JSON_PARSE_EXCEPTION_MSG="Expected BEGIN_ARRAY but was BEGIN_OBJECT";

    public RxObserverFilter() {

    }
    public RxObserverFilter(BaseView baseView){
        mBaseView=baseView;

    }
    //showErrorIfNetDisconnect
    public RxObserverFilter(BaseView mBaseView, boolean showContentIfSuccess, boolean showErrorIfNetDisconnect, boolean showErrorIfCodeNotSuccess) {
        this.mBaseView = mBaseView;
        this.showErrorIfNetDisconnect = showErrorIfNetDisconnect;
        this.showContentIfSuccess = showContentIfSuccess;
        this.showErrorIfCodeNotSuccess = showErrorIfCodeNotSuccess;
    }

    public RxObserverFilter(BaseView baseView, boolean showContentIfSuccess){
        mBaseView=baseView;
        this.showContentIfSuccess = showContentIfSuccess;

    }

    public RxObserverFilter(BaseView mBaseView,  boolean showContentIfSuccess,boolean showErrorIfError) {
        this.mBaseView = mBaseView;
        this.showErrorIfNetDisconnect = showErrorIfError;
        this.showErrorIfCodeNotSuccess=showErrorIfError;
        this.showContentIfSuccess = showContentIfSuccess;
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


                if (t instanceof BaseEntity) {
                    Log.d("steven","raw response code:"+ ((BaseEntity) t).getCode());
                    Log.d("steven","raw reponse msg:"+ ((BaseEntity) t).getMessage());
                    Log.d("steven","raw reponse data:"+ ((BaseEntity) t).getData());

                    BaseEntity baseEntity=(BaseEntity) t;

                    if(baseEntity.isTokenInvakid()){

                        if(mBaseView!=null){
                            String message = baseEntity.getMessage();
                            mBaseView.showToast(message);
                            kicked();

                        }else{
                            if(BuildConfig.DEBUG&&mBaseView!=null){
                                mBaseView.showToast("token失效，baseView為空");
                            }
                        }
                    }/*else if(baseEntity.isAccountLocked()){//被冻结
//                        SPutils.put(AppContext.getContext(), SPConstant.TOKEN,"");
//                        if(mBaseView!=null){
//                            mBaseView.showAccountExceptionDialog();
//                            mBaseView.showEmptyView();
//                        }
                        if(mBaseView!=null){
                            BaseEntity be=(BaseEntity) t;
                            mBaseView.showToast(be.getMessage());

//                        mBaseView.showAccountExceptionDialog();
//                        mBaseView.showEmptyView();
                        }else{

                        }
//                        ARouter.getInstance().build(ARouterConstant.VCODE_LOGIN).navigation();
                        KJActivityStack.create().finishAllActivity();

                    }*/
                    else if (((BaseEntity) t).isSuccess()) {

                        if(mBaseView!=null&& showContentIfSuccess){
                            mBaseView.showContentView();
                        }else{
//                            if(BuildConfig.DEBUG&&mBaseView!=null){
//                                mBaseView.showToast("baseView為空");
//                            }
                        }
                        onSuccees(t);

                        if (!TDevice.isNetworkConnected(AppContext.getContext())){
                            if (mBaseView!=null){
                                mBaseView.showToast("連接異常，請檢查網路");
                            }
                        }
                    }
                    else if(((BaseEntity) t).getCode()!=0){ //code其余非0的情況
//                        if(mBaseView.hasBaseLayout()){
//                            mBaseView.showEmptyView();
//                        }
                        String message = ((BaseEntity) t).getMessage();
                        if (!TextUtils.isEmpty(message)){
                            if (mBaseView!=null){
                                if (showErrorIfCodeNotSuccess){
                                    mBaseView.showToast(message);
                                    mBaseView.stopProgressDialog();

                                    if (mBaseView.hasBaseLayout()){

                                        mBaseView.showErrorView();
                                    }else{
                                        boolean b = onCodeError(t);

                                        if (!b) {//若自己不处理就默认处理
                                            handleError(new Throwable(((BaseEntity) t).getMessage()));
                                        }
                                    }
                                }else{
                                    boolean b = onCodeError(t);

                                    if (!b) {//若自己不处理就默认处理
                                        handleError(new Throwable(((BaseEntity) t).getMessage()));
                                    }
                                }

                            }else{
                                boolean b = onCodeError(t);

                                if (!b) {//若自己不处理就默认处理
                                    handleError(new Throwable(((BaseEntity) t).getMessage()));
                                }
                            }
                        }

                    }else {
                        if (!TDevice.isNetworkConnected(AppContext.getContext())){
                            if (mBaseView!=null){
                                mBaseView.showToast("連接異常，請檢查網路");
                            }
                        }
                        boolean b = onCodeError(t);

                        if (!b) {//若自己不处理就默认处理
                            handleError(new Throwable(((BaseEntity) t).getMessage()));
                        }
                    }
                } else {
                    handleError(new Throwable("T must be BaseEntity"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            handleError(e);
            //其他異常需要統計一下💗💗
//            MobclickAgent.reportError(AppContext.getContext(),e);

        }

    }

    /**
     * 出現了異地登錄
     */
    private void kicked() {
        SPutils.put(AppContext.getContext(), SPConstant.KEY_TOKEN,"");
//        SPutils.put(AppContext.getContext(), ShareData.HAS_AUTH,false);


        KJActivityStack.create().finishAllActivity();
        if (mBaseView==null){
            return;
        }

        if (mBaseView instanceof Activity){
            Activity activity=(Activity)mBaseView;
            Intent intent = new Intent(activity.getPackageName()+".login");
            intent.putExtra("isExit",true);
            intent.putExtra("isKicked",true);
            activity.startActivity(intent);

        }else if(mBaseView instanceof Fragment){

            Fragment fragment=(Fragment)mBaseView;
//                                FragmentActivity activity = fragment.getActivity();
            Intent intent = new Intent(fragment.getContext().getPackageName()+".login");
            intent.putExtra("isExit",true);
            intent.putExtra("isKicked",true);
            fragment.startActivity(intent);
        }
    }


    @Override
    public void onError(Throwable e) {
//        e.
        handleError(e);
    }


    @Override
    public void onComplete() {

    }

    public  void handleError(Throwable t) {
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
            msg = "資料解析错误";
        } else if (t instanceof NullPointerException) {
            msg = "未知異常,請聯繫客服人员";
        } else if (t instanceof EOFException) { //伺服器接口没返回資料
            msg = "伺服器發生错误";
        } else if(t instanceof NetworkOnMainThreadException) {
            msg = "網路請求不能在主執行緒执行";
        }else if(t instanceof JsonSyntaxException){
//              JsonSyntaxException jsonSyntaxException= (JsonSyntaxException) t;
           /*   String localizedMessage = jsonSyntaxException.getLocalizedMessage();
              jsonSyntaxException.
              jsonSyntaxException.g
              JsonObject jsonObject=new JsonObject()*/
            msg="json解析異常:"+t.getMessage(); //这种情況下很可能是出現了異地登錄的情況
        }/*else if(t instanceof retrofit2.adapter.rxjava2.HttpException){

            msg="参數错误";
        }*/
        else{

            msg = t.getMessage();
            //其他異常需要統計一下


        }

//        if (BuildConfig.DEBUG_MODEL) {//debug
        if (BuildConfig.DEBUG) {//debug
            if (msg!=null&&msg.contains("End of input at line 1 column 1 path $")) {
                msg = "服務端請求没有返回结果";
            }
        } else {//release
            //友盟错误分析
        }
//        if(BuildConfig.DEBUG){
     /*       Looper.prepare();

            Toast.makeText(AppContext.getContext(), msg, Toast.LENGTH_SHORT).show();
            Looper.loop();*/
        if(mBaseView!=null){
            mBaseView.showToast(msg);
            mBaseView.stopProgressDialog();
        }

//        }

        try {
            if (showErrorIfNetDisconnect){

                onFailure(new Throwable(t));
            }else{
                if (t instanceof UnknownHostException || t instanceof SocketTimeoutException ||t instanceof SocketException) {
                    onNetTimeOutOrDisconnetedAndDontShowErrorView();


                    if (!TDevice.isNetworkConnected(AppContext.getContext())){
                        if (mBaseView!=null){
                            mBaseView.showToast("連接異常，請檢查網路");
                        }
                    }

                } else{
                    onFailure(new Throwable(t));
                }
            }

            Log.i(TAG, "handleError()", t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 返回成功
     *
     * @param responseData
     */
    public  abstract void onSuccees(T responseData) throws Exception;

    /**
     * 返回失败
     *
     * @param e
     * @throws Exception
     */
    protected  void onFailure(Throwable e) throws Exception{
        if(mBaseView!=null&&(showErrorIfCodeNotSuccess||showErrorIfNetDisconnect)){
            String message = e.getMessage();
            System.out.println(message);

            if(message.contains("SocketException")  || message.contains("SocketTimeoutException") ){
                mBaseView.showErrorView("請求網路超時");
            }else{
                if (!TDevice.isNetworkConnected(AppContext.getContext())){
                    mBaseView.showErrorView();
                }else{

                    mBaseView.showErrorView("未知異常，請聯繫客服");
                    Log.e("RxObserverFilter",message);
                }

            }
        }

    };

    /**
     * 返回失败s
     *
     * @param responseData
     * @param //           true:自己主动处理；false ：默认处理
     * @throws Exception
     */
    public  boolean onCodeError(T responseData) throws Exception {
        return false;
    }

    /**
     *  没有網路或者網路超時，并且不显示errorView的時候
     *  （主要用在列表載入更多或者下载刷新的時候，重置載入控件的狀態更新UI，
     *  比如让IRecyclerview的loading狀態变為GONE）
     */
    protected  void onNetTimeOutOrDisconnetedAndDontShowErrorView(){

    }

}
