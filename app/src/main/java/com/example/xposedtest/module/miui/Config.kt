package com.example.xposedtest.module.miui

import com.example.xposedtest.utility.cast

object Config {

  object Package {

    val protectAppList = listOf(
        "com.example.xposedtest",
        "com.example.miinfo" // Test case
    )

    val preventInstallAppList = listOf(
        "com.ss.android.ugc.aweme",
        "com.smile.gifmaker"
    )

    fun isProtectApp(pkg: Any?): Boolean {
      pkg.cast<String>()?.apply {
        if (protectAppList.contains(this)) {
          return true
        }
      }
      return false
    }
  }

  object Preference {

    val filterList = listOf(
        "pref_key_update_notification",
        "pref_key_auto_update_via_wifi"
    )

    val keyOfAutoUpdateMarket = "pref_key_auto_update_market"
  }
}