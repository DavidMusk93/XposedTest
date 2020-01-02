package com.example.xposedtest.zuiyou

import com.example.xposedtest.wx.PackageNode

object zuiyou {

  private val zuiyou_ = PackageNode(null, "cn.xiaochuankeji.tieba")

  object ui {
    private val ui_ = PackageNode(zuiyou_, "ui")

    val PageMeFragment: String by lazy { PackageNode(ui_, "home.page.PageMeFragment").toString() }
  }

  object network {
    private val network_ = PackageNode(null, "com.izuiyou.network")

    val NetCrypto: String by lazy { PackageNode(network_, "NetCrypto").toString() }
  }

  // naked class
  object defpackage {
    // private val defpackage_ = PackageNode(null, "defpackage")
    private val defpackage_ = null

    val cz1: String by lazy { PackageNode(defpackage_, "cz1").toString() }
    val fy3: String by lazy { PackageNode(defpackage_, "fy3").toString() }
    val by3: String by lazy { PackageNode(defpackage_, "by3").toString() }
    val gy3: String by lazy { PackageNode(defpackage_, "gy3").toString() }
  }
}