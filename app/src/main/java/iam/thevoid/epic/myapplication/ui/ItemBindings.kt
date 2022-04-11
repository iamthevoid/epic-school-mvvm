package iam.thevoid.epic.myapplication.ui

import iam.thevoid.epic.myapplication.data.model.Domain
import iam.thevoid.epic.myapplication.databinding.recycler.ItemBindings

fun domainsBindings(viewModel: DomainViewModel): ItemBindings {
    return ItemBindings.of(Domain::class, factory = { parent -> DomainLayout(viewModel, parent) })
}
