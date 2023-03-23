package com.mnaufalhamdani.customselectdialog.domain

data class MultipleSelectItemDomain(
    var codeOrId: String,
    var message: String,
    var index: Int = 0,//updated on Adapter
    var isChecked: Boolean = false,//updated on Adapter
)