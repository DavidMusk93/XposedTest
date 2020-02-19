package com.example.xposedtest.module.miui

import android.net.Uri
import android.util.Log
import com.example.xposedtest.utility.getField
import com.example.xposedtest.utility.setField

class MarketInstallHookContext(
    var nameOfPackage: String = "",
    var nameOfApkUri: String = "",
    var prevent: Boolean = false
) {

  companion object {

    val TAG = "MarketInstallHookContext"
    val indexOfPackage: Int = 0
    val indexOfApkUri: Int = 2
  }

  fun update(clazz: Class<*>) {
    if (nameOfApkUri.isEmpty()) {
      clazz.apply {
        for (i in declaredFields.withIndex()) {
          when (i.index) {
            MarketInstallHookContext.indexOfPackage -> {
              nameOfPackage = i.value.name
            }
            MarketInstallHookContext.indexOfApkUri -> {
              nameOfApkUri = i.value.name
            }
          }
        }
      }
    }
  }

  fun preventInstall(obj: Any?) {
    obj ?: return
    prevent = false
    val pkg = obj.getField<String>(nameOfPackage) ?: return
    if (Config.Package.preventInstallAppList.contains(pkg)) {
      Log.i(TAG, "prevent $pkg")
      obj.setField(nameOfApkUri, Uri.EMPTY)
      prevent = true
    }
  }
}