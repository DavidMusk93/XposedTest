package com.example.xposedtest.extension

import com.example.xposedtest.utility.StringUtils


fun ByteArray.toS() = StringUtils.byteArrayToString(this)

fun ByteArray.toHexList(): List<String> {
  val list = mutableListOf<String>()
  this.forEach {
    list.add("0x%02X".format(it))
  }
  return list
}