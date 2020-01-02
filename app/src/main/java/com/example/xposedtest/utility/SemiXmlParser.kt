package com.example.xposedtest.utility

object SemiXmlParser {

  fun load(s: String?): Map<String, String>? {
    var a = 0
    var b: Int
    var key: String
    if (s == null || !s.startsWith("~SEMI_XML~"))
      return null
    val r = s.substring(10)
    val length = r.length - 4
    mutableMapOf<String, String>().apply {
      while (a < length) {
        b = r[a].toInt().shl(16) + r[++a].toInt() + a
        key = r.substring(a, b)
        a = r[b].toInt().shl(16) + r[++b].toInt() + b
        key to r.substring(b, a)
      }
      return this
    }
  }

}