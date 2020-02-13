package tw.com.lig.module_base.base;

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
//        if (isBackgroudDim()){
//            super(context, R.style.NoneMdialog);
//        }else{
//            super(context, R.style.Mdialog);
//
//        }

        setContentView(getContentView());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     /*   LayoutInflaterCompat.setFactory2(getLayoutInflater(), new LayoutInflater.Factory2() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                return null;
            }

            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                return null;
            }
        });*/

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);
    }

    /**
     * 設定佈局檔案
     * @return
     */
    public abstract int getContentView();
//    protected boolean isBackgroudDim(){
//        return true;
//    }
}
