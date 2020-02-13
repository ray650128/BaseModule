package tw.com.lig.module_base.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import tw.com.lig.module_base.R;


/**
 * Created by jpf on 2018/5/3.
 */

public abstract class BaseDialog extends Dialog {

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.NoneMdialog);
        setContentView(getContentView());
    }

    public BaseDialog(Context context, int noneMdialog) {
        super(context, noneMdialog);
        setContentView(getContentView());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!useCustomSetting()){
            WindowManager m = getWindow().getWindowManager();
            Display d = m.getDefaultDisplay();
            WindowManager.LayoutParams p = getWindow().getAttributes();
            p.width = d.getWidth();
            getWindow().setAttributes(p);
        }

    }
    protected boolean useCustomSetting(){
        return false;
    }

    /**
     * 設定佈局檔案
     * @return
     */
    public abstract int getContentView();

//    public abstract void User();
}
