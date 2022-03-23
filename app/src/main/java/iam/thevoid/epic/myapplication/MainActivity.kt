package iam.thevoid.epic.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleEmitter
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    val subject: Subject<String> = BehaviorSubject.create()

    private lateinit var editText: EditText
    private lateinit var recyclerView: RecyclerView

    private val adapter = StringsAdapter()

    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        .switchMapSingle {
            get("https://api.domainsdb.info/v1/domains/search?domain=${it}")
                .subscribeOn(Schedulers.io())
                .doOnError { Log.d(LOG_TAG, it.message.orEmpty()) }
                .retryWithDelay { it is HttpException }
                .map(::getDomainsList)

        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { domains ->
            adapter.setItems(domains)
        }

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



    private fun getDomainsList(json: String): List<String> {
        val root = JSONObject(json)
        val array = root.getJSONArray("domains")
        val domains: MutableList<JSONObject> = mutableListOf()
        for (i in 0 until array.length()) {
            domains.add(array.getJSONObject(i))
        }
        return domains.map { obj: JSONObject -> obj.getString("domain") }
    }

    private fun get(url: String): Single<String> {
        return Single.create { emitter: SingleEmitter<String> ->

            Log.d(LOG_TAG, "request $url started")

            val connection = (URL(url).openConnection() as HttpURLConnection).apply {
                setRequestProperty("User-Agent", "Mozilla/5.0")
                requestMethod = "GET"
            }

            try {
                val responseCode: Int = connection.responseCode

                if (responseCode in 200..299) {

                    var response = ""

                    BufferedReader(InputStreamReader(connection.inputStream)).let { reader ->

                        var line = ""
                        while (reader.readLine()?.also { line = it } != null) {
                            response += line
                            Log.d(LOG_TAG, line)
                        }
                    }
                    emitter.onSuccess(response)
                } else {
                    emitter.tryOnError(HttpException(responseCode))
                }
            } catch (e: Exception) {
                emitter.tryOnError(e)
            }
        }
    }


    companion object {
        private val LOG_TAG = MainActivity::class.simpleName
    }
}