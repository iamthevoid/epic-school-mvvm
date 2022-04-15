package iam.thevoid.epic.myapplication.presentation.util

import androidx.recyclerview.widget.DiffUtil
import iam.thevoid.epic.myapplication.presentation.ui.artist.ArtistData

class DiffCallback(private val oldList: List<ArtistData>, private val newList: List<ArtistData>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].artist.name == newList[newItemPosition].artist.name

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}