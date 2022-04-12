package iam.thevoid.epic.myapplication.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import iam.thevoid.epic.myapplication.binding.RxLoading
import iam.thevoid.epic.myapplication.data.model.Country
import iam.thevoid.epic.myapplication.data.model.DomainsResponse
import iam.thevoid.epic.myapplication.data.network.country.CountriesClient
import iam.thevoid.epic.myapplication.data.network.domain.DomainsClient
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.processors.BehaviorProcessor
import io.reactivex.rxjava3.processors.FlowableProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

class DomainViewModel : ViewModel() {
    val input: FlowableProcessor<String> = BehaviorProcessor.create()

    val rxLoading = RxLoading()

    val items = input
        .subscribeOn(Schedulers.io())
        .debounce(500, TimeUnit.MILLISECONDS)
        .switchMapSingle { query ->
            DomainsClient.api.search(query)
                .subscribeOn(Schedulers.io())
                .compose(rxLoading.single())
                .retryWithDelay { it is HttpException }
        }
        .map(DomainsResponse::domains)

    fun flag(country: String): Single<Any> {
        return CountriesClient.api.byCode(country)
            .map<Any> { it.flag }
            .subscribeOn(Schedulers.io())
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