package com.example.xposedtest.xposed

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import com.alibaba.fastjson.JSON
import com.example.xposedtest.MainHook
import com.example.xposedtest.bean.FastLoadBean
import com.example.xposedtest.db.DbEventHandler
import com.example.xposedtest.utility.CommonUtil.Companion.bool
import com.example.xposedtest.utility.CommonUtil.Companion.byteArrayToString
import com.example.xposedtest.utility.CommonUtil.Companion.typeCast
import com.example.xposedtest.utility.DebugUtil
import com.example.xposedtest.utility.DebugUtil.Companion.flattenBundle
import com.example.xposedtest.utility.DebugUtil.Companion.showArgs
import com.example.xposedtest.utility.FsUtil
import com.example.xposedtest.utility.cast
import com.example.xposedtest.utility.getField
import com.example.xposedtest.wx.JsFuncHt
import com.example.xposedtest.wx.WxClass
import com.example.xposedtest.wx.WxHelper.peekBizLvbuffer
import com.example.xposedtest.wx.WxUiHt
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.lang.ref.WeakReference

object WxMethodHook {
  val hookActivityOnResume = object : XC_MethodHook() {
    override fun afterHookedMethod(param: MethodHookParam) {
      fun xLog(msg: String?) = DebugUtil.log("[Cache_Activity]$msg")
      val className = "${param.thisObject}".substringBeforeLast('@')
      typeCast<Activity>(param.thisObject)?.run {
        xLog("$this")
        when (className.hashCode()) {
          WxUiHt.LauncherUI -> {
            MainHook.gLaunchUIRef = WeakReference(this)
          }
          WxUiHt.BrandServiceIndexUI -> {
            MainHook.gBrandServiceIndexUIRef = WeakReference(this)
          }
          WxUiHt.ChattingUI -> {
            MainHook.gChattingUIRef = WeakReference(this)
          }
          WxUiHt.ContactInfoUI -> {
            MainHook.gContactInfoUIRef = WeakReference(this)
          }
          WxUiHt.WebviewMpUI -> {
            MainHook.gWebviewMpUIRef = WeakReference(this)
          }
          WxUiHt.TmplWebViewTooLMpUI -> {
            MainHook.gTmplWebViewTooLMpUI = WeakReference(this)
          }
          else -> {
            xLog("there is no Ref to cache $className")
          }
        }
      } ?: xLog("$className is not an Activity") /* redundant */
    }
  }

  val hookActivityOnPause = object : XC_MethodHook() {
    override fun afterHookedMethod(param: MethodHookParam) {
      super.afterHookedMethod(param)

      fun xLog(msg: String?) = DebugUtil.log("[Pause_Activity]$msg")

      param.thisObject.cast<Activity>()?.run {
        xLog("$this")
      }
    }
  }

  val hookStartActivityB4 = object : XC_MethodHook() {
    override fun beforeHookedMethod(param: MethodHookParam) {
      showArgs("[br.d]", param.args, 4) { arg, i ->
        when (i) {
          0 -> "$arg"
          3 -> typeCast<Intent>(arg)?.run { flattenBundle(this.extras) }
          else -> typeCast(arg)
        }
      }
    }
  }

  val hookStartActivityB5 = object : XC_MethodHook() {
    override fun beforeHookedMethod(param: MethodHookParam) {
      showArgs("[br.d]", param.args, 5) { arg, i ->
        when (i) {
          0 -> "$arg"
          3 -> typeCast<Intent>(arg)?.run { flattenBundle(this.extras) }
          4 -> "${typeCast<Int>(arg)}"
          else -> typeCast(arg)
        }
      }
    }
  }

  val hookStartActivityA9 = object : XC_MethodHook() {
    override fun beforeHookedMethod(param: MethodHookParam) {
      showArgs("[PreloadLogic.a]", param.args, 9) { arg, i ->
        when (i) {
          5 -> typeCast<Intent>(arg)?.let { flattenBundle(it.extras) }
          else -> "$arg"
        }
      }
    }
  }

  val hookWxWebViewUIOnPause = object : XC_MethodHook() {
    override fun afterHookedMethod(param: MethodHookParam) = runBlocking {
      if (!"${param.thisObject}".substringBeforeLast('@').hashCode().equals(WxUiHt.WebviewMpUI)) {
        return@runBlocking
      }

      MainHook.scrollJob?.cancelAndJoin()
      MainHook.scrollJob = null

      return@runBlocking
    }
  }

