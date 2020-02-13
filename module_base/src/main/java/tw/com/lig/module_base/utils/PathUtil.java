package tw.com.lig.module_base.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Author：steven gao on 2017/11/6.
 * Email：gaotaiwen201@163.com
 * Version：v1.0
 */


public class PathUtil {

//    public static void installApk(Context context, File file) {
//        //兼容8.0，不用代碼申請權限了，只在Manifests加上權限 REQUEST_INSTALL_PACKAGES即可
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setAction(Intent.ACTION_VIEW);
//        Uri uri;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            uri = VersionFileProvider.getUriForFile(context, context.getPackageName() + ".versionProvider", file);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        } else {
//            uri = Uri.fromFile(file);
//        }
//        intent.setDataAndType(uri, "application/vnd.android.package-archive");
//        context.startActivity(intent);
//    }

    public static String getCompressImagePath(Context ctx) {
        String appCachePath = null;
        if (checkSDCard() && ctx.getExternalCacheDir() != null) {
            appCachePath = ctx.getExternalCacheDir().getPath() + "/temp.jpg";
        } else {
            appCachePath = ctx.getCacheDir().getPath() + "/temp.jpg";
        }
        File file = new File(appCachePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return appCachePath;
    }

    public static String getCompressImagePath(Context ctx,String fileName) {
        String appCachePath = null;
        if (checkSDCard() && ctx.getExternalCacheDir() != null) {
            appCachePath = ctx.getExternalCacheDir().getPath() + File.separator + fileName ;
        } else {
            appCachePath = ctx.getCacheDir().getPath() + File.separator + fileName;
        }
        File file = new File(appCachePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return appCachePath;
    }

    public static String getDownloadPDFPath(Context ctx) {
        String appCachePath = null;
        if (checkSDCard() && ctx.getExternalCacheDir() != null) {//遇到這個路勁返回為空的情況
            appCachePath =  ctx.getExternalCacheDir().getPath() + "/pdfs/" ;
        } else {
            appCachePath = ctx.getCacheDir().getPath() + "/pdfs/" ;
        }
        File file = new File(appCachePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return appCachePath;
    }


//    Environment 常用方法：
//            * 方法：getDataDirectory()
//    解釋：返回 File ，獲取 Android 資料目錄。
//            * 方法：getDownloadCacheDirectory()
//    解釋：返回 File ，獲取 Android 下載/快取內容目錄。
//            * 方法：getExternalStorageDirectory()
//    解釋：返回 File ，獲取外部儲存目錄即 SDCard
//* 方法：getExternalStoragePublicDirectory(String type)
//    解釋：返回 File ，取一個高端的公用的外部儲存器目錄來擺放某些類型的檔案
//* 方法：getExternalStorageState()
//    解釋：返回 File ，獲取外部儲存設備的當前狀態
//* 方法：getRootDirectory()
//    解釋：返回 File ，獲取 Android 的根目錄

    public static String getDownloadApkCachePath() {
        String appCachePath = null;
        appCachePath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        return appCachePath;
    }

    /**
     *
     */
    public static boolean checkSDCard() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        return sdCardExist;
    }

    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }


}
