package iam.thevoid.epic.myapplication.databinding

import android.view.View
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.lang.ref.WeakReference

abstract class Binding<V : View, DATA : Any>(
    private val flowable: Flowable<DATA>,
    view: V
) {

    private val view = WeakReference(view)

    private val disposable = CompositeDisposable()

    abstract fun V.set(data: DATA)

    fun subscribeChanges() {
        disposable.add(
            flowable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view.get()?.set(it) }, Throwable::printStackTrace)
        )
    }

    fun unsubscribeChanges() {
        disposable.clear()
    }
}