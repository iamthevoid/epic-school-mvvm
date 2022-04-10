package iam.thevoid.epic.myapplication.data.network.domain

import iam.thevoid.epic.myapplication.data.model.DomainsResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface DomainsApi {
    @GET("/v1/domains/search")
    fun search(@Query("domain") domain: String): Single<DomainsResponse>
}