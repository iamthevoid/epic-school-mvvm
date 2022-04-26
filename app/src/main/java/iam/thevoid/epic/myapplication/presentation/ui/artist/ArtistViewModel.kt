package iam.thevoid.epic.myapplication.presentation.ui.artist

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import iam.thevoid.epic.myapplication.BR
import iam.thevoid.epic.myapplication.R
import iam.thevoid.epic.myapplication.data.model.musicbrnz.Artist
import iam.thevoid.epic.myapplication.data.model.musicbrnz.ArtistsPage
import iam.thevoid.epic.myapplication.data.network.music.MusicbrainzClient
import iam.thevoid.epic.myapplication.data.retryWithDelay
import iam.thevoid.epic.myapplication.presentation.ui.databinding.ObservableString
import iam.thevoid.epic.myapplication.presentation.ui.databinding.loading
import iam.thevoid.epic.myapplication.presentation.ui.databinding.toBinding
import iam.thevoid.epic.myapplication.presentation.ui.databinding.toRx
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.util.concurrent.TimeUnit

class ArtistViewModel : ViewModel() {

    companion object {
        const val EMPTY_ARTIST = "---"
        const val EMPTY_FLAG = "---"
    }

    val itemBinding = ItemBinding.of<Artist>(BR.artist, R.layout.item_artist)
        .bindExtra(BR.vm, this)

    val searchQuery = ObservableString("")

    val loading: ObservableBoolean = ObservableBoolean(false)

    val items = searchQuery.toRx()
        .subscribeOn(Schedulers.io())
        .debounce(500, TimeUnit.MILLISECONDS).switchMapSingle { query ->
        if (query.isBlank()) Single.just(ArtistsPage()) else requestPage(query)
    }.map { it.artists }
        .toBinding()

    private fun requestPage(query: String, page: Int = 1): Single<ArtistsPage> {
        return MusicbrainzClient.getPage(query, offset = (page - 1) * MusicbrainzClient.PAGE_SIZE)
            .subscribeOn(Schedulers.io())
            .loading(loading)
            .retryWithDelay()
    }
}