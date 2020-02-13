package tw.com.lig.module_base.base

import android.content.Context
import android.view.View
import android.view.ViewGroup


class RecyclerCommonAdapter<T>(context: Context, private val layoutId: Int, private val items: List<T>, val init: (View, T) -> Unit): RecyclerBaseAdapter<RecyclerCommonAdapter.ItemHolder<T>>(context) {
/*    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        return true
    }*/

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val view: View = inflater.inflate(layoutId, parent, false)
        return ItemHolder<T>(view, init)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val vh: ItemHolder<T> = holder as ItemHolder<T>
        vh.bind(items.get(position))
    }

    //注意init是個函數形式的輸入參數
    class ItemHolder<in T>(val view: View, val init: (View, T) -> Unit) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        fun bind(item: T) {
            init(view, item)
        }
    }
}