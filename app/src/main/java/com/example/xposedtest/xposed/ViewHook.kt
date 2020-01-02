package com.example.xposedtest.xposed

import android.app.Activity
import android.app.Instrumentation
import android.view.KeyEvent
import android.widget.ListView
import com.example.xposedtest.MainHook
import com.example.xposedtest.utility.CommonUtil.Companion.checkNetworkQuality
import com.example.xposedtest.utility.CommonUtil.Companion.typeCast
import com.example.xposedtest.utility.DebugUtil
import com.example.xposedtest.wx.WxClass
import de.robv.android.xposed.XposedHelpers
import kotlinx.coroutines.*
import java.lang.Runnable
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

object ViewHook {
  private val stepPool by lazy { listOf(410, 503, 680, 700, 800, 835) }
  private val delayPool by lazy { listOf<Long>(1200, 2000, 2500) }

  interface ViewHookCallback {
    fun onComplete()
    fun onCancel()
  }

  fun gobackWebView(ref: WeakReference<Activity>?): Job {
    fun xLog(msg: String?) = DebugUtil.log("[Go_Back_WebView]$msg")

    return CoroutineScope(Dispatchers.Default).launch {
      delay(10000)
      xLog("isActive: $isActive")
      if (isActive) {
        xLog("trigger system back")
        val inst = Instrumentation()
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)
      }
    }
  }

  fun autoScrollXwalkView(webViewUIRef: WeakReference<Activity>?, callback: ViewHookCallback? = null): Job {
    return CoroutineScope(Dispatchers.Default).launch {
      fun xLog(msg: String?) = DebugUtil.log("[Auto_Scroll_XwalkView]$msg")

      if (webViewUIRef?.get() == null) {
        return@launch
      }

      XposedHelpers.getObjectField(webViewUIRef.get(), "oVq")?.run {
        // _log("$this")
        XposedHelpers.getObjectField(this, "zXd")?.run {
          // _log("$this")
          if (!"$this".contains("xwalk")) {
            return@launch
          }
          XposedHelpers.getObjectField(this, "Ach")?.run {
            // _log("$this")
            xLog("start monitoring web  S C R O L L")
            val MAX_RETRY = 3
            var i = 0
            var j = 0
            var y: Int
            var yPre: Int
            var hitBottom = false
            val delayMillis = (4 - checkNetworkQuality()) * 2000
            val scrollTask = Runnable { XposedHelpers.callMethod(this, "scrollBy", 0, stepPool.shuffled().last()) }
            val readTask = Runnable {
              val url = "http://mp.weixin.qq.com/s?__biz=MjA1ODMxMDQwMQ==&mid=2657256220&idx=5&sn=a704eaf4b5c1d2307972558ce7597b80&chksm=490687727e710e64348b6db0bc8e1a849c7f3e73846b110573de56459de09664ab46ad8df451&scene=4&subscene=126#wechat_redirect"
              val a = XposedHelpers.newInstance(WxClass.BRANDSERVICE_A_B_A)
              xLog("goto $url")
              MainHook.gWebviewMpUIRef?.get()?.run { XposedHelpers.callStaticMethod(WxClass.PreloadLogic, "a", this, url, 0, 4, 0, null, 0, a, 32) }
              MainHook.backJob = gobackWebView(MainHook.gTmplWebViewTooLMpUI)
            }
            xLog("WebView delay milliseconds: $delayMillis")
            delay(delayMillis.toLong())

            while (isActive) {
              if (i++ > 6666) {
                break
              }
              yPre = typeCast(XposedHelpers.callMethod(this, "computeVerticalScrollOffset"))!!
              xLog("[${"%04d".format(i)}]web scroll-y position: $yPre")
              XposedHelpers.callMethod(this, "post", scrollTask)
              delay(delayPool.shuffled().last())
              y = typeCast(XposedHelpers.callMethod(this, "computeVerticalScrollOffset"))!!

              if (y == yPre) {
                delay(2000)
                if (!hitBottom) {
                  hitBottom = true
                  j = i - 1
                }

                if (i - j > MAX_RETRY) {
                  xLog("H I T  T H E  B O T T O M")
                  callback?.onComplete()
                  break
                }
                continue
              }
              hitBottom = false

              if (MainHook.gTest) {
                delay(1000)
                XposedHelpers.callMethod(this, "post", readTask)
                MainHook.gTest = false
              }
            }

            if (!hitBottom && !isActive) {
              callback?.onCancel()
            }
          }
        }
      }
    }
  }

  fun autoScrollListView(listView: ListView, step: Int, duration: Int, times: Int) {
    var i = 0
    val delayPool = listOf<Long>(500, 800, 1000, 1200, 2000, 2500)
    while (i++ < times) {
      listView.smoothScrollBy(step, duration)
      TimeUnit.MILLISECONDS.sleep(delayPool.shuffled()[0])
      if (step < 0 && listView.firstVisiblePosition == 0) {
        break
      }
      if (step > 0 && listView.lastVisiblePosition == listView.count - 1) {
        break
      }
    }
  }
}