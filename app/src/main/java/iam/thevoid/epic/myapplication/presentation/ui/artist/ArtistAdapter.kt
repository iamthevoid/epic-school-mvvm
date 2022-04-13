package iam.thevoid.epic.myapplication.presentation.ui.artist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import iam.thevoid.epic.myapplication.R
import iam.thevoid.epic.myapplication.data.model.musicbrnz.Artist
import iam.thevoid.epic.myapplication.data.network.country.CountriesClient
import iam.thevoid.epic.myapplication.presentation.util.DiffCallback
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ArtistAdapter : RecyclerView.Adapter<ArtistAdapter.StringsViewHolder>() {

    private val items: MutableList<Artist> = mutableListOf()

    private val flags = mutableMapOf<String, String>()

    fun setItems(newItems: List<Artist>) {
        DiffUtil.calculateDiff(DiffCallback(items, newItems)).also { result ->
            items.clear()
            items.addAll(newItems)
            result.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringsViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artist, parent, false)
            .let(ArtistAdapter::StringsViewHolder)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: StringsViewHolder, position: Int) =
        holder.onBind(
            cache = flags,
            item = items[position],
            onFlagLoad = { country, flag -> flags[country] = flag }
        )

    class StringsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var disposables = CompositeDisposable()

        private val title: TextView = view.findViewById(R.id.title)
        private val subtitle: TextView = view.findViewById(R.id.subtitle)
        private val imageView: ImageView = view.findViewById(R.id.flag)

        fun onBind(
            cache: Map<String, String>,
            item: Artist,
            onFlagLoad: (String, String) -> Unit
        ) {
            title.text = item.name
            subtitle.text = item.disambiguation ?: "---"

            imageView.load(null)
            cache[item.country]?.also {
                imageView.load(it)
            } ?: kotlin.run {
                disposables.clear()
                disposables.add(
                    CountriesClient.api.byCode(item.country)
                        .subscribeOn(Schedulers.io())
                        .map { it.flag }
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess { onFlagLoad(item.country, it) }
                        .map<Any> { it }
                        .onErrorReturnItem(R.drawable.ic_baseline_festival_24)
                        .subscribe(imageView::load, Throwable::printStackTrace)
                )
            }
        }
    }
}