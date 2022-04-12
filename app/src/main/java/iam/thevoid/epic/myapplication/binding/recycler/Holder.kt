package iam.thevoid.epic.myapplication.binding.recycler

import androidx.recyclerview.widget.RecyclerView

class Holder<T: Any>(private val layout: Layout<T>) : RecyclerView.ViewHolder(layout.view) {

    fun onBind(data: T) {
        layout.notifyChanges(data)
    }
}