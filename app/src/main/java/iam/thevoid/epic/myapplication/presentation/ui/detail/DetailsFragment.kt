package iam.thevoid.epic.myapplication.presentation.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import iam.thevoid.epic.myapplication.R
import iam.thevoid.epic.myapplication.databinding.FragmentDetailsBinding

class DetailsFragment: Fragment() {

    companion object {

        private const val NAME_KEY = "DetailsFragment.NAME_KEY"

        fun instance(name: String) = DetailsFragment().apply {
            arguments = Bundle().apply {
                putString(NAME_KEY, name)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDetailsBinding.bind(view)
        binding.name = requireArguments().getString(NAME_KEY)
    }
}