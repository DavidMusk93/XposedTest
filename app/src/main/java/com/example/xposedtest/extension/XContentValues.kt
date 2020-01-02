package com.example.xposedtest.extension

import android.content.ContentValues
import com.example.xposedtest.db.MessageType
import com.example.xposedtest.utility.MessageBlob
import com.example.xposedtest.utility.SemiXmlParser
import com.example.xposedtest.xposed.gLog

fun ContentValues.extGet(k: String) = "$k|${get(k)}"

fun ContentValues.peekMessage() {
  when (getAsInteger("type")) {
    MessageType.BizInfo ->
      SemiXmlParser.load(getAsString("content"))?.run {
        gLog("@BizInfoContent", this)
      }
    MessageType.VoipContent ->
      MessageBlob.load(getAsByteArray("lvbuffer"))
    else ->
      gLog("@peekMessage",
          extGet("msgId"),
          extGet("msgSvrId"),
          extGet("type"),
          extGet("talker"),
          extGet("content"))
  }
}

fun ContentValues.amrToMp3() {
  // CoroutineScope(Dispatchers.Default).launch {
  // VoiceHelper.decode(WxPath.FilePath+"/"+getAsString("path"))
  // }
}