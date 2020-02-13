package tw.com.lig.module_base.utils;

import android.Manifest;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

//import com.orhanobut.logger.Logger;


public class PermissionUtil {


    private PermissionUtil() {
    }


    public interface RequestPermission {
        void onRequestPermissionSuccess();

        void onRequestPermissionFailure();
    }


    public static void requestPermission(final RequestPermission requestPermission, RxPermissions rxPermissions, String... permissions) {
        if (permissions == null || permissions.length == 0) return;

        List<String> needRequest = new ArrayList<>();
        for (String permission : permissions) { //过滤調已经申請过的权限
            if (!rxPermissions.isGranted(permission)) {
                needRequest.add(permission);
            }
        }

        if (needRequest.size() == 0) {//全部权限都已经申請过，直接执行操作
            requestPermission.onRequestPermissionSuccess();
        } else {//没有申請过,则开始申請
            String[] permission=new String[needRequest.size()];
            rxPermissions
                    .request(needRequest.toArray(permission))
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@NonNull Boolean aBoolean) throws Exception {
                            if (aBoolean) {
//                                Logger.e("Request permissons success"+permission[0]);
                                requestPermission.onRequestPermissionSuccess();
                            } else {
//                                Logger.e("Request permissons failure"+permission[0]);
                                // Permission Denied
                                requestPermission.onRequestPermissionFailure();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
//                            Logger.e(throwable.toString());
                        }
                    });
        }

    }


    /**
     * 請求摄像头权限
     */
    public static void launchCamera(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }
    public static void readPhoneState(RequestPermission requestPermission, RxPermissions rxPermissions){
        requestPermission(requestPermission, rxPermissions, Manifest.permission.READ_PHONE_STATE);

    }


    public static void recordAudio(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);
    }



    /**
     * 請求外部儲存的权限
     */
    public static void externalStorageand(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }



    public static void updateApk(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.GET_TASKS);
    }


    /**
     * 請求发送短信权限
     */
    public static void sendSms(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.SEND_SMS);
    }


    /**
     * 請求打電話权限
     */
    public static  void callPhone(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.CALL_PHONE);
    }

    /**
     * 請求讀取手機通讯錄权限
     *
     * @param requestPermission
     * @param rxPermissions
     */
    public static void readContacts(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.READ_CONTACTS);
    }


    /**
     * 請求獲取手機狀態的权限
     */
    public static void readPhonestate(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.READ_PHONE_STATE);
    }

    /**
     * 請求定位权限
     *
     * @param requestPermission
     * @param rxPermissions
     */
    public static void getLoacation(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermission(requestPermission, rxPermissions,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE);
    }



}

