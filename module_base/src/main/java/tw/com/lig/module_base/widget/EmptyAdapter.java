package tw.com.lig.module_base.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import tw.com.lig.module_base.R;


public class EmptyAdapter extends RecyclerView.Adapter<EmptyAdapter.ViewHolder> {
    private Context context;
    private String emptyMsg;
    private int emptyDrawable;

    public EmptyAdapter(Context context, String emptyMsg) {
        this.context = context;
        this.emptyMsg = emptyMsg;
    }

    public EmptyAdapter(Context context, String emptyMsg, int emptyDrawable) {
        this.context = context;
        this.emptyMsg = emptyMsg;
        this.emptyDrawable = emptyDrawable;
    }

    public EmptyAdapter(Context context) {
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_empty_normal,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!TextUtils.isEmpty(emptyMsg)){

            holder.tv_msg.setText(emptyMsg);
            if (emptyDrawable!=0){

                holder.iv_icon.setImageResource(emptyDrawable);
            }
        }

    }

    @Override
    public int getItemCount() {
        return 1;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_msg;
        ImageView iv_icon;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_msg=itemView.findViewById(R.id.tv_msg);
            iv_icon=itemView.findViewById(R.id.iv_icon);
        }
    }
}
