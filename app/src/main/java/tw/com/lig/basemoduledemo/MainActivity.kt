package tw.com.lig.basemoduledemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tw.com.lig.module_base.base.BaseKtActivity

class MainActivity : BaseKtActivity() {
    override fun setupPresenter() {}

    override val contentView: Int
        get() = R.layout.activity_main

    override fun initData(savedInstanceState: Bundle?) {}

    override fun initWidget(savedInstanceState: Bundle?) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
