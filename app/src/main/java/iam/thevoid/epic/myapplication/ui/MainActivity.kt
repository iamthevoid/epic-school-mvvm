package iam.thevoid.epic.myapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iam.thevoid.epic.myapplication.R
import iam.thevoid.epic.myapplication.data.network.domain.DomainsClient
import iam.thevoid.epic.myapplication.data.model.DomainsResponse
import iam.thevoid.epic.myapplication.ui.recycler.DomainsAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject
import retrofit2.HttpException
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private val subject: Subject<String> = BehaviorSubject.create()

    private lateinit var editText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var loading: View

    private val adapter = DomainsAdapter()

    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loading = findViewById(R.id.loading)

        editText = findViewById(R.id.et)
        editText.addTextChangedListener {
            val input = it?.toString().orEmpty()
            subject.onNext(input)
        }

        recyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
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
            DomainsClient.api.search(query)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { runOnUiThread { loading.visibility = View.VISIBLE } }
                .doOnEvent { _, _ -> runOnUiThread { loading.visibility = View.INVISIBLE } }
                .doOnError { Log.d(LOG_TAG, it.message.orEmpty()) }
                .retryWithDelay { it is HttpException }
        }
        .map(DomainsResponse::domains)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(adapter::setItems)



    private fun <T : Any> Single<T>.retryWithDelay(
        delay: Long = 2000L,
        filter: (Throwable) -> Boolean = { true }
    ) : Single<T> {
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