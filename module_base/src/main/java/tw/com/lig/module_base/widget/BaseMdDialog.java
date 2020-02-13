package tw.com.lig.module_base.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import tw.com.lig.module_base.R;


/**
 * Created by jpf on 2018/5/3.
 */

public abstract class BaseMdDialog extends Dialog {

    public BaseMdDialog(@NonNull Context context) {
        super(context, Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP? R.style.Mdialog:R.style.NoneMdialog);
        setContentView(getContentView());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);
    }

    /**
     * 設定布局文件
     * @return
     */
    public abstract int getContentView();
}
