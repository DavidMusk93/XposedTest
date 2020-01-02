package com.example.xposedtest.miui

import com.example.xposedtest.utility.is_N

class UpgradeContext {

  val s1 = if (is_N()) "Eq" else "Vc"

  val s2 = if (is_N()) "Dr" else "Ul"

  var obj: Any? = null

}