package iam.thevoid.epic.myapplication.presentation.util

import androidx.recyclerview.widget.DiffUtil
import iam.thevoid.epic.myapplication.data.model.musicbrnz.Artist

class DiffCallback(private val oldList: List<Artist>, private val newList: List<Artist>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].name == newList[newItemPosition].name

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}