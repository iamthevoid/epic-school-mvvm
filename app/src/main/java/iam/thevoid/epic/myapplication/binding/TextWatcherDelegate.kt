package iam.thevoid.epic.myapplication.binding

import android.text.Editable
import android.text.TextWatcher

class TextWatcherDelegate: TextWatcher {

    private val watchers = mutableSetOf<TextWatcher>()

    fun addWatcher(watcher: TextWatcher) {
        watchers.add(watcher)
    }

    fun removeWatcher(watcher: TextWatcher) {
        watchers.remove(watcher)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        watchers.forEach { it.beforeTextChanged(p0, p1, p2, p3) }
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        watchers.forEach { it.onTextChanged(p0, p1, p2, p3) }
    }

    override fun afterTextChanged(p0: Editable?) {
        watchers.forEach { it.afterTextChanged(p0) }
    }
}