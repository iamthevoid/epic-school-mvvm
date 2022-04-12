package iam.thevoid.epic.myapplication.binding

import android.view.View
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable
import java.lang.ref.WeakReference

abstract class Binding<V: View, DATA : Any>(
    private val flowable: Flowable<DATA>,
    view: V
) {

    private val view = WeakReference(view)

    private var subscription: Disposable? = null

    abstract fun V.set(data: DATA)

    fun subscribeChanges() {
        subscription?.dispose()
        subscription = flowable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                 view.get()?.set(it)
            }, Throwable::printStackTrace)
    }

    fun unsubscribeChanges() {
        subscription?.dispose()
        subscription = null
    }
}