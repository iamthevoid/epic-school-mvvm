package ru.rt.epic.sandbox.binding

import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

fun <T : Any> ObservableField<T>.toRx(): Flowable<T> = Flowable.create<T>({ emitter ->
    val callback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            get()?.also(emitter::onNext)
        }
    }
    addOnPropertyChangedCallback(callback)
    emitter.setCancellable { removeOnPropertyChangedCallback(callback) }
    get()?.also(emitter::onNext)
}, BackpressureStrategy.LATEST)

fun <T : Any> Flowable<T>.toBinding() = RxObservableField(this)

fun <T : Any> Single<T>.toBinding() = toFlowable().toBinding()

fun <T : Any> Flowable<List<T>>.toListBinding() = RxObservableList(this)

fun <T: Any> Single<T>.loading(observable: ObservableBoolean) =
    doOnSubscribe { observable.set(true) }.doOnTerminate { observable.set(false) }