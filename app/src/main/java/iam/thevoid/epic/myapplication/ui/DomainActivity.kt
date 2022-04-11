package iam.thevoid.epic.myapplication.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iam.thevoid.epic.myapplication.R
import iam.thevoid.epic.myapplication.databinding.afterTextChanged
import iam.thevoid.epic.myapplication.databinding.hideWhileLoading
import iam.thevoid.epic.myapplication.databinding.setItems
import iam.thevoid.epic.myapplication.databinding.setText


class DomainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val vm: DomainViewModel = ViewModelProvider(this)[DomainViewModel::class.java]

        findViewById<View>(R.id.loading)
            .hideWhileLoading(vm.loading)

        findViewById<EditText>(R.id.et)
            .afterTextChanged(vm.searchString)
            .setText(vm.searchString)

        findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = LinearLayoutManager(this@DomainActivity)
            setItems(vm.domains, domainsBindings(vm))
        }
    }
}