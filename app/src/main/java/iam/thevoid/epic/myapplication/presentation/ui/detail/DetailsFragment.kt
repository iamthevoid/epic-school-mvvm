package iam.thevoid.epic.myapplication.presentation.ui.detail

import android.os.Bundle
import android.view.View
import iam.thevoid.epic.myapplication.R
import iam.thevoid.epic.myapplication.databinding.FragmentDetailsBinding
import iam.thevoid.epic.myapplication.presentation.ui.BaseFragment

class DetailsFragment: BaseFragment(R.layout.fragment_details) {

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