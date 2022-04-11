package iam.thevoid.epic.myapplication.databinding

import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import coil.load
import iam.thevoid.epic.myapplication.R
import iam.thevoid.epic.myapplication.databinding.delegate.TextWatcherDelegate
import iam.thevoid.epic.myapplication.databinding.recycler.Adapter
import iam.thevoid.epic.myapplication.databinding.recycler.ItemBindings
import iam.thevoid.epic.myapplication.databinding.utils.RxLoading
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.FlowableProcessor
import java.util.logging.Handler


inline fun <reified T : Any> View.lazy(
    @IdRes id: Int,
    create: () -> T,
    onCreated: (T) -> Unit = {}
): T = (getTag(id) as? T) ?: create().also { onCreated(it) }.also { setTag(id, it) }


val View.binding: BindingsCache
    get() = lazy(
        R.id.binding,
        create = { BindingsCache(this) },
        onCreated = ::addOnAttachStateChangeListener
    )

fun <V : View, DATA : Any> V.addBinding(flowable: Flowable<DATA>, setter: V.(DATA) -> Unit) {
    binding.addBinding(object : Binding<V, DATA>(flowable, this) {
        override fun V.set(data: DATA) = setter(data)
    })
}


// View

fun View.hideWhileLoading(loading: RxLoading) {
    addBinding(loading.observe()) { load ->
        visibility = if (load) View.VISIBLE else View.INVISIBLE
    }
}



// RecyclerView

fun <T : Any> RecyclerView.setItems(flowable: Flowable<List<T>>, bindings: ItemBindings) {
    addBinding(flowable) { items ->

        val connectedAdapter = getTag(R.id.recyclerAdapter) as? Adapter<T>

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

// TextView

fun <T : CharSequence> TextView.setText(flowable: Flowable<T>) = addBinding(flowable) { text = it }

fun TextView.setTextColor(flowable: Flowable<Int>) = addBinding(flowable) { setTextColor(it) }


// ImageView

fun ImageView.load(flowable: Flowable<Any>) {
    addBinding(flowable) { load(it) }
}


// EditText

val EditText.textWatcherDelegate: TextWatcherDelegate
    get() = lazy(R.id.textWatcher, ::TextWatcherDelegate, ::addTextChangedListener)

fun EditText.silent(action: EditText.() -> Unit) : EditText = apply {
    val delegate = textWatcherDelegate
    removeTextChangedListener(delegate)
    action()
    addTextChangedListener(delegate)
}

fun EditText.setText(text: Flowable<String>) {
    addBinding(text) {
        silent {
            setText(it)
            setSelection(it.length)
        }
    }
}

fun EditText.afterTextChanged(changes: FlowableProcessor<String>): EditText = apply {
    addBinding(Flowable.create<String>({
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun afterTextChanged(p0: Editable?) = it.onNext(p0?.toString().orEmpty())
        }
        textWatcherDelegate.addDelegate(textWatcher)
        it.setCancellable { textWatcherDelegate.removeDelegate(textWatcher) }
    }, BackpressureStrategy.LATEST), setter = { changes.onNext(it) })
}