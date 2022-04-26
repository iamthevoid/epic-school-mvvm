package ru.rt.epic.sandbox

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import io.reactivex.rxjava3.core.Flowable
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.collections.AsyncDiffObservableList
import ru.rt.epic.sandbox.binding.ObservableString
import ru.rt.epic.sandbox.binding.toBinding
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class DemoViewModel: ViewModel() {

    val binding = ItemBinding.of<Int>(BR.number, R.layout.item_number)

    val input: ObservableString = ObservableString("ttt")

    val snapshot: ObservableString = ObservableString("")

    val list = AsyncDiffObservableList(object : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    })

    val text = Flowable.interval(10, TimeUnit.MILLISECONDS)
        .map {
            Calendar.getInstance().apply {
                time = Date()
                set(Calendar.MILLISECOND, 0)
            }
        }
        .distinctUntilChanged()
        .toBinding()

    fun addItem() {
        list.update(list + if (list.isEmpty()) 0 else Random.nextInt() % list.size)
    }

}