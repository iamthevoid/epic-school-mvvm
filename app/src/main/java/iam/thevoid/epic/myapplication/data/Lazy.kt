@file:JvmName("Lazy")

package iam.thevoid.epic.myapplication.data

import androidx.databinding.ObservableField
import iam.thevoid.epic.myapplication.data.model.musicbrnz.Artist
import iam.thevoid.epic.myapplication.data.network.country.CountriesClient
import iam.thevoid.epic.myapplication.presentation.ui.artist.ArtistViewModel
import iam.thevoid.epic.myapplication.presentation.ui.databinding.ObservableString
import iam.thevoid.epic.myapplication.presentation.ui.databinding.toBinding
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers

fun flag(artist: Artist): ObservableField<String> = Cache.lazy(artist.id) {
    artist.country?.let {
        Maybe.fromCallable<String> { Cache[artist.country] }
            .switchIfEmpty(
                CountriesClient.api.byCode(artist.country)
                    .subscribeOn(Schedulers.io())
                    .map { it.flag }
                    .onErrorReturnItem(ArtistViewModel.EMPTY_FLAG)
                    .doOnSuccess { Cache[artist.country] = it })
            .toBinding()
    } ?: ObservableString(ArtistViewModel.EMPTY_FLAG)
}
