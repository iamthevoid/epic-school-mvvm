package iam.thevoid.epic.myapplication.presentation.ui.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import iam.thevoid.epic.myapplication.R
import iam.thevoid.epic.myapplication.databinding.FragmentArtistBinding
import iam.thevoid.epic.myapplication.presentation.ui.detail.DetailsFragment
import ru.rt.epic.sandbox.base.BaseFragment

class ArtistFragment : BaseFragment<FragmentArtistBinding>(R.layout.fragment_artist) {

    private val vm by viewModels<ArtistViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = vm
    }

}