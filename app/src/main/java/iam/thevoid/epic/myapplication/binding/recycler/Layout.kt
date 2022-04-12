package iam.thevoid.epic.myapplication.binding.recycler

import android.view.View
import android.view.ViewGroup
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.BehaviorProcessor

abstract class Layout<T : Any>(parent: ViewGroup) {

    private val _data: BehaviorProcessor<T> = BehaviorProcessor.create()

    val itemChanges: Flowable<T> = _data

    val item by lazy { _data.value!! }

    abstract fun createView(parent: ViewGroup): View

    val view: View by lazy { createView(parent) }

    fun notifyChanges(data: T) {
        _data.onNext(data)
    }
}