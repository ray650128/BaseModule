package tw.com.lig.module_base.widget;

import android.content.Context;

import androidx.annotation.NonNull;

import tw.com.lig.module_base.R;

public class BaseNoDimDialog extends BaseDialog {
    public BaseNoDimDialog(@NonNull Context context) {
//        super(context);
        super(context, R.style.Mdialog);
    }

    @Override
    public int getContentView() {
        return 0;
    }
}
