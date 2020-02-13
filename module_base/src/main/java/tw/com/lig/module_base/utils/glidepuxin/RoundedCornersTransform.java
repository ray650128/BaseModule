package tw.com.lig.module_base.utils.glidepuxin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

import java.security.MessageDigest;

/**
 * @author csc
 * @date 2019-01-18
 * Todo 設定圖片部分圓角
 */
public class RoundedCornersTransform implements Transformation<Bitmap> {
    private BitmapPool mBitmapPool;

    private float radius;

    private boolean isLeftTop, isRightTop, isLeftBottom, isRightBotoom;

    /**
     * 需要設定圓角的部分
     *
     * @param leftTop     左上角
     * @param rightTop    右上角
     * @param leftBottom  左下角
     * @param rightBottom 右下角
     */
    public void setNeedCorner(boolean leftTop, boolean rightTop, boolean leftBottom, boolean rightBottom) {
        isLeftTop = leftTop;
        isRightTop = rightTop;
        isLeftBottom = leftBottom;
        isRightBotoom = rightBottom;
    }

    /**
     * @param context 上下文
     * @param radius  圓角幅度
     */
    public RoundedCornersTransform(Context context, float radius,boolean leftTop, boolean rightTop, boolean leftBottom, boolean rightBottom) {
        this.mBitmapPool = Glide.get(context).getBitmapPool();
        this.radius = radius;
        isLeftTop = leftTop;
        isRightTop = rightTop;
        isLeftBottom = leftBottom;
        isRightBotoom = rightBottom;
    }

    @NonNull
    @Override
    public Resource<Bitmap> transform(@NonNull Context context, @NonNull Resource<Bitmap> resource, int outWidth, int outHeight) {

        Bitmap source = resource.get();
        int finalWidth, finalHeight;
        //輸出目標的寬高或高寬比例
        float scale;
        if (outWidth > outHeight) {
            //如果 輸出寬度 > 輸出高度 求高寬比

            scale = (float) outHeight / (float) outWidth;
            finalWidth = source.getWidth();
            //固定原圖寬度,求最終高度
            finalHeight = (int) ((float) source.getWidth() * scale);
            if (finalHeight > source.getHeight()) {
                //如果 求出的最終高度 > 原圖高度 求寬高比

                scale = (float) outWidth / (float) outHeight;
                finalHeight = source.getHeight();
                //固定原圖高度,求最終寬度
                finalWidth = (int) ((float) source.getHeight() * scale);
            }
        } else if (outWidth < outHeight) {
            //如果 輸出寬度 < 輸出高度 求寬高比

            scale = (float) outWidth / (float) outHeight;
            finalHeight = source.getHeight();
            //固定原圖高度,求最終寬度
            finalWidth = (int) ((float) source.getHeight() * scale);
            if (finalWidth > source.getWidth()) {
                //如果 求出的最終寬度 > 原圖寬度 求高寬比

                scale = (float) outHeight / (float) outWidth;
                finalWidth = source.getWidth();
                finalHeight = (int) ((float) source.getWidth() * scale);
            }
        } else {
            //如果 輸出寬度=輸出高度
            finalHeight = source.getHeight();
            finalWidth = finalHeight;
        }

        //修正圓角
        this.radius *= (float) finalHeight / (float) outHeight;
        Bitmap outBitmap = this.mBitmapPool.get(finalWidth, finalHeight, Bitmap.Config.ARGB_8888);
        if (outBitmap == null) {
            outBitmap = Bitmap.createBitmap(finalWidth, finalHeight, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        //關聯畫筆繪製的原圖bitmap
        BitmapShader shader = new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //計算中心位置,進行偏移
        int width = (source.getWidth() - finalWidth) / 2;
        int height = (source.getHeight() - finalHeight) / 2;
        if (width != 0 || height != 0) {
            Matrix matrix = new Matrix();
            matrix.setTranslate((float) (-width), (float) (-height));
            shader.setLocalMatrix(matrix);
        }

        paint.setShader(shader);
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0.0F, 0.0F, (float) canvas.getWidth(), (float) canvas.getHeight());
        //先繪製圓角矩形
        canvas.drawRoundRect(rectF, this.radius, this.radius, paint);

        //左上角圓角
        if (!isLeftTop) {
            canvas.drawRect(0, 0, radius, radius, paint);
        }
        //右上角圓角
        if (!isRightTop) {
            canvas.drawRect(canvas.getWidth() - radius, 0, canvas.getWidth(), radius, paint);
        }
        //左下角圓角
        if (!isLeftBottom) {
            canvas.drawRect(0, canvas.getHeight() - radius, radius, canvas.getHeight(), paint);
        }
        //右下角圓角
        if (!isRightBotoom) {
            canvas.drawRect(canvas.getWidth() - radius, canvas.getHeight() - radius, canvas.getWidth(), canvas.getHeight(), paint);
        }

        return BitmapResource.obtain(outBitmap, this.mBitmapPool);
    }


    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
    }
}
