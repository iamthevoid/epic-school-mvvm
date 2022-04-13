package iam.thevoid.epic.myapplication.data.network.music

import iam.thevoid.epic.myapplication.data.model.musicbrnz.ArtistsPage
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface MusicbrainzApi {

    @GET("/ws/2/artist")
    @Headers("User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64)")
    fun getArtists(@QueryMap query: Map<String, String>) : Single<ArtistsPage>

}