package iam.thevoid.epic.myapplication.presentation.ui.artist

import android.os.Bundle
import android.view.View
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
import iam.thevoid.epic.myapplication.presentation.ui.BaseFragment
import iam.thevoid.epic.myapplication.presentation.ui.detail.DetailsFragment

class ArtistFragment : BaseFragment(R.layout.fragment_artist) {


    private lateinit var editText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var loading: View
    private lateinit var spinnerButton: View
    private lateinit var spinner: Spinner

    private val domainsAdapter = ArtistAdapter {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, DetailsFragment.instance(it.name))
            .addToBackStack(null)
            .commit()
    }

    private val pageAdapter by lazy { PageAdapter(spinner.context) }


    val vm by activityViewModels<ArtistViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.state.observe(requireActivity(), ::applyState)

        loading = view.findViewById(R.id.loading)

        spinnerButton = view.findViewById(R.id.spinner_button)
        spinnerButton.setOnClickListener { spinner.performClick() }

        spinner = view.findViewById(R.id.spinner)
        spinner.adapter = pageAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                vm.onSelectPage(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) = Unit
        }

        editText = view.findViewById(R.id.et)
        editText.addTextChangedListener {
            val input = it?.toString().orEmpty()
            vm.onTextInput(input)
        }

        recyclerView = view.findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = domainsAdapter
    }

    private fun applyState(state: ArtistViewState) {
        if (editText.text.toString() != state.input) {
            editText.setText(state.input)
        }
        domainsAdapter.setItems(state.items)
        spinnerButton.visibility = if (state.pagesCount > 1) View.VISIBLE else View.INVISIBLE
        pageAdapter.clear()
        pageAdapter.addAll((1..state.pagesCount).map { "$it" })
        if (state.isFirstPage) {
            spinner.setSelection(0)
        }
//        loading.visibility = if (state.loading) View.VISIBLE else View.INVISIBLE
    }

    @BindingAdapter("app:visibleUnless")
    fun goneUnless(view: View, loading: Boolean) {
        view.visibility = if (loading) View.VISIBLE else View.INVISIBLE
    }
}