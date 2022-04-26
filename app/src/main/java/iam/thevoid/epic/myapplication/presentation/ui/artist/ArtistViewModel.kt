package iam.thevoid.epic.myapplication.presentation.ui.artist

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import iam.thevoid.epic.myapplication.BR
import iam.thevoid.epic.myapplication.R
import iam.thevoid.epic.myapplication.data.Cache
import iam.thevoid.epic.myapplication.data.model.musicbrnz.Artist
import iam.thevoid.epic.myapplication.data.model.musicbrnz.ArtistsPage
import iam.thevoid.epic.myapplication.data.network.country.CountriesClient
import iam.thevoid.epic.myapplication.data.network.music.MusicbrainzClient
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import me.tatarka.bindingcollectionadapter2.ItemBinding
import ru.rt.epic.sandbox.binding.*
import java.util.concurrent.TimeUnit

class ArtistViewModel : ViewModel() {

    val binding = ItemBinding.of<Artist>(BR.artist, R.layout.item_artist)
        .bindExtra(BR.vm, this)

    val query = ObservableString("")

    val list = query.toRx()
        .subscribeOn(Schedulers.io())
        .filter { it.isNotBlank() }
        .debounce(500, TimeUnit.MILLISECONDS)
        .switchMapSingle { query ->
            if (query.isBlank()) Single.just(ArtistsPage()) else requestPage(query)
        }.map { it.artists }
        .toListBinding()


    val loading: ObservableBoolean = ObservableBoolean(false)

    private fun requestPage(query: String, page: Int = 1): Single<ArtistsPage> {
        return MusicbrainzClient.getPage(query, offset = (page - 1) * MusicbrainzClient.PAGE_SIZE)
            .subscribeOn(Schedulers.io())
            .loading(loading)
            .retryWithDelay()
    }


    fun flag(artist: Artist) : RxObservableField<String> = Cache.lazy(artist.id) {
        artist.country?.let { country ->
            Maybe.fromCallable<String> { Cache.get(country) }
                .switchIfEmpty(
                    CountriesClient.api.byCode(country)
                        .subscribeOn(Schedulers.io())
                        .map { it.flag }
                        .doOnSuccess { Cache.set<String>(country, it) }
                ).toBinding()
        } ?: Single.just(EMPTY_FLAG).toBinding()
    }

    companion object {
        const val EMPTY_FLAG = "---"
    }

    private fun <T : Any> Single<T>.retryWithDelay(
        delay: Long = 2000L,
        filter: (Throwable) -> Boolean = { true }
    ): Single<T> {
        return retryWhen { errorThrowable ->
            errorThrowable.flatMap {
                if (filter(it)) {
                    Flowable.timer(delay, TimeUnit.MILLISECONDS)
                } else {
                    Flowable.error(it)
                }
            }
        }
    }
}