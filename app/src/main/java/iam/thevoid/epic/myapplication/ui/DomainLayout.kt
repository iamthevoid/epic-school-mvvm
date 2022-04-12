package iam.thevoid.epic.myapplication.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import iam.thevoid.epic.myapplication.R
import iam.thevoid.epic.myapplication.binding.load
import iam.thevoid.epic.myapplication.binding.recycler.Layout
import iam.thevoid.epic.myapplication.binding.setText
import iam.thevoid.epic.myapplication.binding.setTextColor
import iam.thevoid.epic.myapplication.data.model.Domain
import io.reactivex.rxjava3.core.Single

class DomainLayout(private val vm: DomainViewModel, parent: ViewGroup) : Layout<Domain>(parent) {
    override fun createView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.item_domain, parent, false)
            .also { view ->
                view.findViewById<TextView>(R.id.text).apply {
                    setText(itemChanges.map { it.name })
                    setTextColor(itemChanges.map { if (it.dead) Color.GRAY else Color.BLACK })
                }
                view.findViewById<ImageView>(R.id.flag).load(
                    itemChanges.switchMapSingle<Any> {
                        if (it.country == null) {
                            Single.just(R.drawable.ic_baseline_festival_24)
                        } else {
                            vm.flag(it.country)
                        }
                    }
                )
            }
    }
}