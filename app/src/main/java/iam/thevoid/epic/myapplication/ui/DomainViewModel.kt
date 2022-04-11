package iam.thevoid.epic.myapplication.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.jakewharton.rx3.replayingShare
import iam.thevoid.epic.myapplication.data.model.Country
import iam.thevoid.epic.myapplication.data.model.Domain
import iam.thevoid.epic.myapplication.data.model.DomainsResponse
import iam.thevoid.epic.myapplication.data.network.country.CountriesClient
import iam.thevoid.epic.myapplication.data.network.domain.DomainsClient
import iam.thevoid.epic.myapplication.databinding.utils.RxLoading
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.processors.BehaviorProcessor
import io.reactivex.rxjava3.processors.FlowableProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

class DomainViewModel : ViewModel() {

    companion object {
        private const val LOG_TAG = "DomainsViewModel"
    }

    private val flags = FlowableCache<Flowable<Domain>, String>()
    private val flagCache = mutableMapOf<String, String>()

    val loading: RxLoading = RxLoading()

    val searchString: FlowableProcessor<String> = BehaviorProcessor.create()

    val domains: Flowable<List<Domain>> = searchString
        .subscribeOn(Schedulers.io())
        .debounce(500, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .switchMapSingle { query ->
            DomainsClient.api.search(query)
                .compose(loading.single())
                .doOnError { Log.d(LOG_TAG, it.message.orEmpty()) }
                .retryWithDelay { it is HttpException }
        }
        .map(DomainsResponse::domains)
        .replayingShare(emptyList())


    fun flag(domain: Flowable<Domain>): Flowable<String> {
        return flags.get(domain) {
            domain.map { it.country.orEmpty() }
                .switchMapSingle { countryCode ->
                    val url : String? = if (countryCode.isBlank()) "" else flagCache[countryCode]
                    if (url != null) {
                        Single.just(url)
                    } else {
                        CountriesClient.api.byCode(countryCode)
                            .subscribeOn(Schedulers.io())
                            .map(Country::flag)
                            .doOnSuccess { flagCache[countryCode] = it }
                    }
                }
        }
    }

    fun fulfillInput(domainName: String) {
        searchString.onNext(domainName)
    }

    class FlowableCache<KEY, ITEM> {

        private val cache = mutableMapOf<KEY, Flowable<ITEM>>()

        fun get(key: KEY, flowable: () -> Flowable<ITEM>): Flowable<ITEM> {
            return cache[key] ?: flowable().also { cache[key] = it }
        }
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