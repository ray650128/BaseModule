package tw.com.lig.module_base.base

import android.view.ViewGroup


abstract class RecyclerBaseAdapter2<VH : androidx.recyclerview.widget.RecyclerView.ViewHolder>() : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>(), OnItemClickListener, OnItemLongClickListener {
    protected  var mOnItemClickListener:((position:Int)->Unit)?=null
    protected  var mOnItemLongClickListener:((position:Int)->Unit)?=null
    protected  var mOnBindListener:((position:Int)->Unit)?=null
//    val inflater: LayoutInflater = LayoutInflater.from(context)

    //獲得列表项的個數，需要子類重寫
    override abstract fun getItemCount(): Int

    //根据布局文件创建视圖持有者，需要子類重寫
    override abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder

    //绑定视圖持有者中的各個控件對象，需要子類重寫
    override abstract fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int)

    override fun getItemViewType(position: Int): Int = 0

    override fun getItemId(position: Int): Long = position.toLong()

    var itemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    var itemLongClickListener: /*AdapterView.*/OnItemLongClickListener? = null
    fun setOnItemLongClickListener(listener:/* AdapterView.*/OnItemLongClickListener) {
        this.itemLongClickListener = listener
    }

    override fun onItemClick(position: Int) {
    }

    override fun onItemLongClick(position: Int) {

    }
    fun addOnItemClickListener(listener:(position:Int)->Unit){
        this.mOnItemClickListener=listener
    }
    fun addOnItemLongClickListener(listener:(position:Int)->Unit){
        this.mOnItemLongClickListener=listener
    }
    fun addOnBindListener(listener:(position:Int)->Unit){
        this.mOnItemClickListener=listener
    }


}