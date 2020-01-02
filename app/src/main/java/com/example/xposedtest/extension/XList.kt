package com.example.xposedtest.extension

fun List<Int>.toByteArray(): ByteArray {
  return ByteArray(size) {
    this[it].toByte()
  }
}