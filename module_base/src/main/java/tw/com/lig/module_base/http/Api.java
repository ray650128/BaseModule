package tw.com.lig.module_base.http;

/**
 * 作者：${weihaizhou} on 2018/1/27 16:56
 */
public interface Api {
//    String API_BASE_URL = "http://api.anxin.com.cn/";

    //請求成功
    int RequestCodeSuccess = 1000;

    //請求成功但是沒有資料
    int RequestCodeEmpty = -1;

    //賬戶被凍結
    int IS_lOCKED= -5;

    //atoken 失效(異地登錄)
    int ATOKEN_INVALID = 220007;

    //用戶鎖定
    int ACCOUNT_LOCKED = -5;

}
