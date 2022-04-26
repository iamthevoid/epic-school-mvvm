package iam.thevoid.epic.myapplication.data

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*

object Cache {

    val cache = BehaviorSubject.createDefault(mutableMapOf<String, Any>())

    inline fun <reified T> observe(key: String) : Observable<T> =
        cache.filter { it[key] is T }.map { it[key] as T  }.distinctUntilChanged()


    fun <T : Any> lazy(key: String, factory: () -> T): T =
        (get(key)) ?: factory().also { set(key, it) }

    operator fun <T : Any> get(key: String): T? = cache[key] as? T

    operator fun <T : Any> set(key: String, item: T) {
        cache[key] = item
    }

}
