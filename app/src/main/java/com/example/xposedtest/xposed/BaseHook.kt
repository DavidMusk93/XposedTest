package com.example.xposedtest.xposed

interface BaseHook {
  fun attachBaseContext()
  fun onResume(tag: String)
}