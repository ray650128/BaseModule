package tw.com.lig.module_base.utils


//import com.alibaba.android.arouter.launcher.ARouter
//import com.whyhow.module_base.base.BaseViewHolder
//import com.umeng.analytics.MobclickAgent
import android.app.Activity
import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import tw.com.lig.module_base.global.AppContext
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.toast


fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun ViewGroup.inflateToNull(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, null, false)
}

fun ImageView.loadUrl(url: String?) {
    Glide.with(context).load(url?.trim()).into(this)
}

//fun ImageView.loadUrlCornered(url: String?, radius: Int,placeholder: Int=R.drawable.hischool) {
//    Glide.with(context).load(url?.trim())
//            .apply(RequestOptions().centerCrop().placeholder(placeholder).transform(RoundCornersTransformation(context, radius, RoundCornersTransformation.CornerType.ALL)))
//
//            .into(this)
//}
//以後統一用这個方法，如果是RoundedImageView需要事先設定好RoundedImageView的参數

fun ImageView.loadUrlWithPlaceHolder(url: String?, defautImage: Int,isCenterCrop:Boolean=false) {
    var requestOptions1 = RequestOptions()
    if (isCenterCrop){
        requestOptions1=requestOptions1.centerCrop()
    }
    val requestOptions =  requestOptions1
//            .circleCrop()
            .placeholder(defautImage)
            .error(defautImage);

    Glide.with(context).load(url?.trim()).apply(requestOptions).into(this)
}

fun ImageView.loadUrlCircleWithDefault(url: String?, defautImage: Int) {
    Glide.with(context)
            /*       .load(url).placeholder(defautImage)
                   .transform( GlideCircleTransform(context))
                   .into(this)*/
            .asBitmap()
            .load(url?.trim())

            .apply(RequestOptions().centerCrop().placeholder(defautImage))
            .into(object : BitmapImageViewTarget(this) {
                @Override
                override fun setResource(resource: Bitmap?) {
                    val circularBitmapDrawable: RoundedBitmapDrawable? =
                            RoundedBitmapDrawableFactory.create(getResources(), resource)
                    circularBitmapDrawable?.setCircular(true)
                    setImageDrawable(circularBitmapDrawable)
                }
            })
}


fun ImageView.loadUrlCircleWithDefault2(url: String?) {
    Glide.with(context)
            /*       .load(url).placeholder(defautImage)
                   .transform( GlideCircleTransform(context))
                   .into(this)*/
//            .asBitmap()
            .load(url?.trim())

            .apply(RequestOptions()./*centerCrop().*//*placeholder(defautImage).*/format(DecodeFormat.PREFER_ARGB_8888).override(TDevice.getScreenWidth()))
            .into(this)
}

fun Context.getColor(colorId: Int): Int {
    return resources.getColor(colorId)
}

fun androidx.fragment.app.Fragment.getColor(colorId: Int): Int {
    return activity!!.resources.getColor(colorId)
}

fun androidx.fragment.app.Fragment.getDrawable(drawableId: Int): Drawable? {
    return activity?.resources?.getDrawable(drawableId)
}

fun Context.getDrawable(drawableId: Int): Drawable {
    return resources.getDrawable(drawableId)
}

fun TextView.checkBlank(message: String): String? {
    val text = this.text.toString()
    if (text.isBlank()) {
        toast(message)
        return null
    }
    return text
}

fun ArrayList<String>.removeLast() {
    removeAt(lastIndex)

}

inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

fun Activity.hideKeyboard(): Boolean {
    val view = currentFocus
    view?.let {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
    }
    return false
}


fun kError(tag: String, errorInfo: String) {
    Log.e(tag, errorInfo)
}
//fun androidx.recyclerview.widget.RecyclerView.lazyLoadImage(){
//    addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
//        override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView?, newState: Int) {
//            super.onScrollStateChanged(recyclerView, newState)
//            if (newState == androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE) {
//                Glide.with(context).resumeRequests()//恢复Glide載入圖片
//            } else {
//                Glide.with(context).pauseRequests()//禁止Glide載入圖片
//            }
//        }
//    })
//}

fun RecyclerView.vertical() {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
}

fun androidx.recyclerview.widget.RecyclerView.horizontal() {
    layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
}

fun androidx.recyclerview.widget.RecyclerView.grid(columns: Int) {
    layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, columns)
}

fun androidx.recyclerview.widget.RecyclerView.disableScroll() {
    isNestedScrollingEnabled = false

}

fun Activity.getKtColor(colorId: Int): Int {
    return resources.getColor(colorId)

}

fun Observable<Any>.IO(): Observable<Any> {
    return observeOn(Schedulers.io())
}

