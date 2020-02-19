package com.example.xposedtest.module.miui

object Config {

  object Package {

    val protectionAppList = listOf(
        "com.example.xposedtest",
        "com.example.miinfo" // Test case
    )

    val preventInstallAppList = listOf(
        "com.ss.android.ugc.aweme",
        "com.smile.gifmaker"
    )
  }

  object Preference {

    val filterList = listOf(
        "pref_key_update_notification",
        "pref_key_auto_update_via_wifi"
    )

    val keyOfAutoUpdateMarket = "pref_key_auto_update_market"
  }
}