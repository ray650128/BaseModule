package tw.com.lig.module_base.widget;


import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class FixCursorLayout extends FrameLayout {
    private TextView hintView;

    public FixCursorLayout(Context context) {
        this(context, null);
    }

    public FixCursorLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixCursorLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child instanceof EditText) {
            int gravity = ((EditText) child).getGravity() & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
            if (gravity == Gravity.END || gravity == Gravity.RIGHT) {
                setEditText((EditText) child);
            }
        }
    }

    private void setEditText(EditText editText) {
        hintView = new TextView(getContext());
        hintView.setTextColor(editText.getHintTextColors());
        hintView.setText(editText.getHint());
        hintView.setTextSize(TypedValue.COMPLEX_UNIT_PX, editText.getTextSize());
        hintView.setPadding(editText.getPaddingLeft(), editText.getPaddingTop(),
                editText.getPaddingRight(), editText.getPaddingBottom());
        hintView.setGravity(editText.getGravity());
        addView(hintView, editText.getLayoutParams());

        editText.setHint("");
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    hintView.setVisibility(VISIBLE);
                } else {
                    hintView.setVisibility(GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });
    }
}
