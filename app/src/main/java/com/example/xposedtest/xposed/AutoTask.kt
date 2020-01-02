package com.example.xposedtest.xposed

import android.content.Intent
import android.widget.ListView
import com.example.xposedtest.MainHook.Companion.gBrandServiceIndexUIRef
import com.example.xposedtest.MainHook.Companion.gChattingUIRef
import com.example.xposedtest.MainHook.Companion.gContactInfoUIRef
import com.example.xposedtest.MainHook.Companion.gLaunchUIRef
import com.example.xposedtest.utility.CommonUtil
import com.example.xposedtest.utility.CommonUtil.Companion.typeCast
import com.example.xposedtest.utility.DebugUtil
import com.example.xposedtest.xposed.ViewHook.autoScrollListView
import de.robv.android.xposed.XposedHelpers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

object AutoTask {
  fun jumpToAllBizInfo(bizId: String) = runBlocking {
    fun xLog(msg: String?) = DebugUtil.log("[UI_Jump]$msg")

    val delayPool = listOf<Long>(1500, 2000, 2500)
    val br_d_cls = XposedUtil.findClass("com.tencent.mm.br.d")
    val b_cls = XposedUtil.findClass("com.tencent.mm.plugin.brandservice.b")

    if (!CommonUtil.bool(br_d_cls) || !CommonUtil.bool(b_cls)) {
      xLog("fail to find class")
      return@runBlocking
    }

    delay(delayPool.shuffled().last())
    gLaunchUIRef?.get()?.run {
      xLog("launch BrandServiceIndexUI")

      var intent = Intent()
      intent.putExtra("intent_service_type", 251658241)

      XposedHelpers.callStaticMethod(br_d_cls, "b", this, "brandservice", ".ui.BrandServiceIndexUI", intent)

      delay(delayPool.shuffled().last())
      intent = Intent()
      intent.putExtra("Chat_User", bizId)
      intent.putExtra("finish_direct", true)
      intent.putExtra("chat_from_scene", 2)
      intent.putExtra("specific_chat_from_scene", 4)
      intent.putExtra("img_gallery_enter_from_chatting_ui", true)
      gBrandServiceIndexUIRef?.get()?.run {
        xLog("launch ChattingUI")
        XposedHelpers.getStaticObjectField(b_cls, "fUR")?.let { app_x_e ->
          XposedHelpers.callMethod(app_x_e, "e", intent, this)
        }

        delay(delayPool.shuffled().last())
        intent = Intent()
        intent.putExtra("Kdel_from", 2)
        intent.putExtra("chat_from_scene", 2)
        intent.putExtra("key_biz_profile_stay_after_follow_op", false)
        intent.putExtra("preChatTYPE", 7)
        intent.putExtra("KOpenArticleSceneFromScene", 0)
        intent.putExtra("preChatName", bizId)
        intent.putExtra("Contact_User", bizId)
        intent.putExtra("preUsername", bizId)
        gChattingUIRef?.get()?.run {
          xLog("launch ContactInfoUI")
          XposedHelpers.callStaticMethod(br_d_cls, "b", this, "profile", ".ui.ContactInfoUI", intent, 213)
        }

        delay(delayPool.shuffled().last())
        gContactInfoUIRef?.get()?.run {
          typeCast<ListView>(XposedHelpers.getObjectField(this, "oHA"))?.let { listView -> autoScrollListView(listView, 300, 300, 10) }

          delay(delayPool.shuffled().last())
          XposedHelpers.getObjectField(this, "oHt")?.run {
            xLog("goto contact_info_all_message_newbizinfo")
            XposedHelpers.callMethod(this, "EF", "contact_info_all_message_newbizinfo")
          }
        }
      }
    }
  }
}