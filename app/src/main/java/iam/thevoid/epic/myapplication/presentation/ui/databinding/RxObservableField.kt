package iam.thevoid.epic.myapplication.presentation.ui.databinding

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

class RxObservableField<T: Any>(private val flowable: Flowable<T>): ObservableField<T>() {

    private var disposable: Disposable? = null

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        super.addOnPropertyChangedCallback(callback)
        disposable = flowable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::set)

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        super.removeOnPropertyChangedCallback(callback)
        disposable?.dispose()
        disposable = null
    }
}