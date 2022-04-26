package ru.rt.epic.sandbox.binding

import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import java.util.*

@BindingAdapter("app:time")
fun setTime(textView: TextView, calendar: Calendar?) {
    calendar ?: return
    val hours = "${calendar.get(Calendar.HOUR_OF_DAY)}".padStart(2, '0')
    val minutes = "${calendar.get(Calendar.MINUTE)}".padStart(2, '0')
    val seconds = "${calendar.get(Calendar.SECOND)}".padStart(2, '0')
    val time = "$hours:$minutes:$seconds"
    textView.text = time
}

@InverseBindingAdapter(attribute = "app:input", event = "app:onTextChanged")
fun getText(textView: EditText) : String {
   return textView.text.toString()
}