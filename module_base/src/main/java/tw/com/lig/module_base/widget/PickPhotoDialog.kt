package tw.com.lig.module_base.widget

//import com.jihu.module_tieba.R
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import tw.com.lig.module_base.R
import kotlinx.android.synthetic.main.layout_pick_photo_dialog.*


class PickPhotoDialog(context: Context) : BaseDialog(context) {
    var onConfirmListener: ((Int) -> Unit)? = null

    override fun getContentView(): Int {
        return R.layout.layout_pick_photo_dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setGravity(Gravity.BOTTOM)
        window?.setWindowAnimations(R.style.bottomDialogAnim) // 添加動畫
        tv_cancel.setOnClickListener {
            dismiss()
        }
        tv_album.setOnClickListener {
            dismiss()
            onConfirmListener?.invoke(0)
        }
        tv_capture.setOnClickListener {
            dismiss()
            onConfirmListener?.invoke(1)
        }

    }

    fun setOnConfirmListner(listener: ((Int) -> Unit)) {
        onConfirmListener = listener
    }


}