fun View.remove(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

inline fun View.removeIf(predicate: () -> Boolean): View {
    if (visibility != View.GONE && predicate()) {
        visibility = View.GONE
    }
    return this
}

fun View.hide(): View {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
    return this
}

inline fun View.hideIf(predicate: () -> Boolean): View {
    if (visibility != View.INVISIBLE && predicate()) {
        visibility = View.INVISIBLE
    }
    return this
}

fun View.show(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

inline fun View.showIf(condition: () -> Boolean): View {
    if (visibility != View.VISIBLE && condition()) {
        visibility = View.VISIBLE
    }
    return this
}

/**************************************************/

/**
 * Created by lvruheng on 2017/7/2.
 */
fun Context.showToast(message: String): Toast {
    var toast: Toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
    return toast
}

inline fun <reified T : Activity> Activity.newIntent() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}

fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

//fun ARouter.goTo(destination: String) {
//    ARouter.getInstance().build(destination).navigation()
//}

/**************************/
/*fun RecyclerView.Adapter<BaseViewHolder>.inflatesViewHolder(layoutId:Int):BaseViewHolder{

//    val parent = itemView.parent
//    parent as ViewGroup
    return BaseViewHolder(get.inflate(layoutId))
}*/


//abstract fun <T>Observable<BaseEntity<Any>>.subscribe(rxObserverFilter: RxObserverFilter<BaseEntity<Any>>)
/*fun Observable<BaseEntity<Any>>.IO():Observable<BaseEntity<Any>>{
    return observeOn(Schedulers.io())

}
fun Observable<BaseEntity<Any>>.Main():Observable<BaseEntity<Any>>{
    return observeOn(AndroidSchedulers.mainThread())

}*/


/*fun Observable<BaseEntity<Any>>.ktSubscribe(rxObserverFilter: RxObserverFilter<BaseEntity<Any>>){
    subscribe(
        {
            try {
                if (it == null) {
                    rxObserverFilter.handleError(Throwable("callback of data is null"))
                } else {
                    if (it is BaseEntity<*>) {
                        if ((it  as BaseEntity<*>).isTokenInvakid) {
                            if (rxObserverFilter.getmBaseView() != null) {
                                //                            Activity activity=(Activity)mBaseView;
                                //                            activity.finish();
                                //                            mBaseView.showErrorView();
                                SPutils.put(AppContext.getContext(), SPConstant.KEY_TOKEN, "")


                                *//*         ARouter.getInstance().build(ARouterConstant.VCODE_LOGIN)
                                    .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                    .withBoolean("isJumpToMain", true)     //传值 ，類似于putExtra
                                    .navigation();
                            new android.os.Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mBaseView.showToast("請先登錄");
                                    if(mBaseView instanceof BaseActivity){
                                        BaseActivity activity=(BaseActivity)mBaseView;
                                        if(activity.isFinishWhenTokenInvalid()){
                                            activity.finish();
                                        }else {
                                            activity.setFinishWhenTokenInvalid(true);
                                        }
                                    }else if(mBaseView instanceof Fragment){
                                        Fragment mFragment=(Fragment)mBaseView;
                                        FragmentActivity activity = mFragment.getActivity();
                                        if(activity!=null){
                                            activity.finish();
                                        }
                                    }

                                }
                            },500);*//*
                                rxObserverFilter.getmBaseView().showLoginExceptionDialog(rxObserverFilter.getmBaseView())
                                rxObserverFilter.getmBaseView().showErrorView()

                            } else {
                                if (BuildConfig.DEBUG && rxObserverFilter.getmBaseView() != null) {
                                    rxObserverFilter.getmBaseView().showToast("token失效，baseView為空")
                                }
                            }
                        } else if ((it as BaseEntity<*>).isAccountLocked) {//被冻结
                            SPutils.put(AppContext.getContext(), SPConstant.KEY_TOKEN, "")
                            if (rxObserverFilter.getmBaseView() != null) {
                                rxObserverFilter.getmBaseView().showAccountExceptionDialog()
                                rxObserverFilter.getmBaseView().showEmptyView()
                            }

                        } else if ((it as BaseEntity<*>).isSuccess) {

                            if (rxObserverFilter.getmBaseView() != null) {
                                rxObserverFilter.getmBaseView().showContentView()
                            } else {
                                //                            if(BuildConfig.DEBUG&&mBaseView!=null){
                                //                                mBaseView.showToast("baseView為空");
                                //                            }
                            }
                            rxObserverFilter.onSuccees(it)

                        } else {
                            val b = rxObserverFilter.onCodeError(it)

                            if (!b) {//若自己不处理就默认处理
                                rxObserverFilter.handleError(Throwable((it as BaseEntity<*>).message))
                            }
                        }
                    } else {
                        rxObserverFilter.handleError(Throwable("T must be BaseEntity"))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                rxObserverFilter.handleError(e)

            }


        }
        , {

    }

    )
}*/
fun TextView.toast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>.toast(message: String) {
    Toast.makeText(AppContext.getContext(), message, Toast.LENGTH_SHORT).show()
}

fun TextView.obtainText(): String {
    return text.toString().trim()
}

fun Dialog.toast(text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()


}

fun EditText.clear() {
    setText("")
}

fun EditText.fill(content: String) {
    text = Editable.Factory.getInstance().newEditable(content)

}

fun View.asStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        val height = TDevice.getStatusBarHeight(context)
        layoutParams.height = height.toInt()
    }

}

