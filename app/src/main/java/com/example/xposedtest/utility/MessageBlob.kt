package com.example.xposedtest.utility

import com.example.xposedtest.xposed.gLog

object MessageBlob {

  /** Element type (com.tencent.mm.f.c.dd):
   * 0, int
   * 1, string
   * 2, long
   * 3, buffer
   */
  val typeList = listOf(1, 0,
      1, // Duration
      0,
      0, 0, 0, 0, 0,
      1, 1, 1,
      0, 1, 3, 1)

  fun load(input: ByteArray): List<Any> {
    val m = mutableListOf<Any>()
    val parser = BlobParser(input)
    runCatching {
      m.apply {
        typeList.forEach {
          if (parser.eob())
            return@forEach
          when (it) {
            0 -> add(parser.int())
            1 -> add(parser.string())
            2 -> add(parser.long())
            3 -> add(parser.buffer())
          }
        }
      }.also {
        val sb = StringBuffer()
        var i = 0
        it.forEach {
          sb.append(if (i == 0) "$i)$it" else ", $i)$it")
          ++i
        }
        gLog("@Blob", sb.toString().trimIndent().replace('\n', ' '))
      }
    }
        .onFailure { gLog("@MessageTable", "parse blob failed: ${it.message}") }
    return m
  }

}
