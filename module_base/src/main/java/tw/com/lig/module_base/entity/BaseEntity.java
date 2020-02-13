package tw.com.lig.module_base.entity;

import com.google.gson.annotations.SerializedName;
import tw.com.lig.module_base.http.Api;

import java.io.Serializable;

/**
 * 作者：${weihaizhou} on 2018/1/27 15:47
 */
public class BaseEntity<T>  implements Serializable  {
    private int code;
//    private int Code;
    @SerializedName("msg")
    private String message;
    private T data;


    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    /**
     * 請求是否成功
     * code == 1
     *
     * @return
     */
    public boolean isSuccess() {
        return code == Api.RequestCodeSuccess;
    }
    public boolean isEmpty() {
        return code == Api.RequestCodeEmpty;
    }
    public boolean isTokenInvakid(){
        //220009
        return code==1006;//
    }
//    public boolean isLocked(){
//        return code==Api.IS_lOCKED;
//    }
    public boolean isAccountLocked() {
        return code == Api.ACCOUNT_LOCKED;
    }

    public void setData(T data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "BaseEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