fun Activity.openBrowser() {
    var pm = getPackageManager();
    var intents = Intent(Intent.ACTION_VIEW);
    intents.addCategory(Intent.CATEGORY_BROWSABLE);
    intents.setData(Uri.parse("http://"));

    var list = pm.queryIntentActivities(intents, PackageManager.GET_INTENT_FILTERS);
//    print(list)
    val packageName = list.getOrNull(0)?.activityInfo?.packageName
    packageName?.let {

        try {
            val shortcutIntent = packageManager?.getLaunchIntentForPackage(it)
            startActivity(shortcutIntent)
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is java.lang.SecurityException) {

                try {
                    var startIntent = Intent()
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    var componentName: ComponentName? = null
                    val brand = Build.BRAND
//            LogUtils.e("launchIt zss brand===", brand);
                    if (brand.contains("nubia")) {
                        //适配努比亚打不开QQ音乐的問題，需要開啟自启动权限
                        componentName = ComponentName("cn.nubia.security2",
                                "cn.nubia.security.appmanage.selfstart.ui.SelfStartActivity");
                        startIntent.setComponent(componentName)
                        startActivity(startIntent)
                    } else if (brand.contains("vivo")) {
                        //适配vivo Xplay6半屏打不开QQ音乐的問題，需要開啟關联启动权限
                        componentName = ComponentName("com.vivo.appfilter",
                                "com.vivo.appfilter.activity.StartupManagerActivityRom30");
                        startIntent.setComponent(componentName)
                        startActivity(startIntent)
                    }else{
                        toast("打开失败，請聯繫客服")
                    }

                } catch (e1: Exception) {
                    e1.printStackTrace();
                }

            }

        }
    }

}

fun androidx.fragment.app.Fragment.openBrowser() {
    activity?.openBrowser()
}

fun androidx.fragment.app.Fragment.openPhone() {
    activity?.openPhone()
}

fun androidx.fragment.app.Fragment.openMarket() {
    activity?.openMarket()
}

fun Activity.openMarket() {
    val appMarketPackageNames = arrayOf(

            "com.xiaomi.market", //小米
            "com.huawei.appmarket", //华為
            "com.oppo.market", //oppo
            "com.meizu.mstore", //魅族
            "com.bbk.appstore", //vivo
            "com.sec.android.app.samsungapps", //三星
            "com.smartisan.appstore", //锤子
            "com.smartisanos.appstore",//坚果手機
            "com.oneplus.market", //一加手機
            "com.lenovo.leos.appstore", //联想應用商店
            "zte.com.market", //中兴
            "com.letv.app.appstore", //乐视
            "com.android.meitu.appstore",//美圖
            "com.gionee.aora.market",//金立
            "cn.nubia.neostore", //努比亚
            "com.baidu.appsearch", //百度手機助手
            "com.tencent.android.qqdownloader", //應用宝
            "com.qihoo.appstore", //360手機助手
            "com.sogou.appmall", //搜狗手機助手
            "com.wandoujia.phoenix2", //豌豆荚

            "com.dragon.android.pandaspace", //91手機助手
            "com.hiapk.marketpho", //安智應用商店
            "com.yingyonghui.market", //應用汇
            "com.pp.assistant", //pp手機助手
            "com.tencent.qqpimsecure")//qq手機管家
    appMarketPackageNames.first {

        isInstalled(this, it)
    }.let {
        try {
            val shortcutIntent = packageManager?.getLaunchIntentForPackage(it)
            startActivity(shortcutIntent)
        } catch (e: Exception) {
            e.printStackTrace()
            toast("打开失败，請聯繫客服")

        }

    }

}

fun isInstalled(context: Context, packageName: String): Boolean {
    var packageInfo: PackageInfo?
    try {
        packageInfo = context.packageManager.getPackageInfo(packageName, 0)
    } catch (e: PackageManager.NameNotFoundException) {
        packageInfo = null
        //                e.printStackTrace();
    }

    return if (packageInfo == null) {
        false
    } else {
        true
    }


}

