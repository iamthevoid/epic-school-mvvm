package iam.thevoid.epic.myapplication.data.network.country

import iam.thevoid.epic.myapplication.data.network.core.Client

object CountriesClient {

    private val client by lazy { Client("https://restcountries.com/", CountriesApi::class) }

    val api: CountriesApi by lazy { client.api }
}