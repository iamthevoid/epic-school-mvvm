package iam.thevoid.epic.myapplication.binding

import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.processors.BehaviorProcessor

class RxLoading {

    private val _loading = BehaviorProcessor.createDefault(false)

    fun observe() : Flowable<Boolean> = _loading

    fun <T : Any> single(): SingleTransformer<T, T> = SingleTransformer<T, T> { upstream ->
        upstream
            .doOnSubscribe { _loading.onNext(true) }
            .doOnEvent { _, _ -> _loading.onNext(false) }
    }

    fun <T : Any> maybe() = MaybeTransformer<T, T> { upstream ->
        upstream
            .doOnSubscribe { _loading.onNext(true) }
            .doOnEvent { _, _ -> _loading.onNext(false) }
    }


    fun completable() = CompletableTransformer { upstream ->
        upstream
            .doOnSubscribe { _loading.onNext(true) }
            .doOnEvent { _loading.onNext(false) }
    }

}