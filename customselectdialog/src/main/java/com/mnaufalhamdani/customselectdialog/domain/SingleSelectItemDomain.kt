package com.mnaufalhamdani.customselectdialog.domain

data class SingleSelectItemDomain(
    var codeOrId: String,
    var message: String,
    var index: Int = 0,//updated on Adapter
)