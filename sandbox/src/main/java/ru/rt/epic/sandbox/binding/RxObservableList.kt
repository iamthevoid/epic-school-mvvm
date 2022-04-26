package ru.rt.epic.sandbox.binding

import androidx.databinding.ObservableList
import androidx.recyclerview.widget.DiffUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import me.tatarka.bindingcollectionadapter2.collections.AsyncDiffObservableList

class RxObservableList<T: Any>(private val flowable: Flowable<List<T>>): AsyncDiffObservableList<T>(object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}) {

    private val subscription = CompositeDisposable()

    override fun addOnListChangedCallback(callback: ObservableList.OnListChangedCallback<out ObservableList<T>>) {
        super.addOnListChangedCallback(callback)
        subscription.add(
            flowable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::update)
        )
    }

    override fun removeOnListChangedCallback(callback: ObservableList.OnListChangedCallback<out ObservableList<T>>) {
        super.removeOnListChangedCallback(callback)
        subscription.clear()
    }
}