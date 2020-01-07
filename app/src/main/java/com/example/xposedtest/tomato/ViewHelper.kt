package com.example.xposedtest.tomato

import android.view.View
import com.example.xposedtest.utility.cast
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ViewHelper {

  val delayPool = listOf<Long>(200, 400, 600, 800, 1000, 1200, 1400, 1600)

  fun clickView(view: Any?) {
    view.cast<View>()?.let { v ->
      MainScope().launch {
        delay(delayPool.shuffled().last())
        v.performClick()
      }
    }
  }

}