fun Activity.openPhone() {

    var intents = Intent(Intent.ACTION_DIAL);
    intents.addCategory(Intent.CATEGORY_DEFAULT);
//                                                       intents.setData(Uri.parse("http://"));

    var list = packageManager?.queryIntentActivities(intents, PackageManager.GET_INTENT_FILTERS);
    val packageName = list?.getOrNull(0)?.activityInfo?.packageName
    packageName?.let {
        val shortcutIntent = packageManager?.getLaunchIntentForPackage(it)
        startActivity(shortcutIntent)
    }

}

fun TextView.setDrawableRight(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
}

val staticList = arrayListOf<Int>(1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
val staticList2 = listOf<String>(1.toString(), 1.toString(), "2", "3", "5", "6", "7", "8", "9", "10")

fun <T> select(isTrue: Boolean, param1: () -> T, param2: () -> T) = if (isTrue) param1() else param2()

//fun MobclickAgent.report( eventId:String){
//    if (BuildConfig.DEBUG){
//        MobclickAgent.onEvent(AppContext.getContext(),eventId)
//    }
//
//}
//對圖片进行压缩
fun Bitmap.scale(scaleRatio:Float):Bitmap{
    val matrix = Matrix()
    matrix.postScale(scaleRatio, scaleRatio) //长和宽放大缩小的比例
    return Bitmap.createBitmap(this, 0, 0, this.getWidth(), this.getHeight(), matrix, true)
}

fun TextView.strike(){
    paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
}


/**
 * scaleRatio默认压缩比例為1，即不进行压缩
 */
//fun TextView.setTextWithImageUrlLeft(text:String,imageUrl:String,scaleRatio:Float=1.0f){
//
//    Glide.with(context).asBitmap().load(imageUrl).into(object : SimpleTarget<Bitmap>() {
//        override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
//            val sp = SpannableString(" " + text)
//
//            val displayMetrics = resources.displayMetrics
//            val density = displayMetrics.density
//            var drawables:BitmapDrawable? = null
//            //獲取一张圖片
//            if (density==2.0f){
//                drawables = BitmapDrawable(resource)
//            }else{
//                drawables = BitmapDrawable(resource!!.scale(1.5f))
//            }
////            val drawable = BitmapDrawable(resource)
//
//            drawables.setBounds(0, 0, drawables.getMinimumWidth(), drawables.getMinimumHeight())
//            //居中對齐imageSpan
//            val imageSpan = MixtureTextView(drawables)
//            sp.setSpan(imageSpan, 0, 1, ImageSpan.ALIGN_BASELINE)
//            setText(sp)
//        }
//
//    })
//
//
//
//}

fun TextView.setDrawableLeft(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
}

fun TextView.shrink(content:String,shrinkCount:Int){
    val sb=StringBuilder()
    for (i in 0..shrinkCount-1){
        sb.append("缩")
    }

    val span = SpannableStringBuilder(sb.toString() + content)
    span.setSpan(ForegroundColorSpan(Color.TRANSPARENT), 0, shrinkCount, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    text=span
//    return  this.span
}




//fun io.reactivex.Observable.withLoading(baseView: BaseView){
//    return this.com
//
//}

/* fun <T: Any?> T .createIfNull():T {
     if (this==null){
         return T

     }*/

//    val intent = Intent(this, T::class.java)
//    startActivity(intent)
//}

/*fun View.setHeight(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.height = value
        layoutParams = lp
    }
}

fun View.setWidth(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.width = value
        layoutParams = lp
    }
}*/

fun View.setHeight(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.height = value
        layoutParams = lp
    }
}

fun View.setWidth(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.width = value
        layoutParams = lp
    }
}

fun Any.BothNotNull(a:Any?,b:Any?):Boolean{
    a?.let {
        b?.let {
            return true
        }
    }
    return false

}

fun Any.All3NotNull(a:Any?,b:Any?,c:Any?):Boolean{
    a?.let {
        b?.let {
            c?.let {

                return true
            }
        }
    }
    return false

}

fun Any?.isNotNull(ifNotNull:(Unit)->Unit){
    if (this!=null){
        ifNotNull.invoke(Unit)
    }
}
fun Any?.isNullOrNot(ifNull:(Unit)->Unit,ifNotNull:(Unit)->Unit){
    if (this==null){
        ifNull.invoke(Unit)
    }else{
        ifNotNull.invoke(Unit)

    }


}
/*fun TextView.setMaxLength(maxLength:Int){

}
fun ImageView.tint(  color:Int,drawable:Int){

    val icon =  getResources().getDrawable(drawable)
    val  tintIcon = DrawableCompat.wrap(icon as Drawable);
    val  csl=getResources().getColorStateList(color)
    DrawableCompat.setTintList(tintIcon,csl)
    setImageDrawable(tintIcon)
}*/







