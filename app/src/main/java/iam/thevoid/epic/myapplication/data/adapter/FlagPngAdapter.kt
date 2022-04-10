package iam.thevoid.epic.myapplication.data.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class FlagPngAdapter : TypeAdapter<String?>(){

    override fun write(out: JsonWriter?, value: String?) {
        out?.beginObject()
            ?.name("png")?.value(value)
            ?.endObject()
    }

    override fun read(`in`: JsonReader?): String? {
        var png: String? = null
        `in`?.apply {
            beginObject()
            while (hasNext()) {
                when (nextName()) {
                    "svg" -> skipValue()
                    "png" -> png = nextString()
                }
            }
            endObject()
        }
        return png
    }
}