package tw.com.lig.module_base.widget;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

import androidx.appcompat.widget.AppCompatTextView;

public class FirstLineContentTextview extends AppCompatTextView {
    public FirstLineContentTextview(Context context) {
        super(context, null);
    }

    public FirstLineContentTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                setVisibility(View);
                Layout layout = getLayout();
                if (layout != null) {
                    int lineEnd = layout.getLineEnd(0);
                    if (onGetFirstLineListener != null&&lineEnd!=0) {
                        String originalText = getText().toString();
                        String substring = originalText.substring(0, lineEnd);
//                        setText(substring);
//                        onGetFirstLineListener.onFirstLine(substring);

                        onGetFirstLineListener.firstLine(substring,originalText.substring(lineEnd));
                        getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }

                }


            }
        });
    }
    public interface  OnGetFirstLineListener{
        void firstLine(String firstLine, String content);
//        void onFirstLine(String firstLine);
    }
    private OnGetFirstLineListener onGetFirstLineListener;

    public void setOnGetFirstLineListener(OnGetFirstLineListener onGetFirstLineListener) {
        this.onGetFirstLineListener = onGetFirstLineListener;
    }
}