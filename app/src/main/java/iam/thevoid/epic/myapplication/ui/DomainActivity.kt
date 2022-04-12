package iam.thevoid.epic.myapplication.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iam.thevoid.epic.myapplication.R
import iam.thevoid.epic.myapplication.binding.afterTextChanged
import iam.thevoid.epic.myapplication.binding.setItems
import iam.thevoid.epic.myapplication.binding.showWhileLoading


class DomainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val vm = ViewModelProvider(this)[DomainViewModel::class.java]
        findViewById<View>(R.id.loading).showWhileLoading(vm.rxLoading)
        findViewById<EditText>(R.id.et).afterTextChanged(vm.input)
        findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = LinearLayoutManager(this@DomainActivity)
            setItems(vm.items, domainBinding(vm))
        }
    }
}