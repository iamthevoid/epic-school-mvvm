package iam.thevoid.epic.myapplication.data.network.country

import iam.thevoid.epic.myapplication.data.model.Country
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface CountriesApi {

    @GET("/v2/alpha/{code}")
    fun byCode(@Path("code") code: String): Single<Country>
}