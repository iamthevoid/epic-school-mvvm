package iam.thevoid.epic.myapplication.presentation.ui.artist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import iam.thevoid.epic.myapplication.R
import iam.thevoid.epic.myapplication.databinding.FragmentArtistBinding
import iam.thevoid.epic.myapplication.presentation.ui.BaseFragment

class ArtistFragment : BaseFragment<FragmentArtistBinding>(R.layout.fragment_artist) {


    private val vm by activityViewModels<ArtistViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = vm
    }
}