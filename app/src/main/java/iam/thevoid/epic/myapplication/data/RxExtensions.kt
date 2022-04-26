package iam.thevoid.epic.myapplication.data

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

fun <T : Any> Single<T>.retryWithDelay(
    delay: Long = 2000L,
    filter: (Throwable) -> Boolean = { true }
): Single<T> {
    return retryWhen { errorThrowable ->
        errorThrowable.flatMap {
            if (filter(it)) {
                Flowable.timer(delay, TimeUnit.MILLISECONDS)
            } else {
                Flowable.error(it)
            }
        }
    }
}