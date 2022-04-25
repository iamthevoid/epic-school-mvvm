package iam.thevoid.epic.sandbox

import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.adapters.TextViewBindingAdapter

@BindingAdapter("android:text")
fun setText(editText: EditText, text: String) {
    TextViewBindingAdapter.setText(editText, text)
//    editText.setText(text)
}

@InverseBindingAdapter(attribute = "android:text")
fun getText(editText: EditText): String {
    return editText.text?.toString().orEmpty()
}