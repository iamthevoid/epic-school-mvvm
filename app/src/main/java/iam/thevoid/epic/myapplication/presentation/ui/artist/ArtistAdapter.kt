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

class ArtistAdapter(private val onItemClick: (Artist) -> Unit) : RecyclerView.Adapter<ArtistAdapter.StringsViewHolder>() {

    private val items: MutableList<ArtistData> = mutableListOf()

    fun setItems(newItems: List<ArtistData>) {
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
        holder.onBind(items[position], onItemClick)




    class StringsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val title: TextView = view.findViewById(R.id.title)
        private val subtitle: TextView = view.findViewById(R.id.subtitle)
        private val imageView: ImageView = view.findViewById(R.id.flag)

        fun onBind(item: ArtistData, onItemClick: (Artist) -> Unit) {
            itemView.setOnClickListener { onItemClick(item.artist) }
            title.text = item.artist.name
            subtitle.text = item.artist.disambiguation ?: "---"
            imageView.load(item.flag ?: R.drawable.ic_baseline_festival_24)
        }
    }
}