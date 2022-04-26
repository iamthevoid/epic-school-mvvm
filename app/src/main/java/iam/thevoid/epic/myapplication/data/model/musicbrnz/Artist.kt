package iam.thevoid.epic.myapplication.data.model.musicbrnz

data class Artist(
    val id: String,
    val name: String,
    val type: String,
    val disambiguation: String?,
    val country: String?
)