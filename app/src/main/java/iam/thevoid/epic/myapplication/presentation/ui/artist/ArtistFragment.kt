package iam.thevoid.epic.myapplication.presentation.ui.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iam.thevoid.epic.myapplication.R
import iam.thevoid.epic.myapplication.databinding.FragmentArtistBinding
import iam.thevoid.epic.myapplication.presentation.ui.BaseFragment
import iam.thevoid.epic.myapplication.presentation.ui.detail.DetailsFragment

class ArtistFragment : BaseFragment() {

    private val domainsAdapter = ArtistAdapter {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, DetailsFragment.instance(it.name))
            .addToBackStack(null)
            .commit()
    }

    private lateinit var binding : FragmentArtistBinding

    private val pageAdapter by lazy { PageAdapter(binding.spinner.context) }


    val vm by activityViewModels<ArtistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vm.state.observe(requireActivity(), ::applyState)
        return FragmentArtistBinding.inflate(inflater).also {
            binding = it
            binding.vm = vm
        }.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spinnerButton.setOnClickListener { binding.spinner.performClick() }

        binding.spinner.adapter = pageAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                vm.onSelectPage(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) = Unit
        }

        binding.et.addTextChangedListener {
            val input = it?.toString().orEmpty()
            vm.onTextInput(input)
        }

        binding.recycler.layoutManager = LinearLayoutManager(requireActivity())
        binding.recycler.adapter = domainsAdapter
    }

    private fun applyState(state: ArtistViewState) {
//        if (binding.et.text.toString() != state.input) {
//            binding.et.setText(state.input)
//        }
        domainsAdapter.setItems(state.items)
        binding.spinner.visibility = if (state.pagesCount > 1) View.VISIBLE else View.INVISIBLE
        pageAdapter.clear()
        pageAdapter.addAll((1..state.pagesCount).map { "$it" })
        if (state.isFirstPage) {
            binding.spinner.setSelection(0)
        }
    }

    companion object {
        @BindingAdapter("app:visibleUnless")
        @JvmStatic
        fun goneUnless(view: View, loading: Boolean) {
            view.visibility = if (loading) View.VISIBLE else View.INVISIBLE
        }
    }
}