package iam.thevoid.epic.myapplication.databinding.recycler

import androidx.recyclerview.widget.RecyclerView

class Holder<T : Any>(private val layout: Layout<T>): RecyclerView.ViewHolder(layout.view) {

    fun onBind(item: T) {
        layout.set(item)
    }
}