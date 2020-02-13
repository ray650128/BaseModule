package tw.com.lig.module_base.utils.toast;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by shangguan on 2017/10/17.
 */

public class ToastUtils {

    private ToastUtils() {
    }

    public static void showLongToast(Context context, CharSequence msg) {
//        showToast(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//        Toasty.normalLong(context,msg).show();
    }

    public static void showLongToast(Context context, int resId) {
//        showToast(context.getApplicationContext(), resId, Toast.LENGTH_LONG);
        Toast.makeText(context.getApplicationContext(), resId, Toast.LENGTH_LONG).show();
//        Toasty.normalLong(context,resId).show();
    }

    public static void showToast(Context context, int resId) {
        Toast.makeText(context.getApplicationContext(), resId, Toast.LENGTH_LONG).show();
//        Toasty.normalLong(context,resId).show();
    }
    public static void showToast(Context context, String resId) {
        Toast.makeText(context.getApplicationContext(), resId, Toast.LENGTH_LONG).show();
//        Toasty.normalLong(context,resId).show();
    }

}
