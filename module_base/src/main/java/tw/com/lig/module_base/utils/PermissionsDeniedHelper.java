package tw.com.lig.module_base.utils;

import android.app.Activity;
import android.content.Context;

import tw.com.lig.module_base.R;


/**
 * 作者：${weihaizhou} on 2017/8/30 13:54
 */
public class PermissionsDeniedHelper {

    private Context mContext;
    private TextDialog textDialog;
    private String title = "權限申請";
    private String message;
    private boolean isCustomTip;//自定義提示語

    public PermissionsDeniedHelper(Builder builder) {
        this.mContext = builder.mContext;
        this.title = builder.title;
        this.message = builder.message;
        this.isCustomTip=builder.isCustomTip;

        if (textDialog == null) {
            textDialog = new TextDialog(mContext);
        }
        textDialog.setTitle(title);
        if(!isCustomTip){
            textDialog.setMessage("請在設定-應用-" + mContext.getString(R.string.app_name) + "-權限中開啟"+message+"權限。");
        }else{
            textDialog.setMessage(message);
        }
        textDialog.setYesOnclickListener("去設定", new TextDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                LaunchPermissionSetting.GoToSetting((Activity) mContext);
                textDialog.dismiss();
            }
        });

        textDialog.setNoOnclickListener("取消", new TextDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                textDialog.dismiss();
            }
        });
        textDialog.show();
    }

    public static class Builder {

        private Context mContext;
        private String title;
        private String message;
        private boolean isCustomTip;

        public Builder(Context mContext) {
            this.mContext = mContext;

        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String msg) {
            this.message = msg;
            return this;
        }
        public Builder customTip(){
            this.isCustomTip=true;
            return this;
        }

        public PermissionsDeniedHelper bulid() {
            return new PermissionsDeniedHelper(this);
        }
    }
}
