package tw.com.lig.module_base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import tw.com.lig.module_base.global.AppContext;

import java.util.List;

//import com.jihu.jihu.Service.CheckNewMSGService;
//import com.jihu.jihu.application.App;
//import com.jihu.jihu.application.Constants;
//import com.jihu.jihu.ui.activity.MainActivity;
//import com.jihu.jihu.ui.activity.WelcomActivity;
//import com.jihu.jihu.ui.login.LoginActivity;
//import com.jihu.jihu.ui.login.StartActivity;


public class UIUtils {
//	public static StartActivity startActivity = null;
//	public static WelcomActivity welcomActivity = null;
	/*start模拟机*/

	public static String exeBtn = null;

	/**
	 * 定义环形插补器默认参數
	 */
	private final static int DEFAULT_SHAKE_DURATION = 500;
	private final static float DEFAULT_SHAKE_CYCLE = 4.0f;

	public static String imgSavePath = Environment.getExternalStorageDirectory().getPath() + "/easymoney/";
//    public static MainActivity mainActivity = null;
	/*start yucheng 2018.2.19 從AP移过来*/

	public static Context getContext() {
		return AppContext.getContext();
	}

//	public static Handler getHandler() {
//		return App.getHandler();
//	}
//
//	public static int getMainThreadId() {
//		return App.getMainThreadId();
//	}

	// /////////////////載入资源文件 ///////////////////////////

	// 獲取字串
	public static String getString(int id) {
		return getContext().getResources().getString(id);
	}

	// 獲取字串陣列
	public static String[] getStringArray(int id) {
		return getContext().getResources().getStringArray(id);
	}

	// 獲取圖片
	public static Drawable getDrawable(int id) {
		return getContext().getResources().getDrawable(id);
	}

	// 獲取颜色
	public static int getColor(int id) {
		return getContext().getResources().getColor(id);
	}



	// /////////////////dip和px轉換//////////////////////////

	public static int dip2px(float dip) {
		float density = getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * density + 0.5f);
	}

	public static float px2dip(int px) {
		float density = getContext().getResources().getDisplayMetrics().density;
		return px / density;
	}

	// /////////////////載入布局文件//////////////////////////
	public static View inflate(int id) {
		return View.inflate(getContext(), id, null);
	}

	// /////////////////判断是否运行在主執行緒//////////////////////////
//	public static boolean isRunOnUIThread() {
//		// 獲取当前執行緒id, 如果当前執行緒id和主執行緒id相同, 那么当前就是主執行緒
//		int myTid = android.os.Process.myTid();
//		if (myTid == getMainThreadId()) {
//			return true;
//		}
//
//		return false;
//	}
//
//	// 运行在主執行緒
//	public static void runOnUIThread(Runnable r) {
//		if (isRunOnUIThread()) {
//			// 已经是主執行緒, 直接运行
//			r.run();
//		} else {
//			// 如果是子執行緒, 借助handler让其运行在主執行緒
//			getHandler().post(r);
//		}
//	}

	////////////////////////显示toast的utils/////////////////
	public static void showToast(String showContent){
		Toast.makeText(getContext(),showContent, Toast.LENGTH_SHORT).show();
	}





	//start liuwangping  轉換成後台要的版本号類型
	public static String getVersionNameZhuanhoutai(String versionName) {
		String[] split = versionName.split("\\.");
		StringBuffer buffer = new StringBuffer();
		for (int x = 0; x < split.length; x++)
			if (x == 0)
				buffer.append(split[x]);
			else if (split[x].length() == 1)
				buffer.append("0" + split[x]);
			else
				buffer.append(split[x]);
		String s = buffer.toString();
		return buffer.toString();
	}
	//end liuwangping  轉換成後台要的版本号類型
	/*start liuwangping  獲取本地VersionName*/
	public static String getStringVersionName() {
		PackageInfo pi = null;
		try {
			PackageManager pm = getContext().getPackageManager();
			pi=  pm.getPackageInfo(getContext().getPackageName(), 0);

		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return pi.versionName;
	}


	/*end liuwangping  獲取本地VersionName*/

	public static String getPackageVersionName() {
		//1.PackageManager 包的管理者對象
		PackageManager pm = getContext().getPackageManager();
		//2.獲取應用的配置信息,在此处传递0獲取的是基本信息(包名,版本名称,版本号)
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getContext().getPackageName(),0);
			String versionName = packageInfo.versionName;
			return versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getQianLiangWeiPackageVersionName(){
		String versionName = getPackageVersionName();
		return /*versionName.substring(0,3)*/versionName;
	}


	/**
	 * 檢查是否有網路連接
	 *
	 * @return
	 */
//	public static boolean isNetworkConnected() {
//		if (getContext() != null) {
//			ConnectivityManager mConnectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
//			if (mNetworkInfo != null) {
//				return mNetworkInfo.isAvailable();
//			}
//		}
//		return false;
//	}

	public static byte[] hexToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] bytes = new byte[length];
		String hexDigits = "0123456789abcdef";
		for (int i = 0; i < length; i++) {
			int pos = i * 2; // 兩個字元對應一個byte
			int h = hexDigits.indexOf(hexChars[pos]) << 4; // 注1
			int l = hexDigits.indexOf(hexChars[pos + 1]); // 注2
			if (h == -1 || l == -1) { // 非16进制字元
				return null;
			}
			bytes[i] = (byte) (h | l);
		}
		return bytes;
	}

