package iam.thevoid.epic.myapplication.databinding.utils

import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.subjects.BehaviorSubject

class RxLoading {

    private val _loading = BehaviorSubject.createDefault(false)

    fun observe(): Flowable<Boolean> =
        _loading.toFlowable(BackpressureStrategy.LATEST)


    fun <T : Any> single(): SingleTransformer<T, T> = SingleTransformer {
        it.doOnSubscribe { _loading.onNext(true) }.doOnTerminate { _loading.onNext(false) }
    }

    fun <T : Any> maybe(): MaybeTransformer<T, T> = MaybeTransformer {
        it.doOnSubscribe { _loading.onNext(true) }.doOnTerminate { _loading.onNext(false) }
    }

    fun completable(): CompletableTransformer = CompletableTransformer {
        it.doOnSubscribe { _loading.onNext(true) }.doOnTerminate { _loading.onNext(false) }
    }
}