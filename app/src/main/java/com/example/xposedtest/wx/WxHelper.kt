package com.example.xposedtest.wx

import android.database.Cursor
import com.alibaba.fastjson.JSON
import com.example.xposedtest.bean.NewBizInfoBean
import com.example.xposedtest.utility.CommonUtil
import com.example.xposedtest.utility.CommonUtil.Companion.typeCast
import com.example.xposedtest.utility.DebugUtil
import com.example.xposedtest.utility.FsUtil
import com.example.xposedtest.xposed.XposedUtil
import de.robv.android.xposed.XposedHelpers
import fr.arnaudguyon.xmltojsonlib.XmlToJson
import java.util.*

enum class AllowByIdentityStatus(val value: Int) {
  DISABLE(0),
  ENABLE(2)
}

object WxHelper {
  fun getWxId(): String? {
    val storage_z = XposedHelpers.callStaticMethod(WxClass.MODEL_C, "PO")
    storage_z ?: return null
    return typeCast(XposedHelpers.callMethod(storage_z, "getField", 2, null))
  }

  fun allowByIdentity(roomId: String, status: AllowByIdentityStatus) {
    val pb_azx_cls = XposedUtil.findClass("com.tencent.mm.protocal.protobuf.azx")
    val plugin_messenger_foundation_a_a_j_a_cls = XposedUtil.findClass("com.tencent.mm.plugin.messenger.foundation.a.a.j.a")
    val vh by Delegator.VH()

    XposedHelpers.newInstance(pb_azx_cls)?.run {
      XposedHelpers.setObjectField(this, "uTY", roomId)
      XposedHelpers.setIntField(this, "pti", status.value)

      XposedHelpers.newInstance(plugin_messenger_foundation_a_a_j_a_cls, 66, this)?.run {
        XposedHelpers.callMethod(vh, "c", this)
      }
    }
  }

  fun peekBizLvbuffer(cursor: Cursor) {
    fun xLog(msg: String?) = DebugUtil.log("[peekBizLvbuffer]$msg")

    val q_cls = XposedUtil.findClass("com.tencent.mm.storage.q")
    val a_cls = XposedUtil.findClass("com.tencent.mm.plugin.backup.f.a.a")

    XposedHelpers.newInstance(q_cls)?.run {
      XposedHelpers.callMethod(this, "d", cursor)
      typeCast<String>(XposedHelpers.callStaticMethod(a_cls, "Ej", XposedHelpers.getObjectField(this, "field_content")))?.let { content ->
        // xLog(content)
        FsUtil.saveStringToFile(content, "${FsUtil.BizPath}/msg.xml")
        val category = CommonUtil.filterXmlByElement(content, "category") ?: return@let
        val json = XmlToJson.Builder(category)
            .forceList("/category/item")
            .build()
            .toFormattedString()
        FsUtil.saveStringToFile(json, "${FsUtil.BizPath}/msg.json")
        val bean = JSON.parseObject(json, NewBizInfoBean::class.java)
        bean.category?.item?.forEach { xLog(CommonUtil.truncateChksm(it.url)) }
      }
      // print_member<Int>(this, "dew")
      // print_member<String>(this, "dex")
    }
    // xLog("<<<<<<")
  }

  fun changeChatroomNickname(id: String, name: String) {
    fun xLog(msg: String?) = DebugUtil.log("[changeChatroomNickname]$msg")

    val j: Any? by Delegator.VH()

    val bac = XposedHelpers.newInstance(WxClass.PROTOBUF_BAC)
    val brj = XposedHelpers.newInstance(WxClass.PROTOBUF_BRJ)
    val brj2 = XposedHelpers.newInstance(WxClass.PROTOBUF_BRJ)

    XposedHelpers.setBooleanField(brj, "wiQ", true)
    XposedHelpers.setObjectField(brj, "wiP", id)

    XposedHelpers.setBooleanField(brj2, "wiQ", true)
    XposedHelpers.setObjectField(brj2, "wiP", name)

    XposedHelpers.setObjectField(bac, "uUb", brj)
    XposedHelpers.setObjectField(bac, "vUx", brj2)

    val wHP = XposedHelpers.newInstance(WxClass.A_A_J_A, 27, bac)

    wHP ?: return
    j?.run {
      xLog("$this")
      XposedHelpers.callMethod(this, "c", wHP)
    }
  }

  fun addBiz(username: String) {
    val ghk = listOf(39, 56, 35, 87, 88, 89, 85)
    val evj by Delegator.EVJ()
    val l1 = LinkedList<String>()
    val l2 = LinkedList<Int>()
    val l3 = LinkedList<String>()
    val str: String? = null

    l1.add(username)
    l2.add(ghk.shuffled()[0])

    val m = XposedHelpers.newInstance(WxClass.PLUGINSDK_MODEL_M, 1, l1, l2, l3, "", "", null, "", null)
    /** illegal call: XposedHelpers.callMethod(m, "sP", null) */
    XposedHelpers.callMethod(m, "sP", str)
    XposedHelpers.callMethod(m, "lh", 0)
    evj?.run { XposedHelpers.callMethod(this, "a", m, 0) }
  }

  fun changeNicknameWithinChatroom(id: String, username: String, nickname: String) {
    fun xLog(msg: String?) = DebugUtil.log("[changeNicknameWithinChatroom]$msg")

    val azz = XposedHelpers.newInstance(WxClass.PROTOBUF_AZZ)
    val j: Any? by Delegator.VH()

    XposedHelpers.setObjectField(azz, "uTY", id)
    XposedHelpers.setObjectField(azz, "jfn", username)
    XposedHelpers.setObjectField(azz, "vmc", nickname)

    val wHP = XposedHelpers.newInstance(WxClass.A_A_J_A, 48, azz)
    wHP ?: return
    xLog("$j")
    j?.run {
      XposedHelpers.callMethod(this, "c", wHP)
    }
  }
}