/*
	//登錄信息失效
	public static void userKicked() {

		try {

			Intent intent = new Intent(getContext(), CheckNewMSGService.class);
			SharePreferenceUtils.putBoolean(UIUtils.getContext(), Constants.TUICHU,true);//判断是否登錄true為已登錄，falsh為未登錄
			getContext().stopService(intent);
			*/
/*start yucheng 登陆信息失效先注释2018.2.19*//*

			*/
/*AppPreferences.eraseUser();

			MyActivityManager mam = MyActivityManager.getInstance();
			mam.finishAllActivity();

*//*

			*/
/*end yucheng 登陆信息失效先注释2018.2.19*//*

//			UserToast.toSetToast(getContext(),"您的账号在其他地方登錄，請注意账号安全");
//			getContext().startActivity(new Intent(getContext(), LoginActivity.class).putExtra("isKick", true).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/

	/**
	 * 打开網路連接設定
	 *
	 * @param activity
	 */
	public static void openNetworkConfig(Activity activity) {

		if (Build.VERSION.SDK_INT > 13) {
			//3.2以上打开設定界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
			activity.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
		} else {
			activity.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
		}
	}

	/**
	 * 开始环形插补動畫
	 *
	 * @param view
	 */
	public static void startShake(final View view) {
		TranslateAnimation animation = makeTranslate(0, 10, 0, 0, DEFAULT_SHAKE_DURATION);
		animation.setInterpolator(new CycleInterpolator(DEFAULT_SHAKE_CYCLE));
		view.startAnimation(animation);
	}

	/**
	 * 旋轉動畫
	 *
	 * @param fromX
	 * @param toX
	 * @param fromY
	 * @param toY
	 * @param duration
	 * @return
	 */
	private static TranslateAnimation makeTranslate(float fromX, float toX, float fromY, float toY, int duration) {
		TranslateAnimation animation = new TranslateAnimation(fromX, toX, fromY, toY);
		animation.setDuration(duration);
		return animation;
	}


	public static String formatDuring(long mss) {
		String day = null;
		String hour = null;
		String minute = null;
		String second = null;
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;
		if (days <= 0){
			day = "";
			if (hours <= 0){
				hour = "";
				if (minutes <= 0){
					minute = "";
					if (seconds <= 0){
						second = "0秒";
					}else {
						second = seconds + " 秒 ";
					}
				}else {
					minute = minutes + " 分钟 ";
					second = seconds + " 秒 ";
				}
			}else {
				hour = hours + " 小時 ";
				minute = minutes + " 分钟 ";
				second = seconds + " 秒 ";
			}
		}else {
			day = days + " 天 ";
			hour = hours + " 小時 ";
			minute = minutes + " 分钟 ";
			second = seconds + " 秒 ";

		}



		return day + hour + minute
				+ second;
	}


//	public static boolean CheckMoNiJi(){
//		Class mclass = null;
//		try {
//
//			mclass = Class.forName("android.os.SystemProperties");
//
//			invoker = mclass.newInstance();
//
//			Method mmethod = mclass.getMethod("get", new Class[] { String.class,String.class });
//
//			result = mmethod.invoke(invoker, new Object[] {"gsm.version.baseband", "no message" });
//			//no Message,模拟机
//			if (result.equals("no message")||CheckMNJUtil.notHasBlueTooth()||CheckMNJUtil.notHasLightSensorManager(getContext())
//					||CheckMNJUtil.isFeatures()||CheckMNJUtil.checkIsNotRealPhone()){
//				isMONIJI=true;
//				SharePreferenceUtils.putBoolean(UIUtils.getContext(), Constants.MONIJI,isMONIJI);
//				return true;
//			}else {
//				isMONIJI=false;
//				SharePreferenceUtils.putBoolean(UIUtils.getContext(), Constants.MONIJI,isMONIJI);
//			}
//			Log.e("基帶版本:", (String) result);
//
//		} catch (Exception e) {
//		}
//		return false;
//	}
	/**
	 * 利用反射獲取狀態列高度
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		//獲取狀態列高度的资源id
		int resourceId =context. getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}


		public static boolean isAndroidVersion (Activity activity){
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT || Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//4.4 全透明狀態列
				return true;
			}else {
				return false;
			}

		}

		////////////////////判断是否安装微信/////////
		public static boolean isWeixinAvilible(Context context) {
			final PackageManager packageManager = context.getPackageManager();// 獲取packagemanager
			List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 獲取所有已安装程序的包信息
			if (pinfo != null) {
				for (int i = 0; i < pinfo.size(); i++) {
					String pn = pinfo.get(i).packageName;
					if (pn.equals("com.tencent.mm")) {
						return true;
					}
				}
			}

			return false;
		}

	/**
	 * 判断qq是否可用
	 *
	 * @param context
	 * @return
	 */
	public static boolean isQQClientAvailable(Context context) {
		final PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				if (pn.equals("com.tencent.mobileqq")) {
					return true;
				}
			}
		}
		return false;
	}

	public static void setShouqiRuanjianpan(){
		InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isOpen=imm.isActive();
		if (isOpen){
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public static void setQidongRuanjianpan(){
		InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}


//	public static void requestNetWorkErro(Response response){
//		if (response.code() == 500){
//			showToast(Constants.REQUESTERREMESSAGE);
//		}
//	}

	public static void showToastShenfenGuoQi(){
		/*getHandler().removeCallbacksAndMessages(0);
		getHandler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getContext(),"身份已过期，請重新登錄",Toast.LENGTH_SHORT).show();
			}
		},1000);*/

	}





}
