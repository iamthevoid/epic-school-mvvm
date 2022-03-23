package iam.thevoid.epic.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class StringsAdapter : RecyclerView.Adapter<StringsAdapter.StringsViewHolder>() {

    private val items: MutableList<String> = mutableListOf()


    fun setItems(newItems: List<String>) {
        DiffUtil.calculateDiff(StringsDiffCallback(items, newItems)).also { result ->
            items.clear()
            items.addAll(newItems)
            result.dispatchUpdatesTo(this)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringsViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_string, parent, false)
            .let(::StringsViewHolder)

    override fun onBindViewHolder(holder: StringsViewHolder, position: Int) =
        holder.onBind(items[position])

    override fun getItemCount(): Int = items.size




    class StringsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val textView: TextView = view.findViewById(R.id.text)

        fun onBind(value: String) {
            textView.text = value
        }
    }



    class StringsDiffCallback(
        private val oldList: List<String>,
        private val newList: List<String>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            areItemsTheSame(oldItemPosition, newItemPosition)
    }
}