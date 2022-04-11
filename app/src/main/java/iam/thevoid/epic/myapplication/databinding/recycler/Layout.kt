package iam.thevoid.epic.myapplication.databinding.recycler

import android.view.View
import android.view.ViewGroup
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.BehaviorProcessor

abstract class Layout<DATA : Any>(parent: ViewGroup) {

    abstract fun createView(parent: ViewGroup): View

    private val _value = BehaviorProcessor.create<DATA>()

    val view by lazy { createView(parent) }

    val itemChanges: Flowable<DATA> = _value

    val item by lazy { _value.value!! }

    fun set(data: DATA) {
        _value.onNext(data)
    }
}