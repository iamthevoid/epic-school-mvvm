package iam.thevoid.epic.myapplication.binding

import android.view.View
import java.lang.reflect.ParameterizedType
import java.lang.reflect.TypeVariable

class BindingCache(view: View): View.OnAttachStateChangeListener {

    private var attached: Boolean = view.isAttachedToWindow

    private val cache: MutableMap<String, Binding<*, *>> = mutableMapOf()

    fun addBinding(binding: Binding<*, *>, key: String = key()) {
        cache[key]?.unsubscribeChanges()
        cache[key] = binding

        if (attached) {
            binding.subscribeChanges()
        }
    }


    override fun onViewAttachedToWindow(p0: View?) {
        attached = true
        cache.forEach { entry -> entry.value.subscribeChanges() }
    }

    override fun onViewDetachedFromWindow(p0: View?) {
        attached = false
        cache.forEach { entry -> entry.value.unsubscribeChanges() }
    }

    private fun key(): String = with(Thread.currentThread().stackTrace) {

        buildString {
            for (i in size - 1 downTo DEPTH + 1)
                append(this@with[i].methodName).append("_")

            append(
                if (size < DEPTH + 1) "" else with(this@with[DEPTH]) {
                    Class.forName(className).methods.find { it.name == methodName }
                }
                    ?.let {
                        if (it.genericParameterTypes.size > 1) {
                            "${it.name}_${(it.genericParameterTypes.component2() as? ParameterizedType)?.actualTypeArguments?.let { types ->
                                if (types.isNotEmpty()) types.component1().let { type ->
                                    (type as? TypeVariable<*>)?.bounds.let { types ->
                                        (types?.firstOrNull() as? Class<*>)?.name
                                    } ?: type.toString()
                                } else ""
                            }}"
                        } else
                            "${it.name}_${it.typeParameters.let { typeParameters ->
                                typeParameters.firstOrNull()?.bounds.let { types ->
                                    (types?.firstOrNull() as? Class<*>)?.name
                                }.orEmpty()
                            }}"
                    }.orEmpty()
            )
        }
    }

    companion object {
        private const val DEPTH = 5
    }
}