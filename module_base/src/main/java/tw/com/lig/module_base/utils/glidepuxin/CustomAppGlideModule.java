package tw.com.lig.module_base.utils.glidepuxin;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by shangguan on 2017/9/14.
 */

@GlideModule
public final class CustomAppGlideModule extends AppGlideModule {

    /**
     *  通過GlideBuilder設定預設的結構(Engine,BitmapPool ,ArrayPool,MemoryCache等等).
     * @param context
     * @param builder
     */
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //重新設定內存限制
        builder.setMemoryCache(new LruResourceCache(10*1024*1024));
    }

    /**
     * 清單解析的開啟
     *
     * 這裡不開啟，避免添加相同的modules兩次
     * @return
     */
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}