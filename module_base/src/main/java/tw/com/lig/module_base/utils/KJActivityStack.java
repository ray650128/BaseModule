package tw.com.lig.module_base.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import tw.com.lig.module_base.base.BaseActivity;

import java.util.Stack;

//import com.umeng.analytics.MobclickAgent;

/**
 * 應用程序Activity管理類：用于Activity管理和應用程序退出
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
     * 獲取当前Activity栈中元素個數
     */
    public int getCount() {
        return activityStack.size();
    }

    /**
     * 添加Activity到栈
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
     * 獲取当前Activity（栈顶Activity）
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
     * 獲取当前Activity（栈顶Activity） 没有找到则返回null
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
     * 结束当前Activity（栈顶Activity）
     */
    public void finishActivity() {
        BaseActivity activity = activityStack.lastElement();
        finishActivity(  activity);
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            if (activityStack != null) {
                activityStack.remove(activity);
            } else {
                Log.e("activityStack", "activityStack == null");
            }
            // mActivity.finish();//此处不用finish
            activity = null;
        }
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Class<?> cls) {
        for (BaseActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity( activity);
            }
        }
    }

    /**
     * 關闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
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
     * 结束所有Activity
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
            //如果开发者調用kill或者exit之類的方法杀死进程，請务必在此之前調用onKillProcess方法，用来保存統計資料。
//            MobclickAgent.onKillProcess(AppContext.getContext());
        } catch (Exception e) {
            Runtime.getRuntime().exit(-1);
            //如果开发者調用kill或者exit之類的方法杀死进程，請务必在此之前調用onKillProcess方法，用来保存統計資料。
//            MobclickAgent.onKillProcess(AppContext.getContext());

        }
    }
}