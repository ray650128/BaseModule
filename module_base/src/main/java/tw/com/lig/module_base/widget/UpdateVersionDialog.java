package tw.com.lig.module_base.widget;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import tw.com.lig.module_base.R;
//import com.jihu.jihu.R;

public class UpdateVersionDialog extends BaseDialog implements View.OnClickListener {
    private TextView tv_version,tv_desc,tv_progress;
    private Button btn_update;
    private View iv_colse;
    private ProgressBar progressBar;

    public UpdateVersionDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int getContentView() {
        return R.layout.layout_version_update_dialog2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
        setCanceledOnTouchOutside(false);
        tv_progress=findViewById(R.id.tv_progress);
        tv_version = findViewById(R.id.tv_version);
        tv_desc=findViewById(R.id.tv_desc);
        btn_update=findViewById(R.id.btn_update);
        btn_update.setOnClickListener(this);
        iv_colse = findViewById(R.id.iv_close);
        iv_colse.setOnClickListener(this);
        progressBar=findViewById(R.id.progressBar);

    }
    public void setVersion(String version){
        tv_version.setText(version+"新版本上線");
    }
    public void setDesc(CharSequence desc){
        tv_desc.setText(desc);
    }

    @Override
    public void onClick(View v) {
        if(v==iv_colse){
            dismiss();
        }else if(v==btn_update){
            if(onClickListener!=null){
                onClickListener.onUpdateClick();
                progressBar.setVisibility(View.VISIBLE);
                btn_update.setVisibility(View.INVISIBLE);
//                dismiss();
            }
        }

    }
    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public  interface OnClickListener{
      void onUpdateClick();



    }
    public void setProgress(int progress) {

        progressBar.setProgress(progress);
        tv_progress.setText("下載中"+progress+"%");
    }
}
