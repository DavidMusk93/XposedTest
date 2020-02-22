package com.example.xposedtest.module.miui

import android.content.pm.PackageInfo
import android.preference.PreferenceActivity
import com.example.xposedtest.annotation.HookMethod
import com.example.xposedtest.utility.*
import com.example.xposedtest.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage

class NestedHookContext(
    var inited: Boolean = false,
    var f1: String = "",
    var c1: String = "",
    var adapterName: String = "",
    var fragment: Any? = null
)

class MiuiMarketHook(lpparm: XC_LoadPackage.LoadPackageParam)
  : HookEntry(lpparm, HookContext()), IHookEntry {

  companion object TypeHash {

    val ArrayList = "class java.util.ArrayList".hashCode()
    val List = "interface java.util.List".hashCode()
    val Runnable = "interface java.lang.Runnable".hashCode()
    val Button = "class android.widget.Button".hashCode()
    val BaseLocalAppsAdapter = "class com.xiaomi.market.ui.BaseLocalAppsAdapter".hashCode()
  }

  override fun setupHook() {
    super.setupHook(this)
  }

  val ctx = NestedHookContext()
  val dynamicHookCtx = MarketDynamicHookContext()
  val installHookCtx = MarketInstallHookContext()

  @HookMethod
  private fun hookUninstall() {
    log("AppVersion", packageName, appVersion)
    "$packageName.ui.UninstallAppsFragment".`class`()!!
        .apply {
          val clz = this
          hook("onActivityCreated", C.Bundle,
              hookAfter {
                ctx.fragment = thisObject
                log("Uninstall", appVersion)
                clz.declaredFields.forEach {
                  when ("${it.type}".hashCode()) {
                    TypeHash.ArrayList -> {
                      kotlin.runCatching {
                        thisObject.getField<ArrayList<Object>>(it.name)?.forEach {
                          log("LocalAppInfo", it)
                        }
                      }.onFailure { log("GetLocalAppInfo@Failure", it) }
                    }
                    TypeHash.List -> {
                      ctx.f1 = it.name
                      log("Field@List", it.name)
                    }
                    TypeHash.Runnable -> {
                      "${thisObject.getField<Any>(it.name)}"
                          .substringBefore('@').let { c ->
                            log("Field@Runnable", it.name, c)
                            ctx.c1 = c
                          }
                    }
                    TypeHash.Button -> {
                      log("Field@Button", it.name)
                    }
                    TypeHash.BaseLocalAppsAdapter -> {
                      ctx.adapterName = it.name
                      thisObject.getField<Any>(it.name)?.apply {
                        log("Field@BaseLocalAppsAdapter", it.name, this)
                        runCatching {
                          log("Field@Adapter", callMethod("getCount"))
                          log("Field@Adapter", callMethod("getItem", 0))
                        }.onFailure { log("Field@Adapter@Failure", it) }
                      }
                    }
                  }
                }
                if (!ctx.inited && ctx.c1.isNotEmpty() && ctx.f1.isNotEmpty()) {
                  kotlin.runCatching {
                    ctx.c1.`class`()
                        ?.hook("run",
                            hookBefore {
                              ctx.fragment?.getField<MutableList<String>>(ctx.f1)?.apply {
                                log("UninstallThread", this)
                                Config.Package.protectAppList.forEach {
                                  remove(it)
                                }
                              }
                            })
                  }.onFailure { log("DynamicHook@Failure", it) }
                  ctx.inited = true
                }
              })
        }

    "$packageName.widget.MultiChoiceListView".`class`()!!
        .apply {
          hook("setItemChecked", C.Int, C.Boolean,
              hookBefore {
                log("ClickItem", *args)
                kotlin.runCatching {
                  ctx.fragment?.getField<Any>(ctx.adapterName)
                      ?.let { x ->
                        var packageName: String? = null
                        val item = x.callMethod("getItem", args[0])
                        if (dynamicHookCtx.nameOfPackageInfo.isEmpty()) {
                          item.getClassNameFromObject().`class`()?.apply {
                            declaredFields[0].let { f1 ->
                              dynamicHookCtx.nameOfLocalAppInfo = f1.name
                              item.getField<Any>(f1.name)?.apply {
                                f1.type.declaredFields.forEach { f2 ->
                                  if ("${f2.type}".contains("PackageInfo")) {
                                    dynamicHookCtx.nameOfPackageInfo = f2.name
                                    packageName = getField<PackageInfo>(f2.name)?.packageName
                                    return@forEach
                                  }
                                }
                              }
                            }
                          }
                        } else {
                          packageName = item
                              .getField<Any>(dynamicHookCtx.nameOfLocalAppInfo)
                              ?.getField<PackageInfo>(dynamicHookCtx.nameOfPackageInfo)
                              ?.packageName
                        }
                        packageName?.apply {
                          if (Config.Package.protectAppList.contains(this)) {
                            args[1] = false
                            "Uninstall $packageName is disabled!".toast2()
                          }
                        }
                      }
                }.onFailure { log("ClickItem@Failure", it) }
              })
        }
  }

  @HookMethod
  private fun hookPreference() {
    "$packageName.ui.MarketPreference".`class`()!!
        .apply {
          hook("i", hookAfter {
            result.cast<MutableList<String>>()?.apply {
              Config.Preference.filterList.forEach {
                remove(it)
              }
            }
          })
        }

    "$packageName.ui.AboutPreferenceActivity".`class`()!!
        .apply {
          hook("onCreate", C.Bundle,
              hookAfter {
                thisObject.cast<PreferenceActivity>()?.apply {
                  findPreference(Config.Preference.keyOfAutoUpdateMarket).let {
                    preferenceScreen.removePreference(it)
                  }
                }
              })
        }
  }

  @HookMethod
  private fun hookInstall() {
    val q = "$packageName.downloadinstall.q".`class`() ?: return
    "$packageName.downloadinstall.L".`class`()!!
        .apply {
          hook("a", q, C.PackageInstaller,
              hookBoth({
                log("downloadinstall@a", *args)
                installHookCtx.update(q)
                installHookCtx.preventInstall(args[0])
                //kotlin.runCatching {
                //  throw Exception("TrivialException")
                //}.onFailure {
                //  for (i in it.stackTrace) {
                //    log("StackTrace", i)
                //  }
                //}
              }, {
                if (installHookCtx.prevent) {
                  result = false
                }
              })
          )
        }

    "$packageName.downloadinstall.z".`class`()!!
        .apply {
          hook("d", q,
              hookBefore {
                log("downloadinstall@d", *args)
                installHookCtx.update(q)
                installHookCtx.preventInstall(args[0])
              })
        }
  }
}