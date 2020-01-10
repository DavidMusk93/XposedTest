package com.example.xposedtest.douyin

import java.util.regex.Pattern

object UrlHelper {

  val pattern = Pattern.compile("https://[^\"]+")

  val suffix = "&line=1&ratio=540p&media_type=4&vr_type=0&improve_bitrate=0&is_play_url=1&quality_type=11&source=PackSourceEnum_FEED"

  object Tag {
    val VIDEO = "play/?video_id"
    val LIVE = "info/?room_id"
  }

}