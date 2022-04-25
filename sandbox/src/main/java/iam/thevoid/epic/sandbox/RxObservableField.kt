package iam.thevoid.epic.sandbox

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable

class RxObservableField<T: Any>(private val flowable: Flowable<T>): ObservableField<T>() {

    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        super.addOnPropertyChangedCallback(callback)
        disposable.add(
            flowable
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(::set)
        )
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        super.removeOnPropertyChangedCallback(callback)
        disposable.clear()
    }
}