package iam.thevoid.epic.myapplication.presentation.ui.databinding

import androidx.databinding.ObservableField

class ObservableString(initial: String = "") : ObservableField<String>() {

    private var value = initial.also { set(it) }

    override fun set(value: String?) {
        if (value != super.get()) {
            super.set(value)
        }
    }
}