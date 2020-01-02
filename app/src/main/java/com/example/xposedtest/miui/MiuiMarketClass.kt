package com.example.xposedtest.miui

import com.example.xposedtest.xposed.`class`

object MiuiMarketClass {

  val MarketPreference by lazy { "com.xiaomi.market.ui.MarketPreference".`class`() }
  val AboutPreferenceActivity by lazy { "com.xiaomi.market.ui.AboutPreferenceActivity".`class`() }
  val UpdateAppsActivity by lazy { "com.xiaomi.market.ui.UpdateAppsActivity".`class`() }
  val Ibd by lazy { "com.xiaomi.market.ui.bd".`class`() }
  val qf by lazy { "com.xiaomi.market.ui.qf".`class`() }

  val PrefUtils by lazy { "com.xiaomi.market.utility.PrefUtils".`class`() }
  val PrefFile by lazy { "com.xiaomi.market.utility.PrefUtils.PrefFile".`class`() }
  val fb by lazy { "com.xiaomi.market.utility.fb".`class`() }
  val Ha by lazy { "com.xiaomi.market.utility.Ha".`class`() }
  val e by lazy { "com.xiaomi.market.utility.e".`class`() }
  val Za by lazy { "com.xiaomi.market.utility.Za".`class`() }

  val PageConfig by lazy { "com.xiaomi.market.model.PageConfig".`class`() }
  val Ba by lazy { "com.xiaomi.market.model.Ba".`class`() }
  val Ba_a by lazy { "com.xiaomi.market.model.Ba.a".`class`() }
  val DownloadInstallInfo by lazy { "com.xiaomi.market.model.DownloadInstallInfo".`class`()!! }

  val AppInstallCmdExecutor by lazy { "com.sina.weibo.sdk.cmd.AppInstallCmdExecutor".`class`() }

  val y by lazy { "com.xiaomi.market.downloadinstall.y".`class`() }
  val y_b by lazy { "com.xiaomi.market.downloadinstall.y.b".`class`() }

  // AOSP source code
  val IPackageInstallObserver by lazy { "android.content.pm.IPackageInstallObserver".`class`() }
  val IPackageInstallObserver_Stub by lazy { "android.content.pm.IPackageInstallObserver.Stub".`class`() }

  val IPackageDeleteObserver_Stub by lazy { "android.content.pm.IPackageDeleteObserver.Stub".`class`() }

  // Package Manager
  val l by lazy { "com.xiaomi.market.e.l".`class`() }

  val MarketInstallerService_b by lazy { "com.xiaomi.market.installsupport.MarketInstallerService.b".`class`() }
  val lh by lazy { "com.xiaomi.market.ui.lh".`class`()!! }

  val PreloadedAppInstallService_a by lazy { "com.xiaomi.market.service.PreloadedAppInstallService.a".`class`() }

  val downloadinstall_G by lazy { "com.xiaomi.market.downloadinstall.G".`class`() }

  val TaskManager by lazy { "com.xiaomi.market.downloadinstall.TaskManager".`class`()!! }
  val TaskStep by lazy { "com.xiaomi.market.downloadinstall.TaskManager.TaskStep".`class`()!! }
  val downloadinstall_N by lazy { "com.xiaomi.market.downloadinstall.N".`class`() }
  val downloadinstall_A by lazy { "com.xiaomi.market.downloadinstall.A".`class`() }
  val downloadinstall_M by lazy { "com.xiaomi.market.downloadinstall.M".`class`() }
  val downloadinstall_q by lazy { "com.xiaomi.market.downloadinstall.q".`class`() }

  val downloadinstall_l_b by lazy { "com.xiaomi.market.downloadinstall.l.b".`class`() }
  val market_e_q by lazy { "com.xiaomi.market.e.q".`class`() }
}