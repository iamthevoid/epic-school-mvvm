package iam.thevoid.epic.myapplication.ui

import android.graphics.Color.BLACK
import android.graphics.Color.GRAY
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import iam.thevoid.epic.myapplication.R
import iam.thevoid.epic.myapplication.data.model.Domain
import iam.thevoid.epic.myapplication.databinding.load
import iam.thevoid.epic.myapplication.databinding.recycler.Layout
import iam.thevoid.epic.myapplication.databinding.setText
import iam.thevoid.epic.myapplication.databinding.setTextColor

class DomainLayout(private val vm: DomainViewModel, parent: ViewGroup) : Layout<Domain>(parent) {

    override fun createView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.item_domain, parent, false)
            .also { view ->
                view.findViewById<TextView>(R.id.text).also { textView ->
                    textView.setText(itemChanges.map { it.name })
                    textView.setTextColor(itemChanges.map { if (it.dead) GRAY else BLACK })
                }
                view.findViewById<ImageView>(R.id.flag)
                    .load(vm.flag(itemChanges).map { it.ifEmpty { R.drawable.ic_baseline_festival_24 } })

                view.setOnClickListener {
                    vm.fulfillInput(item.name)
                }
            }
    }
}