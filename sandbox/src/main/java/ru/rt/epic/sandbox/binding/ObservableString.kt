package ru.rt.epic.sandbox.binding

import androidx.databinding.ObservableField

class ObservableString(default: String = ""): ObservableField<String>() {

    var value = default.also { set(it) }

    override fun set(value: String?) {
        if (value != this.value) {
            super.set(value)
        }
    }
}