package tw.com.lig.module_base.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：${weihaizhou} on 2017/11/8 16:07
 */
public class WebParamBuilder implements Parcelable {

    private String url;
    private String title;
    private String orderId;
    private String periodsId;

    public static WebParamBuilder create() {
        return new WebParamBuilder();
    }

    public WebParamBuilder() {
    }

    public String getUrl() {
        return url;
    }

    public WebParamBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public WebParamBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public WebParamBuilder setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public WebParamBuilder setPeriodsId(String periodsId) {
        this.periodsId = periodsId;
        return this;
    }

    public String getPeriodsId() {
        return periodsId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(title);
        dest.writeString(orderId);
        dest.writeString(periodsId);
    }

    protected WebParamBuilder(Parcel in) {
        this.url = in.readString();
        this.title = in.readString();
        this.orderId = in.readString();
        this.periodsId = in.readString();
    }

    public static final Creator<WebParamBuilder> CREATOR = new Creator<WebParamBuilder>() {
        @Override
        public WebParamBuilder createFromParcel(Parcel in) {
            return new WebParamBuilder(in);
        }

        @Override
        public WebParamBuilder[] newArray(int size) {
            return new WebParamBuilder[size];
        }
    };
}
