package com.example.xposedtest.module.tomato

import android.view.View
import android.widget.LinearLayout
import com.example.xposedtest.annotation.HookClass
import com.example.xposedtest.annotation.HookMethod
import com.example.xposedtest.utility.C
import com.example.xposedtest.utility.cast
import com.example.xposedtest.utility.getField
import com.example.xposedtest.utility.setField
import com.example.xposedtest.xposed.*
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import kotlinx.coroutines.*
import java.lang.ref.WeakReference

class TomatoHookContext : HookContext() {

  companion object {
    val idMap = mapOf(
        2131297368 to "find",
        2131297390 to "live",
        2131297401 to "mime",
        2131297468 to "tomato",
        2131297776 to "game"
    )

    var enableLog = true
  }

  override fun attachBaseContext() {
    attach("com.mine.proxy_core.ProxyApplication")
  }

  override fun hook() {
    super.hook()
    // hookTrivial()
    // hookAd()
    // hookLookTime()
    // hookView()
    // hookLevel()
    // hookLog()
    // hookLive()
    // hookDownloadVideo()

    for (method in this::class.java.declaredMethods) {
      for (annotation in method.annotations) {
        when (annotation) {
          is HookMethod ->
            method.invoke(this)
        }
      }
    }
  }

  @HookMethod
  private fun hookUpdate() {
    "com.one.tomato.entity.UpdateInfo".`class`()
        .apply { hook("getVersionList", hookAfter { result = null }) }
  }

  @HookMethod
  private fun hookDownloadVideo() {
    "qx".`class`()
        .apply {
          // hook("e", C.String, hookAfter { log("(qx)UrlTransfer", *args, result) })
          /* M3U8 downloader */
          hook("g", C.String, hookAfter { log("(g)M3U8Download", *args, result) })
        }

    // "ex".`class`()
    //     .apply {
    //       hook("a", C.String, hookAfter { log("(ex)UrlTransfer", *args, result) })
    //     }

    val M3U8 = "com.one.tomato.thirdpart.m3u8.download.entity.M3U8".`class`()

    "com.one.tomato.dialog.p".`class`()
        .apply {
          hook("a",
              M3U8, C.String,
              hookAfter {
                log("Dump", *args)
                log("(M3U8)dirFilePath", args[0].getField("dirFilePath"))
              })
        }
  }

  @HookMethod
  private fun hookLive() {
    "com.tencent.rtmp.TXLivePlayer".`class`()
        .apply {
          hook("startPlay",
              C.String, C.Int,
              hookBefore { log("startPlay", *args) })
        }
  }

  @HookMethod
  private fun hookLog() {
    "com.one.tomato.utils.n".`class`()
        .apply {
          hook("a", C.Boolean, hookBefore { args[0] = enableLog })
        }
  }

  @HookMethod
  private fun hookTrivial() {
    "com.one.tomato.mvp.base.BaseApplication".`class`()
        .apply {
          hook("j",
              hookAfter {
                log("Field", thisObject.getField("b"))
                log("Field", thisObject.getField("d"))
              })

          hook("e", hookAfter { log("(e)Result", result) })
        }

    "com.one.tomato.ui.MainTabActivity".`class`()
        .apply {
          hook("onClick", C.View,
              hookBefore { log("onClick", args[0].cast<View>()?.id?.let { idMap[it] }) })

          hook("k", C.Int,
              hookBefore { log("(k)Input", args[0], thisObject.getField("B")) })
        }
  }

  @HookMethod
  private fun hookAd() {
    "com.one.tomato.utils.f".`class`()
        .apply {
          hook("c", C.String, hookAfter { result = null })

          hook("e", C.String, hookAfter { result = null })
        }
  }

  @HookMethod
  private fun hookLookTime() {
    "ny".`class`()
        .apply {
          hook("b", hookAfter { result = 1000 })

          hook("a", hookAfter {
            thisObject.getField<Any>("f5814a")?.setField("hasPay", true)
            result = 1000
          })
        }
  }

