package iam.thevoid.epic.myapplication.binding.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import iam.thevoid.epic.myapplication.ui.recycler.DiffCallback

class Adapter<T : Any> : RecyclerView.Adapter<Holder<T>>() {

    var bindings: ItemBindings = ItemBindings.EMPTY

    private val layoutCache by lazy { mutableMapOf<Int, LayoutFactory<*>>() }

    private val items: MutableList<T> = mutableListOf()

    fun setItems(newItems: List<T>) {
        DiffUtil.calculateDiff(DiffCallback(items, newItems)).also { result ->
            items.clear()
            items.addAll(newItems)
            result.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder<T> =
        createLayout(viewType, parent)?.let(::Holder)
            ?: throw IllegalStateException("Binding not provided")

    @Suppress("UNCHECKED_CAST")
    private fun createLayout(viewType: Int, parent: ViewGroup) =
        getLayoutFactory(viewType).createLayout(parent) as? Layout<T>

    private fun getLayoutFactory(viewType: Int) =
        layoutCache[viewType] ?: (bindings.factory(viewType).also { layoutCache[viewType] = it })




    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: Holder<T>, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemViewType(position: Int): Int =
        items.getOrNull(position)?.javaClass?.name.hashCode()

    override fun getItemCount(): Int = items.size
}