package iam.thevoid.epic.myapplication.data.model

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import iam.thevoid.epic.myapplication.data.adapter.StringBooleanAdapter

class Domain(

    @SerializedName("domain")
    val name: String,

    @SerializedName("country")
    val country: String?,

    @JsonAdapter(StringBooleanAdapter::class)
    @SerializedName("isDead")
    val dead: Boolean
)