package com.example.xposedtest.utility

import android.os.Build

fun is_N(): Boolean {
  return Build.VERSION.SDK_INT == Build.VERSION_CODES.N
}
