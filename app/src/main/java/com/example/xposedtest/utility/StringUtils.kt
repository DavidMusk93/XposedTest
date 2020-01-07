package com.example.xposedtest.utility


object StringUtils {

  object Const {
    // val charSet = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
    val byteSet = "0123456789abcdef".toByteArray()
  }

  fun byteArrayToString(input: ByteArray): String {
    var a = 0
    var b = 0
    val output = ByteArray(input.size.shl(1))
    while (a < input.size) {
      val byte = input[a++]
      // log("@Byte", byte)
      output[b++] = Const.byteSet[byte.toInt().ushr(4).and(15)]
      output[b++] = Const.byteSet[byte.toInt().and(15)]
    }
    // log("@ByteArray", output.map { it.toChar() })
    return String(output)
  }

}