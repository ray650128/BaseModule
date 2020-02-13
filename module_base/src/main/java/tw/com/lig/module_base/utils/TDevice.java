package tw.com.lig.module_base.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.ContextCompat;

import tw.com.lig.module_base.R;
import tw.com.lig.module_base.global.AppContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Method;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;


/**
 * 作者：${weihaizhou} on 2016/5/26 10:11
 */
public class TDevice {


    public static int getVirtualBarHeight(Context context) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - display.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }


    /**
     * 保持登錄按钮始终不會被覆盖
     *
     * @param root
     * @param subView
     * @param marginTop 輸入框與下面要露出的控件的距離
     *
     */
    public static void addLayoutListener(final View root, final View subView,int marginTop) {

        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                // 獲取root在窗体的可视區域
                root.getWindowVisibleDisplayFrame(rect);
                // 獲取root在窗体的不可视區域高度(被其他View遮挡的區域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                // 若不可视區域高度大于200，则鍵盤显示,其實相当于鍵盤的高度
                if (rootInvisibleHeight > 200) {
                    // 显示鍵盤時
                    int srollHeight = rootInvisibleHeight - (root.getBottom() - subView.getBottom()) - getNavigationBarHeight(root.getContext());

                    if (srollHeight > 0) {//当鍵盤高度覆盖按钮時
                        root.scrollTo(0, srollHeight + marginTop);
                    }
                } else {
                    // 隐藏鍵盤時
                    root.scrollTo(0, 0);
                }
            }
        });

    }


    /**
     * 判断是否有虚拟底部按钮
     *
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
//            Log.w(TAG, e);
        }
        return hasNavigationBar;
    }

    /**
     * 獲取底部虚拟按键高度
     *
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }


    /**
     * 設定導航條透明
     * @param activity
     * @param isTranslusent
     */

    public static  void translusentNavigationBar(Activity activity,boolean isTranslusent) {
        if (isTranslusent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setNavigationBarColor(Color.TRANSPARENT);
            }
        }
    }

    /**
     * 透明狀態列
     */
    public static  void translucentStatusBar(Activity activity,boolean isWhite) {

        WindowManager.LayoutParams lp =activity.getWindow().getAttributes();
        if (Build.VERSION.SDK_INT >= 28) {
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        activity.getWindow().setAttributes(lp);

        switchTheme(activity,isWhite);
    }
    /**
     * 設置狀態欄文字和圖標顏色
     * @param activity
     * @param isWhite
     */
    public static void switchTheme(Activity activity,boolean isWhite){
        if(isWhite){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0及以上
                View decorView = activity.getWindow().getDecorView();
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else  {//6.0以下
                View decorView = activity.getWindow().getDecorView();
                decorView.setFitsSystemWindows(true);
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

             /*   int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);*/
//                getWindow().setStatusBarColor(Color.TRANSPARENT);
                SystemBarTintManager tintManager = new SystemBarTintManager(activity);
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarTintColor(activity.getResources().getColor(R.color.semitransparent));


             /*   WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
                localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);*/
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0及以上
                int option= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//狀態列透明且黑色字体
                activity.getWindow().getDecorView().setSystemUiVisibility(option);
                activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
              /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0以上
                    option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//狀態列透明且黑色字体
                    getWindow().getDecorView().setSystemUiVisibility(option);
                    getWindow().setStatusBarColor(Color.TRANSPARENT);
                } else {
                    View decorView = getWindow().getDecorView();
                    decorView.setFitsSystemWindows(true);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

             *//*   int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);*//*
//                getWindow().setStatusBarColor(Color.TRANSPARENT);
                    SystemBarTintManager tintManager = new SystemBarTintManager(this);
                    tintManager.setStatusBarTintEnabled(true);
                    tintManager.setStatusBarTintColor(getResources().getColor(R.color.semitransparent));
                }*/

            } else {//6.0以下
                View decorView = activity.getWindow().getDecorView();
                decorView.setFitsSystemWindows(true);
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

             /*   int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);*/
//                getWindow().setStatusBarColor(Color.TRANSPARENT);
                SystemBarTintManager tintManager = new SystemBarTintManager(activity);
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarTintColor(activity.getResources().getColor(R.color.semitransparent));

            }
        }

    }

    /**
     * 獲取應用程序名称
     */
    public static synchronized String getAppName() {
        try {
            Context context=AppContext.getContext();
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断是否有網路
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    public static int getScreenWidth(){
        WindowManager wm = (WindowManager)  AppContext.getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        return width;

    }
    public static int getScreenHeight(){
        WindowManager wm = (WindowManager)  AppContext.getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        int height = wm.getDefaultDisplay().getHeight();
        return height;

    }
    /**
     * Get status bar height.
     *
     * @return
     */
    public static final float getStatusBarHeight(Context context) {
        int height = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimension(height);

    }
    public static final float getStatusBarHeight() {
        int height = AppContext.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        return AppContext.getContext().getResources().getDimension(height);

    }



    public static void hideSoftKeyboard(View view) {
        if (view == null) return;
        View mFocusView = view;

        Context context = view.getContext();
        if (context != null && context instanceof Activity) {
            Activity activity = ((Activity) context);
            mFocusView = activity.getCurrentFocus();
        }
        if (mFocusView == null) return;
        mFocusView.clearFocus();
        InputMethodManager manager = (InputMethodManager) mFocusView.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(mFocusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showSoftKeyboard(View view) {
        if (view == null) return;
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        if (!view.isFocused()) view.requestFocus();

        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, 0);
    }

    public static int getVersionCode() {
        int versionCode;
        try {
            versionCode = AppContext.getContext().getPackageManager()
                    .getPackageInfo(AppContext.getContext().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
            versionCode = 0;
        }
        return versionCode;
    }

    /*
     * 獲取当前程序的版本号
     */
    public static String getVersionName() {
        //獲取packagemanager的實例
        PackageManager packageManager = AppContext.getContext().getPackageManager();
        //getPackageName()是你当前類的包名，0代表是獲取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(AppContext.getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packInfo != null) {
            return packInfo.versionName;
        } else return "1.0.1";
    }

    /**
     * 手機系統版本
     *
     * @return
     */
    public static String getPhoneVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 獲得手機udid
     *
     * @return
     */
    public static String getUDid() {
        try{
            TelephonyManager tm = (TelephonyManager) AppContext.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";



//        try {
//            TelephonyManager tm = (TelephonyManager) getApplication().getSystemService(TELEPHONY_SERVICE);
//            Log.e("Ap", "IMEI=======" + tm.getDeviceId());
//            return tm.getDeviceId();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
    }

    /**
     * 獲得手機imei
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI() {

        try {
            TelephonyManager tm = (TelephonyManager) AppContext.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 字体設定不同的颜色
     *
     * @param text
     * @param settingText
     */
    public static SpannableStringBuilder settingTextColor(Context context, String text, String settingText, int colorResources) {

        SpannableStringBuilder style = new SpannableStringBuilder(text);
        int index = text.indexOf(settingText);
        if (index != -1) {
            int color = ContextCompat.getColor(context, colorResources);
            style.setSpan(new ForegroundColorSpan(color), index, index + settingText.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return style;
    }


    public static String getPhoneInfo() {
        return Build.MODEL; // 手機型号
    }

    /**
     * <p>
     * Title: getAndroidSDKVersion
     * </p>
     * <p>
     * Description: 獲取当前系統的AndroidSDK版本
     * </p>
     * <p>
     * </p>
     *
     * @return
     * @author zhangqy
     * @date 2015年7月10日 下午5:12:30
     */
    private static int androidSdkVersion = -1;

    public static int getAndroidSDKVersion(Context context) {
        try {
            if (androidSdkVersion == -1) {
                androidSdkVersion = Build.VERSION.SDK_INT;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return androidSdkVersion;
    }

    /**
     * <p>
     * Title: getPackageName
     * </p>
     * <p>
     * Description: 取得apk的包名
     * </p>
     * <p>
     * <p>
     * </p>
     *
     * @param context
     * @return
     * @author xwc1125
     * @date 2015-7-1上午10:53:00
     */
    private static int targetSdkVersion = -1;

    public static int getTargetSdkVersion(Context context) {
        if (context == null) {
            return targetSdkVersion;
        }
        if (targetSdkVersion == -1) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                if (packageInfo != null) {
                    targetSdkVersion = packageInfo.applicationInfo.targetSdkVersion;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return targetSdkVersion;
    }


    public static float dp2px(float dp) {
        return dp * getDisplayMetrics().density;
    }

    public static float px2dp(float px) {
        return px / getDisplayMetrics().density;
    }

    public static DisplayMetrics getDisplayMetrics() {
        return AppContext.getContext().getResources().getDisplayMetrics();
    }

    /**
     * 验证手機格式
     */
    public static boolean isMobileNumber(String mobiles) {

        String telRegex = "[1][34578]\\d{9}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }


    public static boolean hasInternet() {
        boolean flag;
        flag = ((ConnectivityManager) AppContext.getContext().getSystemService(
                Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
        return flag;
    }


    /**
     * 獲取手機型号
     *
     * @return 手機型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 獲取手機厂商
     *
     * @return 手機厂商
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * 獲取当前手機系統版本号
     *
     * @return 系統版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }


    public static String getNames(Context context, int type) {
        String resultString = "";
        try {
            InputStream inputStream = context.getResources().getAssets().open("img.json");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            resultString = new String(buffer, "utf-8");
        } catch (Exception e) {
            resultString = null;
        }
        if (resultString != null) {
            try {
                JSONArray array = new JSONArray(resultString);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    if (item.get("code").equals(type + "")) {
                        return (String) item.get("name");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }


    private static Boolean isDebug = null;

    public static Boolean isDebug() {
        return isDebug == null ? false : isDebug.booleanValue();
    }

    /**
     * 判断是否是 Debug 版本
     * <p>
     * Debug 包值為 false，Release 包值為 true，这是编译自动修改的
     *
     * @param context
     */
    @SuppressLint("NewApi")
    public static void syncIsDebug(Context context) {
        if (isDebug == null) {
            isDebug = context.getApplicationInfo() != null && (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        }
    }
    public static void openCertainAppMarket(Context context, String availableMarketName){
        try {
//            String packageName = context.getPackageName();//獲取我们的app的包名
            Uri uri = Uri.parse("market://details?id=" + availableMarketName);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setPackage(availableMarketName);  //指定某個市场
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
