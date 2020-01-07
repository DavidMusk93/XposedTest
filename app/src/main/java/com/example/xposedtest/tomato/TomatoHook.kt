package com.example.xposedtest.tomato

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
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
  }

  private lateinit var realClassLoader: ClassLoader

  private fun String.`class`() = XposedHelpers.findClassIfExists(this, realClassLoader)

  private fun log(tag: String, vararg param: Any?) {
    gLog("@$processName@$tag", *param)
  }

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

  private fun hookAd() {
    "com.one.tomato.utils.f".`class`()
        .apply {
          hook("c", C.String, hookAfter { result = null })

          hook("e", C.String, hookAfter { result = null })
        }
  }

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
                log("TomatoLiveFragment", *args)
                thisObject.getField<LinearLayout>("chatMsgRoot")?.let { layout ->
                  MainScope().launch {
                    delay(1000)
                    layout.visibility = LinearLayout.GONE
                  }
                }
              })
        }
  }

  override fun attachBaseContext() {
    XposedHelpers.findAndHookMethod(
        "com.mine.proxy_core.ProxyApplication", classLoader,
        "attachBaseContext",
        C.Context,
        hookAfter {
          args[0].cast<Context>()
              ?.classLoader
              ?.apply {
                realClassLoader = this
                hookTrivial()
                hookAd()
                hookLookTime()
                hookView()
              }
        })

  }
}

class TomatoHook(param: XC_LoadPackage.LoadPackageParam)
  : HookEntry(param, TomatoHookContext()), IHookEntry {

  override fun setupHook() {
    super.setupHook(javaClass.simpleName)
  }
}