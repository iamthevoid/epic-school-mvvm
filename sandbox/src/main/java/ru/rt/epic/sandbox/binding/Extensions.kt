package ru.rt.epic.sandbox.binding

import io.reactivex.rxjava3.core.Flowable

fun <T: Any> Flowable<T>.toBinding() = RxObservableField(this)