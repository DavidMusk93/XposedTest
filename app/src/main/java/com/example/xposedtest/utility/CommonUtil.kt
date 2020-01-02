package com.example.xposedtest.utility

import com.facebook.network.connectionclass.ConnectionClassManager
import com.facebook.network.connectionclass.ConnectionQuality
import com.facebook.network.connectionclass.DeviceBandwidthSampler
import okhttp3.OkHttpClient
import okhttp3.Request

class CommonUtil {
  companion object {
    inline fun <reified T> typeCast(item: Any?): T? {
      @Suppress("UNCHECKED_CAST")
      return item as? T
    }

    fun bool(b: Any?): Boolean {
      b ?: return false
      typeCast<Boolean>(b)?.run { return this }
      return true
    }

    fun stringToList(str: String, separator: Char = ';'): List<String> {
      return str.trim().split(separator)
    }

    fun stringToMap(str: String, separator: Char = ';'): Map<String, Any?> {
      val list = stringToList(str, separator)
      val map = mutableMapOf<String, Any?>()
      list.forEach {
        map[it] = null
      }
      return map
    }

    fun checkNetworkQuality(): Int {
      fun _log(msg: String?) = DebugUtil.log("[checkNetworkQuality]$msg")
      val url = "https://www.baidu.com/"
      val client = OkHttpClient()
      DeviceBandwidthSampler.getInstance().startSampling()

      val request = Request.Builder().url(url).build()
      val response = client.newCall(request).execute()
      _log("response length: ${response.body?.string()?.length}")

      DeviceBandwidthSampler.getInstance().stopSampling()
      ConnectionClassManager.getInstance().currentBandwidthQuality.run {
        return when (this) {
          ConnectionQuality.UNKNOWN -> -1
          else -> this.ordinal
        }
      }
    }

    fun byteArrayToString(bytes: ByteArray?): String {
      bytes ?: return ""
      val sb = StringBuilder()
      var i = 0
      for (b in bytes) {
        sb.append("%5d".format(b))
        i = i.inc() % 16
        if (i == 0) {
          sb.appendln()
        }
      }
      return sb.toString()
    }

    fun byteArrayToString(array: Any?): String? {
      typeCast<ByteArray>(array)?.run { return String(this) }
          ?: return null
    }

    fun filterXmlByElement(xml: String, element: String): String? {
      val i = xml.indexOf("<$element")
      val j = xml.lastIndexOf("$element>")
      if (i < 0 || j < 0) {
        return null
      }
      return xml.substring(i, j + element.length + 1)
    }

    fun truncateChksm(url: String?): String? {
      url ?: return null
      val i = url.lastIndexOf("chksm")
      return if (i == -1) url else url.substring(0, i - 1)
    }
  }
}