  val hookWxWebViewUIOnResume = object : XC_MethodHook() {
    override fun afterHookedMethod(param: MethodHookParam) = runBlocking {
      if (!"${param.thisObject}".substringBeforeLast('@').hashCode().equals(WxUiHt.WebviewMpUI)) {
        return@runBlocking
      }

      MainHook.scrollJob?.cancelAndJoin()
      MainHook.backJob?.cancelAndJoin()
      MainHook.scrollJob = null
      MainHook.backJob = null

      MainHook.gWebviewMpUIRef?.run { MainHook.scrollJob = ViewHook.autoScrollXwalkView(this) } /* only scroll allBizInfo */
      return@runBlocking
    }
  }

  val hookBizHistoryUrlUpdate = object : XC_MethodHook() {
    override fun afterHookedMethod(param: MethodHookParam) {
      fun xLog(msg: String?) = DebugUtil.log("[hookBizHistoryUrlUpdate]$msg")

      fun truncateRedirect(url: String): String {
        val i = url.lastIndexOf('#')
        if (i != -1) {
          return url.substring(0, i)
        }
        return url
      }

      fun toggleSchema(url: String): String {
        if (!url.startsWith("https")) {
          return url.replace("http", "https")
        }
        return url
      }

      typeCast<String>(param.args[0])?.run {
        val jsonObject = JSONObject(this)
        when (jsonObject.getString("func").hashCode()) {
          JsFuncHt.handleHaokanAction -> xLog(this)
          JsFuncHt.downloadPageDataForFastLoad -> {
            JSON.parseObject(this, FastLoadBean::class.java)?.params?.itemList?.forEach {
              xLog(it.url)
            }
          }
          else -> {
          }
        }
      }
    }
  }

  val hookOnTabClick = object : XC_MethodHook() {
    fun xLog(msg: String?) = DebugUtil.log("[hookOnTabClick]$msg")

    override fun afterHookedMethod(param: MethodHookParam) {
      val index = typeCast<Int>(param.args[0])
      xLog(">>>$index<<<")

      when (index) {
        1 -> {
          val sql = "SELECT * FROM BizTimeLineInfo order by orderFlag DESC limit 1"
          xLog("exec:$sql")
          val cursor = XposedHelpers.callMethod(MainHook.mainDatabase, "rawQuery", sql, null)
          if (!bool(cursor)) {
            return
          }
          typeCast<Cursor>(cursor)?.run {
            if (this.moveToFirst()) {
              peekBizLvbuffer(this)
            }
            this.close()
          }
          /** enter BrandServiceIndexUI */
          // CoroutineScope(Dispatchers.Default).launch {
          //   val csdnBizId = "gh_5c0f5c561574"
          //   AutoTask.jumpToAllBizInfo(csdnBizId) /* add callback */
          // }
        }
        2 -> {
          // peekUserInfo()
          // peekChatroomMemberList("22680037765@chatroom")

        }
        //3 -> {
        //  val atUserMap = HashMap<String, String>()
        //  atUserMap["atuserlist"] = "<![CDATA[wxid_1ngjbbjujm9q22,wxid_lqqaa0nykwhl21]]>"
        //  val modelmulti_h = XposedHelpers.newInstance(modelmulti_h_cls, "22680037765@chatroom", "autotest@finder @孙明强", 1, 291, atUserMap)
        //  val ah_p = XposedHelpers.callStaticMethod(model_av_cls, "Pw")
        //  XposedHelpers.callMethod(ah_p, "a", modelmulti_h, 0)
        //
      }
    }
  }

  val openDatabase = hookAfter {
    gLog("openDatabase", args[0], args[1].cast<ByteArray>()?.run { String(this) }, args[4])
    args[0].cast<String>()?.run {
      if (this.endsWith("EnMicroMsg.db")) {
        MainHook.mainDatabase = result
        args[2]?.run {
          gLog("SQLiteCipherSpec",
              this.getField<String>("cipher"),
              this.getField<Boolean>("hmacEnabled"),
              this.getField<Int>("kdfIteration"),
              this.getField<Int>("pageSize"))
        }
      }
    }
  }

