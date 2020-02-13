package tw.com.lig.module_base.base

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.tbruyelle.rxpermissions2.RxPermissions
import tw.com.lig.module_base.R
import tw.com.lig.module_base.data.SPConstant
import tw.com.lig.module_base.utils.GetPathFromUri4kitkat
import tw.com.lig.module_base.utils.PermissionUtil
import tw.com.lig.module_base.utils.PermissionsDeniedHelper
import tw.com.lig.module_base.utils.SPutils
import tw.com.lig.module_base.widget.BottomDialog
import tw.com.lig.module_base.widget.PickPhotoDialog
import top.zibin.luban.Luban
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

abstract class BasePhotoActivity : BaseKtActivity() {
    var mImagePath: String? = null
    private var bottomDialog: BottomDialog? = null
    var pickPhotoDialog: PickPhotoDialog? = null
    private val SELECT_IMAGE_RESULT_CODE = 200
    private val TAKE_PHOTO_REQUEST_CODE = 400
//    var photoPickListener:PhotoPickListener?=null
//    lateinit var photoPickListener: PhotoPickListener


    open fun showBottomDialog() {
        /*   if (bottomDialog == null) {
               bottomDialog= BottomDialog(this,R.layout.layout_photo_dialog, intArrayOf(R.id.tv_album,R.id.tv_capture))
           }*/
        if (pickPhotoDialog == null) {
            pickPhotoDialog = PickPhotoDialog(this@BasePhotoActivity)
        }
        pickPhotoDialog?.show()
        pickPhotoDialog?.setOnConfirmListner {
            when (it) {
                0 -> {
                    selectPhoto()
                }
                else -> {
                    capturePhoto()
                }
            }
        }
    }

    fun toCamera() {
        try {
            //             执行拍照前，應該先判断SD卡是否存在
            val SDState = Environment.getExternalStorageState()
            if (SDState == Environment.MEDIA_MOUNTED) {
                /**
                 * 通过指定圖片儲存路径，解决部分机型onActivityResult回調 data返回為null的情況
                 */
                if (getExternalFilesDir(Environment.DIRECTORY_PICTURES) == null)
                    return //todo 判空
                //獲取與應用相關联的路径
                val imageFilePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath
                val formatter = SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                //根据当前時間生成圖片的名称
                val timestamp = "/" + formatter.format(Date()) + ".jpg"
                val imageFile = File(imageFilePath, timestamp)// 通过路径创建保存文件
                mImagePath = imageFile.absolutePath
                val mUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    FileProvider.getUriForFile(this@BasePhotoActivity, "$packageName.fileprovider", imageFile)
                } else {
                    Uri.fromFile(imageFile)

                }

                //                Uri imageFileUri = Uri.fromFile(imageFile);// 獲取文件的Uri
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)// 告诉相機拍摄完毕輸出圖片到指定的Uri
                startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE)
            } else {
                showToast("内存卡不存在")
            }

        } catch (e: Exception) {
            e.printStackTrace()

        }

    }

    fun capturePhoto() {
        PermissionUtil.launchCamera(object : PermissionUtil.RequestPermission {
            override fun onRequestPermissionSuccess() {
                //                        openCaptureActivity(RESULT_CAPTURE);
                toCamera()
            }

            override fun onRequestPermissionFailure() {
                val message = "請在設定-應用-" + getString(tw.com.lig.module_base.R.string.app_name) + "-权限 中開啟读寫儲存权限，才能进行正常拍照。"
                PermissionsDeniedHelper.Builder(this@BasePhotoActivity)
                        .customTip()
                        .setMessage(message)
                        .bulid()

            }
        }, RxPermissions(this@BasePhotoActivity))

    }

    protected fun selectPhoto() {
        PermissionUtil.externalStorageand(object : PermissionUtil.RequestPermission {
            override fun onRequestPermissionSuccess() {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, SELECT_IMAGE_RESULT_CODE)

            }

            override fun onRequestPermissionFailure() {
                showTip()

            }
        }, RxPermissions(this))
    }

    private fun showTip() {
//        SPutils.put(SPConstant.ALLOW_CACHE, false)
        val message = "請在設定-應用-" + getString(R.string.app_name) + "-权限 中開啟读寫儲存权限，才能正常上传圖片和视频。"
        PermissionsDeniedHelper.Builder(this@BasePhotoActivity)
                .customTip()
                .setMessage(message)
                .bulid()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var imagePath: String? = null
        try {
            if (resultCode == Activity.RESULT_OK) {

                if (data != null && data.data != null) {
                    when (requestCode) {
                        SELECT_IMAGE_RESULT_CODE, TAKE_PHOTO_REQUEST_CODE -> {
                            //                            imagePath = GetPathFromUri4kitkat.getPath(this, data.getData());
                            imagePath = GetPathFromUri4kitkat.getPath(this, data.data)


                        }

                    }
                } else {//無資料使用指定的圖片路径
                    imagePath = mImagePath
                }
//                toast("圖片路径$imagePath")
//                data?.putExtra("imagePath",imagePath)

                if (isImageCompressed()) {//如果對圖片进行压缩
                    //對圖片进行压缩
                    val compressedPicPath = Luban.with(this@BasePhotoActivity).load(File(imagePath)).get().get(0).absolutePath
                    onImageSelected(compressedPicPath)
                } else {
                    imagePath?.let { onImageSelected(it) }
                }


            }


        } catch (e: Exception) {
            e.printStackTrace()

        }


    }

    fun getImagePath(): String? {
        return mImagePath
    }


    /**
     * 是否對圖片进行压缩
     */
    protected fun isImageCompressed(): Boolean {
        return true

    }

    abstract fun onImageSelected(imagePath: String)


}
