package tw.com.lig.module_base.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import tw.com.lig.module_base.R;


/**
 * Created by ljk on 2017/7/10.
 * <p>
 * 通用的底部彈出框
 */

public class BottomDialog extends Dialog implements View.OnClickListener {
    private View content_View = null;
    private int[] listItems;
    private DialogClickListener mListener;
    private BouncingView bouncingView;

    //recycler適配相關
    int mRecyclerView;
    RecyclerView.Adapter mAdapter;


    public BottomDialog(@NonNull Context context, int layout_id, int[] views) {
        super(context, R.style.NoneMdialog);

        content_View = getLayoutInflater().inflate(
                layout_id, null);
        LinearLayout ll = new LinearLayout(context);
        ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        bouncingView = new BouncingView(context);
        float arc_max_height = context.getResources().getDimension(R.dimen.arc_max_height);
        bouncingView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) arc_max_height));
        ll.addView(bouncingView);
        ll.addView(content_View);
        setContentView(ll);
        setCanceledOnTouchOutside(true);
        listItems = views;
    }

    public BottomDialog(@NonNull Context context, int layout_id, int recyclerView, RecyclerView.Adapter adapter) {
        super(context, R.style.NoneMdialog);

        content_View = getLayoutInflater().inflate(
                layout_id, null);
        LinearLayout ll = new LinearLayout(context);
        ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        bouncingView = new BouncingView(context);
        float arc_max_height = context.getResources().getDimension(R.dimen.arc_max_height);
        bouncingView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) arc_max_height));
        ll.addView(bouncingView);
        ll.addView(content_View);
        setContentView(ll);
        setCanceledOnTouchOutside(true);
        RecyclerView rv = (RecyclerView) findViewById(recyclerView);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(context));

    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.bottomDialogAnim); // 添加動畫

        if (listItems != null) {
            //遍歷控件id,添加點擊事件
            for (int id : listItems) {
                findViewById(id).setOnClickListener(this);
            }
        }
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);

    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (mListener != null) {
            mListener.dialogClick(this, v);
        }
    }

    /**
     * 暴露給外邊的dialog
     *
     * @param listener
     */
    public void setClickListener(DialogClickListener listener) {
        this.mListener = listener;
    }


    public interface DialogClickListener {
        void dialogClick(Dialog dialog, View v);
    }

    @Override
    public void setContentView(@NonNull View view) {

        super.setContentView(view);
    }

    @Override
    public void show() {
        super.show();
        if (bouncingView != null) {
            bouncingView.show();
        }
    }

    /**
     * 根據手機的解析度從 dp 的單位 轉成為 px(像素)
     */
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
