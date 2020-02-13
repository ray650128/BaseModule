package tw.com.lig.module_base.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import tw.com.lig.module_base.R;


/**
 * Created by ljk on 2016/12/15.
 * 自定义的消息彈出框
 */

public class TextDialog extends Dialog {

    private TextView yes;//确定按钮
    private TextView no;//取消按钮
    private TextView titleTv;//消息標題文本
    private TextView messageTv;//消息提示文本
    private String titleStr;//從外界設定的title文本
    private String messageStr;//從外界設定的消息文本
    //确定文本和取消文本的显示内容
    private String yesStr, noStr;

    private boolean mB;

    private onNoOnclickListener noOnclickListener;//取消按钮被點击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被點击了的监听器
    private View mIdDiv;

    public TextDialog(Builder builder) {
        super(builder.mContext);
        this.titleStr = builder.titleStr;
        this.messageStr = builder.messageStr;
        this.yesStr = builder.yesStr;
        this.noStr = builder.noStr;
        this.noOnclickListener = builder.noOnclickListener;
        this.yesOnclickListener = builder.yesOnclickListener;
    }

    /**
     * 設定取消按钮的显示内容和监听
     *
     * @param str
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
        if (str != null) {
            noStr = str;
        }
        this.noOnclickListener = onNoOnclickListener;
    }

    /**
     * 設定确定按钮的显示内容和监听
     *
     * @param str
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener) {
        if (str != null) {
            yesStr = str;
        }
        this.yesOnclickListener = onYesOnclickListener;
    }

    public TextDialog(Context context) {
        super(context, R.style.CustomDialog);
    }

    public TextDialog(Context context, boolean b) {
        super(context, R.style.CustomDialog);
        mB = b;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getWindow() != null)
            getWindow().setWindowAnimations(R.style.bottomDialogAnim); // 添加動畫


        setContentView(R.layout.dialog_text);
        //按空白处不能取消動畫
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面資料
        initData();
        //初始化界面控件的事件
        initEvent();
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //設定确定按钮被點击後，向外界提供监听
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });
        //設定取消按钮被點击後，向外界提供监听
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                } else {
                    dismiss();
                }
            }
        });
    }


    /**
     * 初始化界面控件的显示資料
     */
    private void initData() {
        //如果用户自定了title和message
        if (titleStr != null) {
            titleTv.setVisibility(View.VISIBLE);
            titleTv.setText(titleStr);
        }
        if (messageStr != null) {
            messageTv.setText(messageStr);

        }
        //如果設定按钮的文字
        if (yesStr != null) {
            yes.setText(yesStr);
        }
        if (noStr != null) {
            no.setText(noStr);
        }
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
        titleTv = findViewById(R.id.title);
        messageTv = findViewById(R.id.message);
        if (mB) {
            messageTv.setGravity(Gravity.CENTER);
        }
        mIdDiv = findViewById(R.id.id_div);
    }


    /**
     * 從外界Activity為Dialog設定標題
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }

    /**
     * 從外界Activity為Dialog設定dialog的message
     *
     * @param message
     */
    public void setMessage(String message) {
        messageStr = message;
    }

    /**
     * 隐藏取消按钮 放在show後边
     */
    public void hideNo() {
        no.setVisibility(View.GONE);
        mIdDiv.setVisibility(View.GONE);
    }

    /**
     * 隐藏确认按键
     */
    public void hideYes() {
        yes.setVisibility(View.GONE);
        mIdDiv.setVisibility(View.GONE);
    }

    /**
     * 設定确定按钮和取消被點击的接口
     */
    public interface onYesOnclickListener {
        void onYesClick();
    }

    public interface onNoOnclickListener {
        void onNoClick();
    }


    public static class Builder {

        private String titleStr;//從外界設定的title文本
        private String messageStr;//從外界設定的消息文本
        //确定文本和取消文本的显示内容
        private String yesStr, noStr;

        private boolean mB;

        private onNoOnclickListener noOnclickListener;//取消按钮被點击了的监听器
        private onYesOnclickListener yesOnclickListener;//确定按钮被點击了的监听器
        private Context mContext;


        public Builder(Context context) {
            mContext = context;
        }


        public Builder setTitle(String title) {
            this.titleStr = title;
            return this;
        }

        public Builder setMessage(String msg) {
            this.messageStr = msg;
            return this;
        }

        public Builder setPositiveButtonText(String text) {
            this.yesStr = text;
            return this;
        }

        public Builder setPositiveButton(onYesOnclickListener listener) {
            this.yesOnclickListener = listener;
            return this;
        }

        public Builder setNegativeButtonText(String text) {
            this.noStr = text;
            return this;
        }

        public Builder setNegativeButton(onNoOnclickListener listener) {
            this.noOnclickListener = listener;
            return this;
        }


        public TextDialog build() {
            if (mContext == null)
                throw new IllegalStateException("context instanceof  activity and cannot null");

            return new TextDialog(this);
        }
    }
}
