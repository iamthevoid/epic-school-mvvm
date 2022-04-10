package iam.thevoid.epic.myapplication.ui.recycler

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import iam.thevoid.epic.myapplication.R
import iam.thevoid.epic.myapplication.data.network.country.CountriesClient
import iam.thevoid.epic.myapplication.data.model.Domain
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class DomainsAdapter : RecyclerView.Adapter<DomainsAdapter.StringsViewHolder>() {

    private val items: MutableList<Domain> = mutableListOf()

    fun setItems(newItems: List<Domain>) {
        DiffUtil.calculateDiff(DiffCallback(items, newItems)).also { result ->
            items.clear()
            items.addAll(newItems)
            result.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringsViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_domain, parent, false)
            .let(DomainsAdapter::StringsViewHolder)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: StringsViewHolder, position: Int) =
        holder.onBind(items[position])

    class StringsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var disposables = CompositeDisposable()

        private val textView: TextView = view.findViewById(R.id.text)
        private val imageView: ImageView = view.findViewById(R.id.flag)

        fun onBind(value: Domain) {
            textView.text = value.name
            textView.setTextColor(if (value.dead) Color.GRAY else Color.BLACK)

            disposables.clear()
            if (value.country == null) {
                imageView.setImageResource(R.drawable.ic_baseline_festival_24)
            } else {
                imageView.load(null)
                disposables.add(
                    CountriesClient.api.byCode(value.country)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            imageView.load(it.flag)
                        }, Throwable::printStackTrace)
                )
            }
        }
    }
}