  val cf_a_a = hookBoth({
    gLog("cf_a.a.before", *args)
    val j = args[1].cast<Long>()
    val imei = XposedHelpers.callStaticMethod(WxClass.cf_a, "dpL")
        .cast<Collection<String>>()
        ?.also { it.forEach { gLog("cf_a.dpL", it) } }

    if (j != null && imei != null && imei.size > 0)
      gLog("cf_a.a.calcMd5",
          XposedHelpers.callStaticMethod(
              WxClass.a_g,
              "u",
              (imei.first() + j)
                  .also { gLog("compositeString", it) }
                  .toByteArray()))
  }, {
    gLog("cf_a.a.after", thisObject.getField<String>("key"))
  })

  val hookOpenDatabase = object : XC_MethodHook() {
    override fun afterHookedMethod(param: MethodHookParam) {
      fun xLog(msg: String?) = DebugUtil.log("[OpenDatabase.after]$msg")

      with(param) {
        val db = typeCast<String>(args[0])
        if (bool(db?.endsWith("EnMicroMsg.db"))) {
          MainHook.mainDatabase = this.result // cache main database
        }
        xLog("$db:${byteArrayToString(args[1])}")
      }
    }
  }

  val hookDbInsert = object : XC_MethodHook() {
    override fun beforeHookedMethod(param: MethodHookParam) {
      val tag = "[insertWithOnConflict]"
      DebugUtil.showArgs(tag, param.args, 4) { arg, i ->
        when (i) {
          2 -> typeCast(XposedHelpers.callMethod(arg, "toString"))
          3 -> "${typeCast<Int>(arg)}"
          else -> typeCast(arg)
        }
      }
      val interst = listOf("message", "chatroom", "img_flag", "rcontact", "rconversation", "BizTimeLineInfo")
      val i = typeCast<Int>(param.args[3]) ?: return
      val contentValues = typeCast<ContentValues>(param.args[2]) ?: return
      typeCast<String>(param.args[0]).let { table ->
        if (!interst.contains(table)) {
          return
        }

        when (i) {
          0 -> {
            if (table.equals("message")) {
              val type = contentValues.getAsInteger("type")
              if (type == 570425393) {
                DbEventHandler.onRoomMemberChanged(contentValues)
              }
            }
            if (table.equals("ProfileInfo")) {
              DbEventHandler.onProfileInfoUpdated(contentValues)
            }
            if (table.equals("BizTimeLineInfo")) {
              DbEventHandler.onBizTimeLineInfoUpdated(contentValues)
            }
          }
          5 -> {
            if (table.equals("chatroom")) {
              DbEventHandler.onMemberQuit(contentValues)
            }
          }
        }
      }
    }
  }

  val hookShortenUrl = object : XC_MethodHook() {
    fun xLog(msg: String?) = DebugUtil.log("[hookShortenUrl]$msg")

    override fun afterHookedMethod(param: MethodHookParam) {
      xLog("${param.args[0]} -> ${param.result}")
    }
  }

  val hookLongUrl = object : XC_MethodHook() {
    fun parseTicket(str: String?): String? {
      str ?: return null
      val key = "pass_ticket"
      val i = str.lastIndexOf(key)
      if (i != -1) {
        val j = str.indexOf('&', i)
        if (j != -1) {
          return str.substring(i + key.length + 1, j)
        }
      }
      return null
    }

    override fun afterHookedMethod(param: MethodHookParam) {
      typeCast<String>(param.args[1])?.let { func ->
        when (func.hashCode()) {
          JsFuncHt.downloadPageDataForFastLoad -> {
            parseTicket(flattenBundle(typeCast(param.args[4])))?.run { MainHook.gPassTicket = this }
          }
          else -> {
          }
        }
      }
      MainHook.gJsApiProxy = param.thisObject
    }
  }

  val hookBizComment = object : XC_MethodHook() {
    fun xLog(msg: String?) = DebugUtil.log("[hootBizComment]$msg")

    override fun beforeHookedMethod(param: MethodHookParam) {
      flattenBundle(param.args[2])?.run {
        if (this.contains("appmsgstat")) {
          // xLog(this)
          FsUtil.saveStringToFile(this, "${FsUtil.BizPath}/biz_article_read_status")
        }
        if (this.contains("elected_comment")) {
          FsUtil.saveStringToFile(this, "${FsUtil.BizPath}/biz_article_comment")
        }
      }
    }
  }
}