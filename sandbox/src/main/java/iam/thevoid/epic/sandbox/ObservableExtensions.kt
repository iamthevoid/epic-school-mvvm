package iam.thevoid.epic.sandbox

import androidx.databinding.ObservableField
import io.reactivex.rxjava3.core.Flowable

fun <T : Any> Flowable<T>.toBinding(): ObservableField<T> = RxObservableField(this)