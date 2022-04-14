package iam.thevoid.epic.myapplication.presentation.ui.artist

import android.os.Handler
import android.os.Looper
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import iam.thevoid.epic.myapplication.data.model.musicbrnz.Artist
import iam.thevoid.epic.myapplication.data.model.musicbrnz.ArtistsPage
import iam.thevoid.epic.myapplication.data.network.country.CountriesClient
import iam.thevoid.epic.myapplication.data.network.music.MusicbrainzClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class ArtistViewModel : ViewModel() {

    private val _liveData: MutableLiveData<ArtistViewState> = MutableLiveData(ArtistViewState())

    val state: LiveData<ArtistViewState> = _liveData

    private val subject: BehaviorSubject<String> = BehaviorSubject.create()

    private val disposable = CompositeDisposable()

    val loading: ObservableBoolean = ObservableBoolean(false)

    private val getArtistSubscription = subject
        .subscribeOn(Schedulers.io())
        .filter { it.isNotBlank() }
        .debounce(500, TimeUnit.MILLISECONDS)
        .switchMapSingle { query ->
            if (query.isBlank()) Single.just(ArtistsPage()) else requestPage(query)
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(::onResponse, Throwable::printStackTrace)
        .also { disposable.add(it) }

    /**
     * _______________
     */

    /**
     * ACTIONS
     */

    fun onTextInput(text: String) {
        updateState { it.copy(input = text) }
        subject.onNext(text)
    }

    fun onSelectPage(page: Int) {
        val query = subject.value ?: return
        requestPage(query, page)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onResponse, Throwable::printStackTrace)
    }

    /**
     * _______________
     */


    private val flags = mutableMapOf<String, String>()

    private fun onResponse(page: ArtistsPage) {
        updateState {
            it.copy(
                items = page.artists.map { ArtistData(it, flags[it.country]) },
                pagesCount = page.pagesCount,
                isFirstPage = page.isFirstPage
            )
        }

        val countries = page.artists.mapNotNull(Artist::country)
        if (!countries.all(flags.keys::contains)) {
            requestFlags(countries - flags.keys)
        }
    }

    private fun requestFlags(countries: List<String>) {
        Observable.fromIterable(countries)
            .flatMapSingle { country ->
                CountriesClient.api.byCode(country)
                    .subscribeOn(Schedulers.io())
                    .map { Pair(country, it.flag) }
            }
            .toList()
            .subscribe({
                it.forEach { (country, flag) ->
                    flags[country] = flag
                }
                updateState { viewState ->
                    viewState.copy(
                        items = viewState.items.map { data ->
                            data.copy(flag = flags[data.artist.country])
                        }
                    )
                }
            }, Throwable::printStackTrace)
    }

    private fun requestPage(query: String, page: Int = 1): Single<ArtistsPage> {
        return MusicbrainzClient.getPage(query, offset = (page - 1) * MusicbrainzClient.PAGE_SIZE)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { Handler(Looper.getMainLooper()).post { loading.set(true) } }
            .doOnEvent { _, _ -> Handler(Looper.getMainLooper()).post { loading.set(false) } }
            .retryWithDelay()
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

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    private fun updateState(update: (ArtistViewState) -> ArtistViewState) {
        Handler(Looper.getMainLooper()).post {
            _liveData.value?.also {
                _liveData.value = update(it)
            }
        }
    }
}