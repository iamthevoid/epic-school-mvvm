package iam.thevoid.epic.myapplication.presentation.ui.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import iam.thevoid.epic.myapplication.R
import iam.thevoid.epic.myapplication.presentation.ui.artist.ArtistViewModel

@BindingAdapter("app:asyncImage")
fun setImage(imageView: ImageView, any: String?) {
    imageView.load(any.takeIf { it != ArtistViewModel.EMPTY_FLAG } ?: R.drawable.ic_baseline_festival_24)
}