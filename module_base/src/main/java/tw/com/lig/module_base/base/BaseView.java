package tw.com.lig.module_base.base;


import android.view.View;

/**
 * Created by ljk on 2017/6/28.
 * mvp 中 view的基類
 */

public interface BaseView {

    void startProgressDialog();

    void startProgressDialog(String loadMsg);

    void stopProgressDialog();

    void showToast(String message);

    void showErrorView();

    void showContentView();

    void showEmptyView();

      void showUserDefinedView(View view);

    void showToastCenter(String message);

    void showAccountExceptionDialog();

    void showLoginExceptionDialog(BaseView mBaseView);

    boolean hasBaseLayout();

    boolean isSmartRefreshLayoutEnabled();

    void showErrorView(String msg);

//    boolean isEventBusRegistered();






}
