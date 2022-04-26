package ru.rt.epic.sandbox.binding

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable

class RxObservableField<T: Any>(private val flowable: Flowable<T>): ObservableField<T>() {

    val subscription = CompositeDisposable()

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        super.addOnPropertyChangedCallback(callback)
        subscription.add(
            flowable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::set)
        )
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        super.removeOnPropertyChangedCallback(callback)
        subscription.clear()
    }
}