package tw.com.lig.module_base.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;

import top.zibin.luban.Luban;

public class ImageCompressUtils {

    public static void clearCompressedPic(final Context context){
        //storage/emulated/0/Android/data/com.whyhow.xunguang/cache/luban_disk_cache/157302007021796.jpeg
    if(context.getExternalCacheDir().exists() && context.getExternalCacheDir().list().length>0){
        File[] files = context.getExternalCacheDir().listFiles();
        for(File s: files){
            if(s.getAbsolutePath().contains("luban_disk_cache")){
                Log.d("steven","clear compress pic:"+ s.getAbsolutePath());
                s.delete();
            }
        }
    }


    }

    public static void compress(final Context context, final File path, final OnCompressListener listener) {
//        File file = new File(Constant.compressImagePath);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Luban.with(context)
                        .load(path) // 傳入要壓縮的圖片列表
//                        .setTargetDir()
                        .ignoreBy(100) // 忽略不壓縮圖片的大小
//                        .setTargetDir(Constant.compressImagePath) // 設定壓縮後檔案儲存位置
                        .setCompressListener(new top.zibin.luban.OnCompressListener() { //設定回調

                            @Override
                            public void onStart() {
                                // TODO 壓縮開始前調用，可以在方法內啟動 loading UI
                                //ToastUtils.show(getContext(), "onStart");
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (listener != null) {
                                            listener.onStart();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onSuccess(final File file) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (listener != null) {
                                            listener.onSuccess(file);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onError(final Throwable e) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (listener != null) {
                                            listener.onError(e);
                                        }
                                    }
                                });
                            }
                        }).launch();    //啟動壓縮
            }
        }).start();
    }



   public interface OnCompressListener {

        void onStart();

        void onSuccess(File file);

        void onError(Throwable e);
    }

}
