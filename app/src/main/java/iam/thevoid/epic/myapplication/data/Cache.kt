package iam.thevoid.epic.myapplication.data

object Cache {
    private val CACHE = mutableMapOf<String, Any?>()

    fun <T> lazy(key: String, factory: () -> T): T =
        get(key) ?: factory().also { CACHE[key] = it }

    operator fun <T> get(key: String): T? = CACHE[key] as? T

    operator fun <T> set(key: String, item: T) {
        CACHE[key] = item
    }
}