package tw.com.lig.module_base.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import androidx.appcompat.widget.AppCompatEditText;

import tw.com.lig.module_base.R;


/**
 * description:帶删除功能的EditText
 */
public class ClearEditText extends AppCompatEditText implements
        OnFocusChangeListener, TextWatcher {

    private OnTextChangeListener mInterface;
    private boolean isClearIconShow = true;

    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        //這裡構造方法也很重要，不加这個很多屬性不能再XML裡面定义
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {

        //獲取EditText的DrawableRight,假如没有設定我们就使用默认的圖片
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = getResources()
                    .getDrawable(R.drawable.icon_fork_search);
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }


    /**
     * 因為我们不能直接给EditText設定點击事件，所以我们用记住我们按下的位置来模拟點击事件
     * 当我们按下的位置 在  EditText的宽度 - 圖標到控件右边的間距 - 圖標的宽度  和
     * EditText的宽度 - 圖標到控件右边的間距之間我们就算點击了圖標，竖直方向没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                boolean touchable = event.getX() > (getWidth()
                        - getPaddingRight() - mClearDrawable.getIntrinsicWidth())
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    this.setText("");
                }
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦點發生變化的時候，判断裡面字串长度設定清除圖標的显示與隐藏
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (null !=mOnEditTextFocusChanged){
            mOnEditTextFocusChanged.onFocusChanged(hasFocus);
        }
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }


    /**
     * 設定清除圖標的显示與隐藏，調用setCompoundDrawables為EditText繪製上去
     *
     * @param visible
     */
    public void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }


    /**
     * 当輸入框裡面内容發生變化的時候回調的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count,
                              int after) {
        if (mInterface != null) {
            mInterface.onTextChanged(s, start, count, after);
        }
        if (isClearIconShow) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    /**
     * 設定晃动動畫
     */
    public void setShakeAnimation() {
        this.setAnimation(shakeAnimation(5));
    }


    /**
     * 晃动動畫
     *
     * @param counts 1秒钟晃动多少下
     * @return
     */
    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

    public void setOnTextChangeListener(OnTextChangeListener listener) {
        mInterface = listener;
    }

    public interface OnTextChangeListener {
        void onTextChanged(CharSequence s, int start, int count, int after);
    }


    public interface OnEditTextFocusChanged{
        void onFocusChanged(boolean hasFocus);
    }

    public OnEditTextFocusChanged mOnEditTextFocusChanged;

    public void setOnEditTextFocusChanged(OnEditTextFocusChanged mOnEditTextFocusChanged) {
        this.mOnEditTextFocusChanged = mOnEditTextFocusChanged;
    }
}
