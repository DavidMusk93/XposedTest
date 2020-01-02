package com.example.xposedtest.db

import android.content.ContentValues
import com.example.xposedtest.utility.CommonUtil
import com.example.xposedtest.utility.CommonUtil.Companion.filterXmlByElement
import com.example.xposedtest.utility.DebugUtil
import com.example.xposedtest.utility.DebugUtil.Companion.flattenList
import com.example.xposedtest.utility.DebugUtil.Companion.flattenMap
import com.example.xposedtest.utility.DebugUtil.Companion.log
import com.example.xposedtest.utility.DebugUtil.Companion.print_member
import com.example.xposedtest.utility.FsUtil
import com.example.xposedtest.wx.WxHelper
import com.example.xposedtest.xposed.XposedUtil
import de.robv.android.xposed.XposedHelpers
import fr.arnaudguyon.xmltojsonlib.XmlToJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object DbEventHandler {
  fun onMemberQuit(contentValues: ContentValues) {
    fun xLog(msg: String?) = DebugUtil.log("[onMemberQuit]$msg")

    val roomId = contentValues.getAsString("chatroomname")
    val member = contentValues.getAsString("memberlist")
    val lastMemberFile = "${FsUtil.RoomLogPath}/${roomId}_memberlist"
    val addList = arrayListOf<String>()
    val delList = arrayListOf<String>()

    if (!FsUtil.isEmptyFile(lastMemberFile)) {
      val lastMember = FsUtil.readFileAsString(lastMemberFile)
      val oldMemberMap = CommonUtil.stringToMap(lastMember)
      val newMutableMap = CommonUtil.stringToMap(member)

      for (k in newMutableMap.keys) {
        if (!oldMemberMap.containsKey(k)) {
          addList.add(k)
        }
      }

      for (k in oldMemberMap.keys) {
        if (!newMutableMap.containsKey(k)) {
          delList.add(k)
        }
      }
    }
    FsUtil.saveStringToFile(member, lastMemberFile)

    if (delList.isNotEmpty()) {
      xLog(">>>>>>${delList.joinToString()} proactively quit $roomId<<<<<<")
    }
  }

  fun onRoomMemberChanged(contentValues: ContentValues) {
    val msgSvrId = contentValues.getAsString("msgSvrId")
    val talker = contentValues.getAsString("talker")
    val content = contentValues.getAsString("content")

    val xml = filterXmlByElement(content, "content_template") ?: return
    val changedType: String


    if (xml.contains("\$username\$")) {
      changedType = "InviteByOther"
    } else if (xml.contains("\$kickoutname\$")) {
      changedType = "Kickout"
    } else {
      changedType = "InviteByMe"
    }

    val name = "${talker}_${msgSvrId}_${changedType}"
    val json = XmlToJson.Builder(xml)
        .forceList("/content_template/link_list")
        .forceList("/content_template/link_list/link/memberlist")
        .build()
    FsUtil.saveStringToFile(json.toFormattedString(), "${FsUtil.MemberChangePath}/$name")
  }

  fun onProfileInfoUpdated(contentValues: ContentValues) {
    val username = contentValues.getAsString("username")
    CoroutineScope(Dispatchers.Default).launch {
      log("[onProfileInfoUpdate]auto add $username")
      delay(1000)
      WxHelper.addBiz(username)
    }
  }

  fun onBizTimeLineInfoUpdated(contentValues: ContentValues) {
    fun xLog(msg: String?) = DebugUtil.log("[onBizTimeLineInfoUpdated]$msg")

    val ae_j_b_cls = XposedUtil.findClass("com.tencent.mm.ae.j.b")
    val content = contentValues.getAsString("content")

    XposedHelpers.callStaticMethod(ae_j_b_cls, "lE", content)?.run {
      xLog("$this")
      xLog("[eRf]${flattenMap(XposedHelpers.getObjectField(this, "eRf"))}")
      xLog("[eTf]${flattenList<String>(XposedHelpers.getObjectField(this, "eTf"))}")
      xLog("[eTh]${flattenList<String>(XposedHelpers.getObjectField(this, "eTh"))}")
      print_member<String>(this, "title")
      print_member<Int>(this, "type")
      print_member<String>(this, "url")
      print_member<String>(this, "username")
    }
  }
}