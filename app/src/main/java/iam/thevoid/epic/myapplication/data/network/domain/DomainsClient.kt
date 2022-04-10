package iam.thevoid.epic.myapplication.data.network.domain

import iam.thevoid.epic.myapplication.data.network.core.Client

object DomainsClient {

    private val client by lazy { Client("https://api.domainsdb.info/", DomainsApi::class) }

    val api: DomainsApi by lazy { client.api }
}