package iam.thevoid.epic.myapplication.presentation.ui.databinding

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class RxDiffCallback<T>: DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return areContentsTheSame(oldItem, newItem)
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    companion object {
        fun <T> get() : RxDiffCallback<T> = RxDiffCallback()
    }
}