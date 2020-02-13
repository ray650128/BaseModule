package tw.com.lig.module_base.base

import android.view.View
import android.view.ViewGroup
import tw.com.lig.module_base.utils.inflate


class RecyclerCommonAdapter2<T>( private val layoutId: Int, private var items: List<T>, val init: (View, T) -> Unit): RecyclerBaseAdapter2<RecyclerCommonAdapter2.ItemHolder<T>>() {
/*    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        return true
    }*/

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
//        val view: View = inflater.inflate(layoutId, parent, false)
        val view: View = parent.inflate(layoutId)

        return ItemHolder<T>(view, init)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val vh: ItemHolder<T> = holder as ItemHolder<T>
        val item = items.get(position)

        vh.bind(item)
        vh.itemView.setOnClickListener {
            mOnItemClickListener?.apply {
                mOnItemClickListener!!(position)
            }

        }

        vh.itemView.setOnLongClickListener {
            mOnItemLongClickListener?.apply {
                mOnItemLongClickListener!!(position)
                return@setOnLongClickListener true
            }
            false

        }
//        addOnBindListener {
//
//        }
        /*mOnBindListener?.let {
            it.invoke(position)
        }*/
    }

    //注意init是個函數形式的輸入參數
    class ItemHolder<in T>(val view: View, val init: (View, T) -> Unit) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        fun bind(item: T) {
            init(view, item)
        }
    }
    public fun getItems():MutableList<T>{
        return items as MutableList<T>
    }
}