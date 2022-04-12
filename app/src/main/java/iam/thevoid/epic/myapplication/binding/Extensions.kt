package iam.thevoid.epic.myapplication.binding

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import iam.thevoid.epic.myapplication.R
import iam.thevoid.epic.myapplication.binding.recycler.Adapter
import iam.thevoid.epic.myapplication.binding.recycler.ItemBindings
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.FlowableProcessor


val View.bindingCache: BindingCache
    get() = lazy(
        R.id.bindingsCache,
        factory = { BindingCache(this) },
        onCreate = ::addOnAttachStateChangeListener
    )


fun <V : View, DATA : Any> V.addBinding(flowable: Flowable<DATA>, setter: V.(DATA) -> Unit) {
    bindingCache.addBinding(object : Binding<V, DATA>(flowable, this) {
        override fun V.set(data: DATA) = setter(data)
    })
}

@Suppress("UNCHECKED_CAST")
fun <T> View.lazy(id: Int, factory: () -> T, onCreate: (T) -> Unit): T {
    return (getTag(id) as? T) ?: factory().also { onCreate(it) }.also { setTag(id, it) }
}

// View

fun View.showWhileLoading(loading: RxLoading) {
    addBinding(loading.observe()) { loading ->
        visibility = if (loading) View.VISIBLE else View.INVISIBLE
    }
}

// ImageView

fun ImageView.load(flowable: Flowable<Any>) {
    addBinding(flowable) { load(it) }
}

// EditText

val EditText.textWatcher: TextWatcherDelegate
    get() = lazy(R.id.editTextWatcherDelegate, ::TextWatcherDelegate, ::addTextChangedListener)

fun EditText.afterTextChanged(processor: FlowableProcessor<String>) {
    addBinding(
        Flowable.create<String>({ emitter ->
                val watcher = object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
                    override fun afterTextChanged(p0: Editable?) {
                        p0?.also { emitter.onNext(it.toString()) }
                    }
                }

                textWatcher.addWatcher(watcher)

                emitter.setCancellable { textWatcher.removeWatcher(watcher) }
            },
            BackpressureStrategy.LATEST)
    ) { editable -> processor.onNext(editable) }
}

// TextView

fun TextView.setText(flowable: Flowable<String>) {
    addBinding(flowable) { text = it }
}
fun TextView.setTextColor(flowable: Flowable<Int>) {
    addBinding(flowable) { setTextColor(it) }
}

// RecyclerView

fun <T: Any> RecyclerView.setItems(flowable: Flowable<List<T>>, bindings: ItemBindings) {
    val connectedAdapter = getTag(R.id.recyclerAdapter) as? Adapter<T>
    addBinding(flowable) { items ->
        if (connectedAdapter != null) {
            connectedAdapter.setItems(items)
        } else {
            adapter = Adapter<T>().also { setTag(R.id.recyclerAdapter, it) }.also {
                it.bindings = bindings
                it.setItems(items)
            }
        }
    }
}