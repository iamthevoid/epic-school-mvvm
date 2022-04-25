package iam.thevoid.epic.sandbox

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.util.*

@BindingAdapter("android:text")
fun setTime(textView: TextView, calendar: Calendar?) {
    calendar ?: return
    val hours = "${calendar.get(Calendar.HOUR_OF_DAY)}".padStart(2, '0')
    val minutes = "${calendar.get(Calendar.MINUTE)}".padStart(2, '0')
    val seconds = "${calendar.get(Calendar.SECOND)}".padStart(2, '0')
    val time = "$hours:$minutes:$seconds"
    textView.text = time
}