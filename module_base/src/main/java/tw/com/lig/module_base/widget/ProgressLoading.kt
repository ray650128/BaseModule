package tw.com.lig.module_base.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import tw.com.lig.module_base.R

class ProgressLoading constructor(context: Context) : Dialog(context, R.style.LightProgressDialog) {

    private var animDrawable: AnimationDrawable? = null

    init {
        setContentView(R.layout.progress_dialog)
        setCancelable(true)
        setCanceledOnTouchOutside(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //獲取動畫
        val loadingView = findViewById<ImageView>(R.id.iv_loading)
        animDrawable = loadingView.background as AnimationDrawable
    }

    /**
     * 顯示對話框
     */
    fun showLoading() {
        if (!this.isShowing) {
            this.show()
        }
        animDrawable?.start()
    }

    /**
     * 隱藏對話框
     */
    fun hideLoading() {
        if (this.isShowing) {
            this.dismiss()
        }
        animDrawable?.stop()
    }
}