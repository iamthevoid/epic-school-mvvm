package iam.thevoid.epic.myapplication.databinding.delegate

import android.text.Editable
import android.text.TextWatcher

class TextWatcherDelegate : TextWatcher {

    private val delegates: MutableSet<TextWatcher> = mutableSetOf()

    fun addDelegate(watcher: TextWatcher) {
        delegates.add(watcher)
    }

    fun removeDelegate(watcher: TextWatcher) {
        delegates.remove(watcher)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        delegates.forEach { it.beforeTextChanged(p0, p1, p2, p3) }
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        delegates.forEach { it.onTextChanged(p0, p1, p2, p3) }
    }

    override fun afterTextChanged(p0: Editable?) {
        delegates.forEach { it.afterTextChanged(p0) }
    }

}