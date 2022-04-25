package iam.thevoid.epic.sandbox

import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.OnItemBind
import me.tatarka.bindingcollectionadapter2.collections.AsyncDiffObservableList
import kotlin.random.Random

class MainViewModel : ViewModel() {

//    val caught = ObservableField<Calendar>()
//
//    val title by lazy {
//        Flowable.interval(10, TimeUnit.MILLISECONDS)
//            .map {
//                Calendar.getInstance().apply {
//                    timeInMillis = System.currentTimeMillis()
//                    set(Calendar.MILLISECOND, 0)
//                }
//            }
//            .distinctUntilChanged()
//            .toBinding()
//    }

//    val text = ObservableString("Input")

//    fun catchTime() {
//        title.get()?.also(caught::set)
//    }

    val items = AsyncDiffObservableList(object : DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return areContentsTheSame(oldItem, newItem)
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    })

    val bindings = ItemBinding.of(OnItemBind<String> { itemBinding, position, item ->
        itemBinding.set(BR.item, if (position % 2 == 0) R.layout.list_item else R.layout.list_item_right)
            .bindExtra(BR.item, if (position % 2 == 0) item else item?.reversed())
    })

    fun addItem() {
        val nextInt = Random.nextInt(items.size + 1)
        val newItems = items.toMutableList().apply { add(nextInt, "Item $nextInt") }
        items.update(newItems)
    }
}