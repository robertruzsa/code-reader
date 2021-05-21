package com.robertruzsa.codereaderview

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE

val Any.TAG: String get() = this::class.java.simpleName

fun View.visible() {
    this.visibility = VISIBLE
}

fun View.gone() {
    this.visibility = GONE
}

fun View.visibleIf(condition: Boolean) {
    if (condition) visible() else gone()
}
