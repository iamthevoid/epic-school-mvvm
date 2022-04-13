package iam.thevoid.epic.myapplication.presentation.ui.artist

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iam.thevoid.epic.myapplication.R
import iam.thevoid.epic.myapplication.data.model.musicbrnz.ArtistsPage
import iam.thevoid.epic.myapplication.data.network.music.MusicbrainzClient
import iam.thevoid.epic.myapplication.presentation.ui.MainActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class ArtistFragment: Fragment(R.layout.fragment_artist) {
    private val subject: BehaviorSubject<String> = BehaviorSubject.create()

    private lateinit var editText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var loading: View
    private lateinit var spinnerButton: View
    private lateinit var spinner: Spinner

    private val domainsAdapter = ArtistAdapter()

    private val pageAdapter by lazy { PageAdapter(spinner.context) }

    var disposable: Disposable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading = view.findViewById(R.id.loading)

        spinnerButton = view.findViewById(R.id.spinner_button)
        spinnerButton.setOnClickListener { spinner.performClick() }

        spinner = view.findViewById(R.id.spinner)
        spinner.adapter = pageAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val page = pageAdapter.getItem(p2) ?: return
                val query = subject.value ?: return
                requestPage(query, page.toInt())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(::onResponse, Throwable::printStackTrace)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) = Unit
        }

        editText = view.findViewById(R.id.et)
        editText.addTextChangedListener {
            val input = it?.toString().orEmpty()
            subject.onNext(input)
        }

        recyclerView = view.findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = domainsAdapter
    }


    override fun onStop() {
        super.onStop()
        disposable?.dispose()
        disposable = null
    }

    override fun onStart() {
        super.onStart()
        disposable = subscribeInput()
    }

    private fun subscribeInput() = subject
        .subscribeOn(Schedulers.io())
        .debounce(500, TimeUnit.MILLISECONDS)
        .switchMapSingle { query ->
            if (query.isBlank()) Single.just(ArtistsPage()) else requestPage(query)
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(::onResponse)

    private fun requestPage(query: String, page: Int = 1): Single<ArtistsPage> {
        return MusicbrainzClient.getPage(query, offset = (page - 1) * MusicbrainzClient.PAGE_SIZE)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { activity?.runOnUiThread { loading.visibility = View.VISIBLE } }
            .doOnEvent { _, _ -> activity?.runOnUiThread { loading.visibility = View.INVISIBLE } }
            .doOnError { Log.d(LOG_TAG, it.message.orEmpty()) }
            .retryWithDelay()
    }

    private fun onResponse(response: ArtistsPage) {
        domainsAdapter.setItems(response.artists)
        spinnerButton.visibility = if (response.pagesCount > 1) View.VISIBLE else View.INVISIBLE
        pageAdapter.clear()
        pageAdapter.addAll((1..response.pagesCount).map { "$it" })
        if (response.isFirstPage) {
            spinner.setSelection(0)
        }
    }

    private fun <T : Any> Single<T>.retryWithDelay(
        delay: Long = 2000L,
        filter: (Throwable) -> Boolean = { true }
    ): Single<T> {
        return retryWhen { errorThrowable ->
            errorThrowable.flatMap {
                if (filter(it)) {
                    Flowable.timer(delay, TimeUnit.MILLISECONDS)
                } else {
                    Flowable.error(it)
                }
            }
        }
    }


    companion object {
        private val LOG_TAG = MainActivity::class.simpleName
    }
}