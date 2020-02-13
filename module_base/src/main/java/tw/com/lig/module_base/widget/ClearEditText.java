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
 * description:帶刪除功能的EditText
 */
public class ClearEditText extends AppCompatEditText implements
        OnFocusChangeListener, TextWatcher {

    private OnTextChangeListener mInterface;
    private boolean isClearIconShow = true;

    /**
     * 刪除按鈕的引用
     */
    private Drawable mClearDrawable;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        //這裡構造方法也很重要，不加這個很多屬性不能再XML裡面定義
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {

        //獲取EditText的DrawableRight,假如沒有設定我們就使用預設的圖片
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
     * 因為我們不能直接給EditText設定點擊事件，所以我們用記住我們按下的位置來模擬點擊事件
     * 當我們按下的位置 在  EditText的寬度 - 圖標到控件右邊的間距 - 圖標的寬度  和
     * EditText的寬度 - 圖標到控件右邊的間距之間我們就算點擊了圖標，豎直方向沒有考慮
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
     * 當ClearEditText焦點發生變化的時候，判斷裡面字串長度設定清除圖標的顯示與隱藏
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
     * 設定清除圖標的顯示與隱藏，調用setCompoundDrawables為EditText繪製上去
     *
     * @param visible
     */
    public void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }


    /**
     * 當輸入框裡面內容發生變化的時候回調的方法
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
     * 設定晃動動畫
     */
    public void setShakeAnimation() {
        this.setAnimation(shakeAnimation(5));
    }


    /**
     * 晃動動畫
     *
     * @param counts 1秒鐘晃動多少下
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
