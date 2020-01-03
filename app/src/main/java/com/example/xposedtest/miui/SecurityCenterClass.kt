package com.example.xposedtest.miui

import com.example.xposedtest.wx.PackageNode

object SecurityCenter {

  operator fun invoke(): PackageNode {
    return miui
  }

  // com.miui.common.persistence.RemoteProvider
  // com.miui.permcenter.install.AdbInstallActivity
  // com.miui.d.c.a
  private val miui = PackageNode(null, "com.miui")
      .apply {
        this["common"] = PackageNode(this, "common")
            .apply {
              this["persistence"] = PackageNode(this, "persistence")
                  .apply {
                    Set("RemoteProvider")
                    RemoteProvider = this["RemoteProvider"]
                  }
            }
        this["permcenter"] = PackageNode(this, "permcenter")
            .apply {
              this["install"] = PackageNode(this, "install")
                  .apply {
                    Set("AdbInstallActivity")
                    AdbInstallActivity = this["AdbInstallActivity"]
                  }
            }
        this["d"] = PackageNode(this, "d")
            .apply {
              this["c"] = PackageNode(this, "c")
                  .apply {
                    Set("a")
                    a = this["a"]
                  }
            }
      }

  var RemoteProvider: PackageNode
  var AdbInstallActivity: PackageNode
  var a: PackageNode

}