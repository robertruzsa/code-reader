package com.robertruzsa.codereader.extensions

val Any.TAG: String get() = this::class.java.simpleName
