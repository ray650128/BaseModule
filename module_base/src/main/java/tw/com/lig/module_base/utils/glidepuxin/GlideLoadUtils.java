package tw.com.lig.module_base.utils.glidepuxin;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import tw.com.lig.module_base.utils.TDevice;

import java.security.MessageDigest;

//import com.puxin.accountant.base.BaseFragment;
//import com.puxin.accountant.utils.glidepuxin.GlideApp;
//import com.puxin.accountant.utils.glidepuxin.GlideCircleTransform;

/**
 * Created by shangguan on 2018/3/15.
 */

public class GlideLoadUtils {
    private String TAG = "ImageLoader";


    /**
     * 借助内部類 實現執行緒安全的单例模式
     * 属于懒汉式单例，因為Java机制规定，内部類SingletonHolder只有在getInstance()
     * 方法第一次調用的時候才會被載入（實現了lazy），而且其載入过程是執行緒安全的。
     * 内部類載入的時候實例化一次instance。
     */
    public GlideLoadUtils() {
    }


    /**
     * 正常
     */
    public void load(Context context, String url, ImageView imageView,int error,int placeholder) {
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed())
                return;
        }
        RequestOptions options = new RequestOptions()
                .error(error)
                .placeholder(placeholder);

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }
    /**
     * 正常
     */
    public void load(Context context, String url, ImageView imageView) {
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed())
                return;
        }
//        RequestOptions options = new RequestOptions()
//                .error(error)
//                .placeholder(placeholder);

        Glide.with(context)
                .load(url)
//                .apply(options)
                .into(imageView);
    }


    public void loadCircle(Context context, String url, ImageView imageView){
        if (context != null && imageView != null) {
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                if (activity.isDestroyed()) {
                    return;
                }
            }
            RequestOptions  mRequestOptions = RequestOptions.circleCropTransform();
//        mRequestOptions.placeholder().error()
            Glide.with(context).load(url).apply(mRequestOptions).into(imageView);

        }

    }
    public void loadCircle(Context context, int url, ImageView imageView){
        if (context != null && imageView != null) {
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                if (activity.isDestroyed()) {
                    return;
                }
            }
            RequestOptions  mRequestOptions = RequestOptions.circleCropTransform();
//        mRequestOptions.placeholder().error()
            Glide.with(context).load(url).apply(mRequestOptions).into(imageView);

        }

    }


    private static class GlideLoadUtilsHolder {
        private final static GlideLoadUtils INSTANCE = new GlideLoadUtils();
    }

    public static GlideLoadUtils getInstance() {
        return GlideLoadUtilsHolder.INSTANCE;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void glideLoadRotate(Activity activity, String url, ImageView imageView) {
        if (activity!=null&&!activity.isDestroyed() && imageView != null) {
            GlideApp.with(activity).load(url).transform(new RotateTransformation(activity,90)).into(imageView);
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void glideLoadWithCorners(Context context, Transformation<Bitmap> transform, String url, ImageView imageView) {


        if (context != null && imageView != null) {
            if (context instanceof Activity ){
                Activity activity= (Activity) context;
                if (activity.isDestroyed()){
                    return;
                }
            }

            GlideApp.with(context)
                    .load(url)      //設定圖片路径(fix #8,文件名包含%符号 無法识别和显示)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//快取全尺寸
//                    .placeholder(defaultPic)
                    .transform(transform)
                    .into(imageView);
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void glideLoadDefault4Corners(Context context, int url, ImageView imageView,float radius) {
        RoundedCornersTransform transform = new RoundedCornersTransform(context, TDevice.dp2px(radius), true, true, true, true);

        if (context != null && imageView != null) {
            if (context instanceof Activity ){
                Activity activity= (Activity) context;
                if (activity.isDestroyed()){
                    return;
                }
            }

            GlideApp.with(context)
                    .load(url)      //設定圖片路径(fix #8,文件名包含%符号 無法识别和显示)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//快取全尺寸
//                    .placeholder(defaultPic)
                    .transform(transform)
                    .into(imageView);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void glideLoadDefault4Corners(Context context, String url, ImageView imageView,float radius) {
        RoundedCornersTransform transform = new RoundedCornersTransform(context, TDevice.dp2px(radius), true, true, true, true);

        if (context != null && imageView != null) {
            if (context instanceof Activity ){
                Activity activity= (Activity) context;
                if (activity.isDestroyed()){
                    return;
                }
            }

            GlideApp.with(context)
                    .load(url)      //設定圖片路径(fix #8,文件名包含%符号 無法识别和显示)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//快取全尺寸
//                    .placeholder(defaultPic)
                    .transform(transform)
                    .into(imageView);
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void glideLoadWithCorners(Context context, Transformation<Bitmap> transform, int url, ImageView imageView) {
        if (context != null && imageView != null) {
            if (context instanceof Activity ){
                Activity activity= (Activity) context;
                if (activity.isDestroyed()){
                    return;
                }
            }

            GlideApp.with(context)
                    .load(url)      //設定圖片路径(fix #8,文件名包含%符号 無法识别和显示)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//快取全尺寸
//                    .placeholder(defaultPic)
                    .transform(transform)
                    .into(imageView);
        }
    }

    public void glideLoadBackground(Context context, String url, final ViewGroup imageView) {
        if (context != null && null != imageView) {
            GlideApp.with(context).load(url).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        imageView.setBackground(resource);
                    }
                }
            });
        }
    }

    public class RotateTransformation extends BitmapTransformation {

        private float rotateRotationAngle = 0f;

        public RotateTransformation(Context context, float rotateRotationAngle) {
            this.rotateRotationAngle = rotateRotationAngle;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotateRotationAngle);
            return Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {

        }
    }

}
