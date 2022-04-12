package iam.thevoid.epic.myapplication.binding.recycler

import android.view.ViewGroup

interface LayoutFactory<T: Any> {
    fun createLayout(viewGroup: ViewGroup) : Layout<T>
}