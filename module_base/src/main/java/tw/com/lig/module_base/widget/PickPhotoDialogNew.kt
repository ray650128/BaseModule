package tw.com.lig.module_base.widget

//import com.jihu.module_tieba.R
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import tw.com.lig.module_base.R
import kotlinx.android.synthetic.main.layout_pick_photo_dialog.*
import org.jetbrains.anko.onClick


class PickPhotoDialogNew(context: Context) : BaseDialog(context) {
    var onConfirmListener: ((Int) -> Unit)? = null

    override fun getContentView(): Int {
        return R.layout.layout_photo_pick_new
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setGravity(Gravity.BOTTOM)
        window?.setWindowAnimations(tw.com.lig.module_base.R.style.bottomDialogAnim) // 添加動畫

        tv_cancel.onClick {
            dismiss()
        }
        tv_album.onClick {
            dismiss()
            onConfirmListener?.invoke(0)
        }
        tv_capture.onClick {
            dismiss()
            onConfirmListener?.invoke(1)
        }

    }

    fun setOnConfirmListner(listener: ((Int) -> Unit)) {
        onConfirmListener = listener
    }
    fun getCancelText():TextView{
        return tv_cancel

    }



}


