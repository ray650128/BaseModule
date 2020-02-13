package tw.com.lig.module_base.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import tw.com.lig.module_base.base.BaseActivity;

import java.util.Stack;

//import com.umeng.analytics.MobclickAgent;

/**
 * 應用程序Activity管理類：用於Activity管理和應用程序退出
 */
final public class KJActivityStack {
    private static Stack<BaseActivity> activityStack;
    private static final KJActivityStack instance = new KJActivityStack();

    private KJActivityStack() {
    }

    public static KJActivityStack create() {
        return instance;
    }

    /**
     * 獲取當前Activity棧中元素個數
     */
    public int getCount() {
        return activityStack.size();
    }

    /**
     * 添加Activity到棧
     */
    public void addActivity(BaseActivity activity) {
        if (activityStack == null) {
            activityStack = new Stack<BaseActivity>();
        }
        activityStack.add(activity);
//        for (Activity activity1 : activityStack) {
//            Logger.e(activity1.getClass().toString());
//        }
    }

    /**
     * 獲取當前Activity（棧頂Activity）
     */
    public Activity topActivity() {
        if (activityStack == null) {
            throw new NullPointerException(
                    "Activity stack is Null,your Activity must extend KJActivity");
        }
        if (activityStack.isEmpty()) {
            return null;
        }
        BaseActivity activity = activityStack.lastElement();
        return   activity;
    }

    /**
     * 獲取當前Activity（棧頂Activity） 沒有找到則返回null
     */
    public Activity findActivity(Class<?> cls) {
        BaseActivity activity = null;
        for (BaseActivity aty : activityStack) {
            if (aty.getClass().equals(cls)) {
                activity = aty;
                break;
            }
        }
        return   activity;
    }

    /**
     * 結束當前Activity（棧頂Activity）
     */
    public void finishActivity() {
        BaseActivity activity = activityStack.lastElement();
        finishActivity(  activity);
    }

    /**
     * 結束指定的Activity(重載)
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            if (activityStack != null) {
                activityStack.remove(activity);
            } else {
                Log.e("activityStack", "activityStack == null");
            }
            // mActivity.finish();//此處不用finish
            activity = null;
        }
    }

    /**
     * 結束指定的Activity(重載)
     */
    public void finishActivity(Class<?> cls) {
        for (BaseActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity( activity);
            }
        }
    }

    /**
     * 關閉除了指定activity以外的全部activity 如果cls不存在於棧中，則棧全部清空
     *
     * @param cls
     */
    public void finishOthersActivity(Class<?> cls) {
        for (BaseActivity activity : activityStack) {
            if (!activity.getClass().equals(cls)) {
                activityStack.remove(activity);
            }

        }
    }

    /**
     * 結束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                (  activityStack.get(i)).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 應用程序退出
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            Runtime.getRuntime().exit(0);
            //如果開發者調用kill或者exit之類的方法殺死進程，請務必在此之前調用onKillProcess方法，用來保存統計資料。
//            MobclickAgent.onKillProcess(AppContext.getContext());
        } catch (Exception e) {
            Runtime.getRuntime().exit(-1);
            //如果開發者調用kill或者exit之類的方法殺死進程，請務必在此之前調用onKillProcess方法，用來保存統計資料。
//            MobclickAgent.onKillProcess(AppContext.getContext());

        }
    }
}