package tw.com.lig.module_base.entity;

import tw.com.lig.module_base.http.Api;

import java.io.Serializable;

/**
 * 作者：${weihaizhou} on 2018/1/27 15:47
 */
public class BaseEntityBig<T> implements Serializable {
    private int Code;
    private String Message;
    private T Data;

    public void setCode(int code) {
        this.Code = code;
    }

    public int getCode() {
        return Code;
    }

    public String getMessage() {
        return Message;
    }

    public T getData() {
        return Data;
    }

    /**
     * 請求是否成功
     * code == 1
     *
     * @return
     */
    public boolean isSuccess() {
        return Code == Api.RequestCodeSuccess;
    }

}
