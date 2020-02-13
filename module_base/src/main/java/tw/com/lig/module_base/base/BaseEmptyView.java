package tw.com.lig.module_base.base;


import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import tw.com.lig.module_base.R;

public class BaseEmptyView extends ConstraintLayout{

    private TextView tv_msg;

    public BaseEmptyView(Context context) {
        super(context);
    }

    public BaseEmptyView(Context context, AttributeSet attrs) {
//            super(context, attrs);
        this(context, attrs, 0);



    }


    public BaseEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EmptyView, defStyleAttr, 0);
        String tip = a.getString(R.styleable.EmptyView_tip);
        View inflate = LayoutInflater.from(context).inflate(R.layout.view_empty_normal, this);
        tv_msg = inflate.findViewById(R.id.tv_msg);
        if (!TextUtils.isEmpty(tip)){
            tv_msg.setText(tip);
        }

        a.recycle();
    }
    public void setTip(String tip){
        tv_msg.setText(tip);

    }
}
