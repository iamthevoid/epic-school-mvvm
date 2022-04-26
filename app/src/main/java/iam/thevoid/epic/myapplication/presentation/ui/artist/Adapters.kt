package iam.thevoid.epic.myapplication.presentation.ui.artist

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import iam.thevoid.epic.myapplication.R

@BindingAdapter("app:imageUrl")
fun setImage(imageView: ImageView, url: String?) {
    url ?: return
    if (url == ArtistViewModel.EMPTY_FLAG) {
        imageView.load(R.drawable.ic_baseline_festival_24)
    } else {
        imageView.load(url)
    }
}