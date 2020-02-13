package tw.com.lig.module_base.utils;

import android.util.Log;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import tw.com.lig.module_base.base.BaseView;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jess on 11/10/2016 16:39
 * Contact with jess.yan.effort@gmail.com
 */

public class RxUtils {

    private RxUtils() {

    }

    /**
     * 用来在網路請求前後显示隐藏进度条
     *
     * @param view
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T,T> applySchedulersWithLoading(final BaseView view) {
        return new ObservableTransformer<T, T>() {
            @Override
            public Observable<T> apply(Observable<T> observable) {
//                if(view.){}
                return observable.subscribeOn(Schedulers.io())
                        .doOnSubscribe(disposable -> {
                            Log.d(TAG, "apply: doOnSubscribe");

                            view.startProgressDialog();//显示进度条
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnDispose(() -> {
                            Log.d(TAG, "apply: doOnDispose");
                            view.stopProgressDialog();//隐藏进度条
                        })
                        .compose(RxUtils.bindToLifecycle(view));
            }
        };
    }

    public static <T> ObservableTransformer<T,T> applySchedulers(final BaseView view,boolean showLoading) {
        return new ObservableTransformer<T, T>() {
            @Override
            public Observable<T> apply(Observable<T> observable) {
//                if(view.){}
                return observable.subscribeOn(Schedulers.io())
                        .doOnSubscribe(disposable -> {
                            Log.d(TAG, "apply: doOnSubscribe");
                            if (showLoading){

                                view.startProgressDialog();//显示进度条
                            }

                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnDispose(() -> {
                            Log.d(TAG, "apply: doOnDispose");
                            if (showLoading){

                                view.stopProgressDialog();//隐藏进度条
                            }
                        })
                        .compose(RxUtils.bindToLifecycle(view));
            }
        };
    }

    private static final String TAG = "RxUtils";

    /**
     * 没有載入进度条
     *
     * @param view
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> applySchedulers(final BaseView view) {
        return new ObservableTransformer<T, T>() {
            @Override
            public Observable<T> apply(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(RxUtils.bindToLifecycle(view));
            }
        };
    }

    public static <T> LifecycleTransformer<T> bindToLifecycle(BaseView view) {
        if (view instanceof LifecycleProvider) {
            return ((LifecycleProvider) view).bindToLifecycle();
        } else {
            throw new IllegalArgumentException("view isn't activity or fragment");
        }

    }


}
