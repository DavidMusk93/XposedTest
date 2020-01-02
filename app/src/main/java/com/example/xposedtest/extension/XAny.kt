package com.example.xposedtest.extension

inline fun <reified T> Any?.cast(): T? {
    if (this is T)
        return this
    return null
}