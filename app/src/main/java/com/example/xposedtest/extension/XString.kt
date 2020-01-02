package com.example.xposedtest.extension

import android.content.Context
import android.widget.Toast
import com.example.xposedtest.utility.HashHelper
import com.example.xposedtest.xposed.gLog
import java.io.File
import java.io.IOException
import java.io.ObjectInputStream
import java.util.regex.Pattern

fun String.basename(): String? {
    val i = this.lastIndexOf('/')
    if (i != -1) {
        return this.substring(i + 1)
    }
    return null
}

fun String.dirname(): String? {
    val i = this.lastIndexOf('/')
    if (i != -1) {
        return this.substring(0, i)
    }
    return null
}

fun String.toast(context: Context) = Toast.makeText(context, this, Toast.LENGTH_SHORT)

@Throws(IOException::class)
fun <T, R> String.loadMap(): Map<T, R>? {
    File(this)
        .let {
            if (it.exists()) {
                val fis = it.inputStream()
                val ois = ObjectInputStream(fis)
                return ois.readObject().cast<Map<T, R>>()
                    ?.also {
                        it.forEach { k, v ->
                            gLog("@loadMap", "$k:$v")
                        }
                    }
            }
        }
    return null
}


fun String.parseGroup(pattern: String): String? {
    val matcher = Pattern.compile(pattern).matcher(this)
    if (matcher.find()) {
        return matcher.group(1)
    }
    return null
}


fun String.md5(): String {
    HashHelper.getDigest("md5").apply {
        update(this@md5.toByteArray())
        return digest().toS()
    }
}

fun String.sha256(): String {
    HashHelper.getDigest("sha-256").apply {
        update(this@sha256.toByteArray())
        return digest().toS()
    }
}