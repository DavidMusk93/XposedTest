package com.example.xposedtest.utility

import com.example.xposedtest.xposed.gLog
import java.io.File

object VoiceHelper {

  fun decode(amr: String?) {
    gLog("@VoiceHelper", "amr: $amr")
    if (amr == null || !amr.endsWith(".amr"))
      return
    File(amr).apply {
      if (!exists()) {
        gLog("@VoiceHelper", "$this not exist")
        return
      }
    }
    val mp3 = "/sdcard/" + amr.replace(".amr", ".mp3").basename()
    gLog("@AmrToMp3", "$amr to $mp3")
    // runCatching {
    //   val ret = JNI.decode(amr, mp3, mp3+"_")
    //   if (ret != 0) {
    //     gLog("@VoiceDecode", "Decode Voice Error: $ret")
    //   }
    // }.onFailure { gLog("@VoiceDecode", "JNI.decode not found") }
  }

}