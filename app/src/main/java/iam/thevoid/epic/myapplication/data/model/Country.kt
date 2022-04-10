package iam.thevoid.epic.myapplication.data.model

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import iam.thevoid.epic.myapplication.data.adapter.FlagPngAdapter

data class Country(

    @SerializedName("name")
    val name: String,

    @JsonAdapter(FlagPngAdapter::class)
    @SerializedName("flags")
    val flag: String
)