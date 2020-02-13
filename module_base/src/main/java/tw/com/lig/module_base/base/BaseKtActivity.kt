package tw.com.lig.module_base.base


abstract class BaseKtActivity : BaseActivity<SimpleBasePresenter>() {
    override val isTitleCentered: Boolean = true
}