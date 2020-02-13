package tw.com.lig.module_base.widget;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import tw.com.lig.module_base.R;

public class WhiteDialog extends BaseDialog implements View.OnClickListener {
    private TextView tv_confirm,tv_cancel,tv_tip;
    private OnDialogClickListener onDialogClickListener;
    public WhiteDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int getContentView() {
        return R.layout.layout_dialog_white;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_cancel=findViewById(R.id.tv_cancel);
        tv_confirm=findViewById(R.id.tv_confirm);
        tv_tip=findViewById(R.id.tv_tip);
        tv_confirm.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }
    public void setTip(String tip){
        tv_tip.setText(tip);
    }

    @Override
    public void onClick(View v) {
        if(v==tv_confirm){
            dismiss();
            if(onDialogClickListener!=null){
                onDialogClickListener.onConfirm();
            }

        }else if(v==tv_cancel){
            dismiss();
        }
    }
    public interface  OnDialogClickListener{
        void onConfirm();
    }

    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener;
    }
}