  @HookMethod
  private fun hookView() {
    "com.one.tomato.dialog.k".`class`()
        .apply {
          val bean = "com.one.tomato.entity.MainNotifyBean".`class`()
          XposedHelpers.findAndHookConstructor(this,
              C.Context, bean,
              hookAfter { ViewHelper.clickView(thisObject.getField("v")) })
        }

    val adViewContext = AdViewContext()

    "com.tomatolive.library.ui.view.custom.LiveAdBannerView".`class`()
        .apply {
          hook("initView",
              hookAfter {
                adViewContext.v1 = WeakReference(thisObject.getField("ivAdClose"))
                adViewContext.v2 = WeakReference(thisObject.getField("ivBannerClose"))
              })

          hook("loadImg",
              C.String, C.ImageView,
              hookAfter {
                if (adViewContext.testCount()) {
                  CoroutineScope(Dispatchers.Unconfined).launch {
                    delay(500)
                    ViewHelper.clickView(adViewContext.v1?.get())
                    delay(500)
                    ViewHelper.clickView(adViewContext.v2?.get())
                  }
                }
              })
        }

    "com.tomatolive.library.ui.activity.live.TomatoLiveFragment".`class`()
        .apply {
          val LiveInitInfoEntity = "com.tomatolive.library.model.LiveInitInfoEntity"

          hook("initRoomInfo", LiveInitInfoEntity,
              hookAfter {
                // log("TomatoLiveFragment", *args)
                thisObject.getField<LinearLayout>("chatMsgRoot")?.let { layout ->
                  MainScope().launch {
                    delay(1000)
                    layout.visibility = LinearLayout.GONE
                  }
                }
              })
        }

    "com.one.tomato.mvp.ui.start.view.WarnActivity".`class`()
        .apply {
          hook("initData",
              hookAfter {
                ViewHelper.clickView(XposedHelpers.callMethod(thisObject,
                    "i",
                    2131298188))
              })
        }
  }

  @HookMethod
  private fun hookLevel() {
    "com.one.tomato.utils.c0".`class`()
        .apply {
          hook("a", C.Int,
              hookAfter { log("(a)Argument", *args, result) })
        }

    "com.one.tomato.entity.db.LevelBean".`class`()
        .apply {
          hook("getCurrentLevelIndex",
              hookAfter {
                log("getCurrentLevelIndex", result)
                result = 15
              })

          hook("getCurrentLevelValue",
              hookAfter {
                log("getCurrentLevelValue", result)
              })
        }

    "com.one.tomato.mvp.ui.papa.view.NewPaPaVideoPlayFragment".`class`()
        .apply {
          hook("c", C.Boolean,
              hookBefore { log("(c)Invoke", *args, XposedHelpers.callMethod(thisObject, "y")) })

          hook("B0",
              hookBefore {
                log("(B0)Invoke", "Playing video",
                    thisObject
                        .getField<Any>("B")
                        ?.getField("secVideoUrl"))
              })
        }

    "my".`class`()
        .apply {
          hook("c", hookBefore { thisObject.setField("e", true) })
        }

    "com.one.tomato.entity.db.UserInfo".`class`()
        .apply {
          hook("getVipType", hookBefore { thisObject.setField("vipType", 1) })
        }

    val LookTimes = "com.one.tomato.entity.LookTimes".`class`()
        .apply {
          hook("getTmtBalance", hookBefore { thisObject.setField("tmtBalance", 999) })
        }

    "com.one.tomato.dialog.q".`class`()
        .apply {
          hook("a",
              LookTimes,
              hookAfter {
                log("Member(x)", thisObject.getField("x"))
                ViewHelper.clickView(thisObject.getField("u"))
              })
        }
  }

}

@HookClass("com.one.fq.ad")
class TomatoHook(param: XC_LoadPackage.LoadPackageParam)
  : HookEntry(param, TomatoHookContext()), IHookEntry {

  override fun setupHook() {
    super.setupHook(this)
  }

}