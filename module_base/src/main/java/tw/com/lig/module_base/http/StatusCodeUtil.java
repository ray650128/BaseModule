package tw.com.lig.module_base.http;


import tw.com.lig.module_base.entity.BaseEntity;
import tw.com.lig.module_base.utils.GsonUtils;

import java.io.IOException;

import retrofit2.HttpException;
import retrofit2.Response;

public class StatusCodeUtil {
    public static String convertStatusCode(HttpException httpException) {
        String msg=null;
        if (httpException.code() == 500) {
            msg = "操作频繁";
        } else if (httpException.code() == 504) {
            msg = "連接異常，請檢查網路";
        } else if (httpException.code() == 404) {
            msg = "請求地址不存在";
        } else if (httpException.code() == 403) {
            msg = "請求被伺服器拒绝";
        } else if (httpException.code() == 307) {
            msg = "請求被重定向到其他頁面";
        } else if(httpException.code()==400){
            Response<BaseEntity> response = (Response<BaseEntity>) httpException.response();
            try {
                String mss= response.errorBody().string();

//                System.out.println(mss);
                BaseEntity baseEntity= GsonUtils.fromJson2(mss,BaseEntity.class);
                msg=baseEntity.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                msg= httpException.message();
            }
//            BaseEntity baseEntity= GsonUtils.fromJson2(responseBody,BaseEntity.class);
//            msg= "";

        }else {

            msg = httpException.message();
        }
        return msg;
    }
}
