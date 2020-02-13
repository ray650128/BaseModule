package tw.com.lig.module_base.base;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * mvp 中presenter的基類
 */
public abstract class BasePresenter<V extends BaseView, M> {

    protected CompositeDisposable compositeDisposable;

    protected V mView;
    protected M mModel;

    public BasePresenter(V view) {
        this.mView = view;
    }

    public BasePresenter(V view, M model) {
        this.mView = view;
        this.mModel = model;
    }

    protected void unDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    protected void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }


    public void onDestroy() {
        unDisposable();//解除订阅
        this.compositeDisposable = null;
        if (mView != null) {
            mView = null;
        }
        mView = null;

        if (mModel != null) {
            mModel = null;
        }
        mModel = null;
    }


/*    public  void initData(){
        if(mModel!=null&&mView!=null){
            mModel.initData()
                    .compose(RxUtils.applySchedulers(mView))//不显示进度条
                    .subscribe(new RxObserverFilter<BaseEntity>() {
                        @Override
                        protected void onSuccees(BaseEntity responseData) throws Exception {

                            Object data = responseData.getData();
                            if(data!=null ){
                                if(data instanceof List){
                                    List mList=(List)data;
                                    if(mList.isEmpty()){
                                        mView.showEmptyView();
                                        return;
                                    }
                                }
                            }
                            mView.onInitDataSuccess(responseData);
                            mView.showContentView();

                        }

                        @Override
                        protected void onFailure(Throwable e) throws Exception {
                            mView.showErrorView();
                        }
                    });
        }
    } ;*/
    public static Class<?> analysisClassInfo(Object object){
        Type genType=object.getClass().getGenericSuperclass();
        Type[] actualTypeArguments = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) actualTypeArguments[0];
    }

}
