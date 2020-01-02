package com.example.xposedtest.utility

import android.os.Environment
import java.io.File

class FsUtil {
  companion object {
    fun pathLazy(path: String): Lazy<String> = lazy {
      fun xLog(msg: String?) = DebugUtil.log("[pathLazy]$msg")
      val dir = File(path)
      if (!dir.exists()) {
        if (!dir.mkdirs()) {
          xLog("mkdir ($dir) failed")
        }
      }
      path
    }

    val RoomInviteePath by pathLazy("${Environment.getExternalStorageDirectory()}/wxbridge/AccessVerifyDir")
    val MemberChangePath by pathLazy("${Environment.getExternalStorageDirectory()}/wxbridge/MemberChangeDir")
    val RoomLogPath by pathLazy("${Environment.getExternalStorageDirectory()}/wxbridge/RoomLogDir")
    val BizPath by pathLazy("${Environment.getExternalStorageDirectory()}/wxbridge/BizDir")
    val ViewHookLockFile by lazy { "$BizPath/view_hook.lock" }

    fun saveStringToFile(text: String, filename: String) {
      try {
        val printWriter = File(filename).printWriter()

        printWriter.println(text)
        printWriter.close()
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }

    fun readFileAsString(filename: String) = File(filename).readText(Charsets.UTF_8)
    fun deleteFile(filename: String) = File(filename).delete()

    fun isEmptyFile(filename: String): Boolean {
      val file = File(filename)
      if (file.exists() && file.isFile) {
        if (file.length() > 0) {
          return false
        }
      }
      return true
    }
  }
}