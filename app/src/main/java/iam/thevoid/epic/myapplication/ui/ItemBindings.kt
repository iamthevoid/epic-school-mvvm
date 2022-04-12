package iam.thevoid.epic.myapplication.ui

import iam.thevoid.epic.myapplication.binding.recycler.ItemBindings
import iam.thevoid.epic.myapplication.data.model.Domain

fun domainBinding(vm: DomainViewModel) =
    ItemBindings.of(Domain::class, factory = { parent ->
        DomainLayout(vm, parent)
    })