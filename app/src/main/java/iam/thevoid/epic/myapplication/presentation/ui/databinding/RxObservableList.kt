package iam.thevoid.epic.myapplication.presentation.ui.databinding

import androidx.databinding.ObservableList
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import me.tatarka.bindingcollectionadapter2.collections.AsyncDiffObservableList

class RxObservableList<T: Any>(private val flowable: Flowable<List<T>>):
    AsyncDiffObservableList<T>(RxDiffCallback.get()) {

    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun addOnListChangedCallback(callback: ObservableList.OnListChangedCallback<out ObservableList<T>>) {
        super.addOnListChangedCallback(callback)
        disposable.add(
            flowable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::update)
        )
    }

    override fun removeOnListChangedCallback(callback: ObservableList.OnListChangedCallback<out ObservableList<T>>) {
        super.removeOnListChangedCallback(callback)
        disposable.clear()
    }
}