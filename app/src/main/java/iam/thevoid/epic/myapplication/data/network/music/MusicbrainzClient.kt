package iam.thevoid.epic.myapplication.data.network.music

import iam.thevoid.epic.myapplication.data.model.musicbrnz.ArtistsPage
import iam.thevoid.epic.myapplication.data.network.core.Client
import io.reactivex.rxjava3.core.Single

object MusicbrainzClient {

    const val PAGE_SIZE = 100

    private val client by lazy { Client("https://musicbrainz.org/", MusicbrainzApi::class) }

    private val api: MusicbrainzApi by lazy { client.api }

    fun getPage(query: String, offset: Int = 0): Single<ArtistsPage> = api.getArtists(
        mapOf(
            "fmt" to "json",
            "limit" to "$PAGE_SIZE",
            "query" to query,
            "offset" to "$offset"
        )
    )
}