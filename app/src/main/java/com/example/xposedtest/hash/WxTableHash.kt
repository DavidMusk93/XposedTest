package com.example.xposedtest.hash

object WxTableHash {
  val message: Int by lazy { "message".hashCode() }
  val BizTimeLineInfo: Int by lazy { "BizTimeLineInfo".hashCode() }
  val WxFileIndex2: Int by lazy { "WxFileIndex2".hashCode() }
}