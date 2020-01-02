package com.example.xposedtest.utility

import java.nio.ByteBuffer

class BlobParser {

  private lateinit var buffer: ByteBuffer

  constructor(input: ByteArray?) {
    if (check(input) != 0) {
      throw Exception("Invalid input")
    }
    buffer = ByteBuffer.wrap(input)
    buffer.position(1)
  }

  private fun check(input: ByteArray?): Int {
    if (input == null || input.isEmpty()) {
      return -1
    }
    if (input.first() != 123.toByte()) {
      return -2 // Invalid begin
    }
    if (input.last() != 125.toByte()) {
      return -3 // Invalid end
    }
    return 0
  }

  fun eob() = buffer.limit() - buffer.position() <= 1

  fun getInt() = buffer.int

  fun getLong() = buffer.long

  // fun getString(): String {
  //     val a = getBuffer()
  //     if (a.isEmpty())
  //         return ""
  //     return String(a)
  // }
  fun getString() = String(getBuffer())

  fun getBuffer(): ByteArray {
    val length = buffer.short
    if (length > 3072)
      throw Exception("Buffer String Length Error")
    if (length == 0.toShort())
      return ByteArray(0)
    val a = ByteArray(length.toInt())
    buffer.get(a, 0, length.toInt())
    return a
  }
}

fun BlobParser.int() = this.getInt()
fun BlobParser.long() = this.getLong()
fun BlobParser.string() = this.getString()
fun BlobParser.buffer() = this.getBuffer()
