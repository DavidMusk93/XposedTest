package com.example.xposedtest.xposed

import com.example.xposedtest.utility.DebugUtil
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

fun hookBefore(action: XC_MethodHook.MethodHookParam.() -> Unit): XC_MethodHook {
  return object : XC_MethodHook() {
    override fun beforeHookedMethod(param: MethodHookParam) {
      param.apply(action)
    }
  }
}

fun hookAfter(action: XC_MethodHook.MethodHookParam.() -> Unit): XC_MethodHook {
  return object : XC_MethodHook() {
    override fun afterHookedMethod(param: MethodHookParam) {
      param.apply(action)
    }
  }
}

// How to inject data?
fun hookBoth(beforeAction: XC_MethodHook.MethodHookParam.() -> Unit, afterAction: XC_MethodHook.MethodHookParam.() -> Unit): XC_MethodHook {
  return object : XC_MethodHook() {
    override fun beforeHookedMethod(param: MethodHookParam) {
      param.apply(beforeAction)
    }

    override fun afterHookedMethod(param: MethodHookParam) {
      param.apply(afterAction)
    }
  }
}

fun gLog(tag: String, vararg msg: Any?) = DebugUtil.log("$tag ${msg.map { "$it" }.joinToString()}")

// How to forward vararg?
fun Class<*>.hook(methodName: String, vararg parameterTypesAndCallback: Any?) = XposedHelpers.findAndHookMethod(this, methodName, *parameterTypesAndCallback)

fun Class<*>.hookCtor(vararg parameterTypesAndCallback: Any?) = XposedHelpers.findAndHookConstructor(this, *parameterTypesAndCallback)