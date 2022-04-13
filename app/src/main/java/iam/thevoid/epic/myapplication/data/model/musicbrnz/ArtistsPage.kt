package iam.thevoid.epic.myapplication.data.model.musicbrnz

import kotlin.math.ceil

data class ArtistsPage(
    val artists: List<Artist> = emptyList(),
    val count: Int = 0,
    val offset: Int = 0
) {

    val isFirstPage: Boolean
        get() = offset == 0

    val pagesCount: Int
        get() = ceil(count.toFloat() / artists.size).toInt()
}