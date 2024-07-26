package com.mnaufalhamdani.customselectdialog.domain

data class SingleSelectItemDomain(
    var codeOrId: String,
    var title: String,
    var message: String? = null,
    var index: Int = 0,//updated on Adapter
)