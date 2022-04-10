package iam.thevoid.epic.myapplication.data.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class StringBooleanAdapter : TypeAdapter<Boolean>(){

    override fun write(out: JsonWriter?, value: Boolean?) {
        if (value == null) {
            out?.nullValue();
            return;
        }
        out?.value(if (value) "True" else "False");
    }

    override fun read(`in`: JsonReader?): Boolean? {
        if (`in`?.peek() == JsonToken.NULL) {
            `in`.nextNull()
            return null
        }
        return `in`?.nextString() == "True"
    }
}