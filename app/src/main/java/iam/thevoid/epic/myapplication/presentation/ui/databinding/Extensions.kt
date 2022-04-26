package iam.thevoid.epic.myapplication.presentation.ui.databinding

import androidx.databinding.*
import androidx.databinding.Observable
import io.reactivex.rxjava3.core.*


fun <T : Any> Flowable<T>.toBinding(): ObservableField<T> = RxObservableField(this)

fun <T : Any> Single<T>.toBinding(): ObservableField<T> = RxObservableField(toFlowable())

fun <T : Any> Flowable<List<T>>.toBinding(): RxObservableList<T> = RxObservableList(this)


fun <T : Any> Single<T>.loading(loading: ObservableBoolean) =
    doOnSubscribe { loading.set(true) }.doOnTerminate { loading.set(false) }

fun <T : Any> Maybe<T>.loading(loading: ObservableBoolean) =
    doOnSubscribe { loading.set(true) }.doOnTerminate { loading.set(false) }

fun Completable.loading(loading: ObservableBoolean) =
    doOnSubscribe { loading.set(true) }.doOnTerminate { loading.set(false) }


fun <T : Any> RxObservableList<T>.toRx() = Flowable.create<List<T>>({ emitter ->
    val callback = object :ObservableList.OnListChangedCallback<ObservableList<T>> () {


        override fun onChanged(sender: ObservableList<T>?) {
            sender?.also(emitter::onNext)
        }

        override fun onItemRangeChanged(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
            sender?.also(emitter::onNext)
        }

        override fun onItemRangeInserted(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
            sender?.also(emitter::onNext)
        }

        override fun onItemRangeMoved(sender: ObservableList<T>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
            sender?.also(emitter::onNext)
        }

        override fun onItemRangeRemoved(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
            sender?.also(emitter::onNext)
        }


    }
    emitter.onNext(this)
    emitter.setCancellable { removeOnListChangedCallback(callback) }
    addOnListChangedCallback(callback)
}, BackpressureStrategy.LATEST)

fun ObservableInt.toRx() = Flowable.create<Int>({ emitter ->
    val callback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            emitter.onNext(get())
        }
    }
    emitter.onNext(get())
    emitter.setCancellable { removeOnPropertyChangedCallback(callback) }
    addOnPropertyChangedCallback(callback)
}, BackpressureStrategy.LATEST)

fun <T> ObservableField<T>.toRx() = Flowable.create<T>({ emitter ->
    val callback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            get()?.also(emitter::onNext)
        }
    }
    get()?.also(emitter::onNext)
    emitter.setCancellable { removeOnPropertyChangedCallback(callback) }
    addOnPropertyChangedCallback(callback)
}, BackpressureStrategy.LATEST)