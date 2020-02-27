package com.example.xposedtest.module.miui

import com.example.xposedtest.module.wx.PackageNode

object Settings {

  operator fun invoke(): PackageNode {
    return android["settings"]
  }

  val android = PackageNode(null, "com.android")
      .apply {
        this["settings"] = PackageNode(this, "settings")
            .apply {
              this["device"] = PackageNode(this, "device")
                  .apply {
                    Set("MiuiMyDeviceSettings")
                    Set("s")
                    Set("r")
                  }

              Set("MiuiSettings")

              this["development"] = PackageNode(this, "development")
                  .apply { Set("DevelopmentSettings") }
            }

        this["a"] = PackageNode(this, "a")
            .apply { Set("a") }
      }
}
