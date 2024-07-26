package com.mnaufalhamdani.customselectdialog.domain

data class MultipleSelectItemDomain(
    var codeOrId: String,
    var title: String,
    var message: String? = null,
    var index: Int = 0,//updated on Adapter
    var isChecked: Boolean = false,//updated on Adapter
)