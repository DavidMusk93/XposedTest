package com.example.xposedtest.wx

object JsCookie {
  private val prefix = "BgABAAgACgALABIAEwAVAAYAn"
  private val suffix = "mh4AAAA%%3D" /* %% -> % */

  val abtestCookiePoll = listOf(
      "oYeACOXHgBWmR4AyZkeAPGZHgAK",
      "oYeACOXHgBWmR4AwZkeAPeZHgAK",
      "oYeACOXHgBWmR4AvZkeAPGZHgAM",
      "oYeACOXHgBWmR4Aw5keAPOZHgAK",
      "oYeACOXHgBWmR4AvZkeAPSZHgAL",
      "oYeACOXHgBWmR4AvZkeAPeZHgAJ",
      "oYeACOXHgBWmR4Ax5keAPOZHgAM",
      "oYeACOXHgBWmR4Av5keAPGZHgAM",
      "oYeACOXHgBWmR4Ax5keAPOZHgAM",
      "oYeACOXHgBWmR4AxJkeAPeZHgAM",
      "oYeACOXHgBWmR4AxJkeAPSZHgAM",
      "oYeACOXHgBWmR4Ax5keAPOZHgAM",
      "YYeACOXHgBWmR4A0JkeAPqZHgAJ",
      "oYeACOXHgBWmR4AzJkeAPWZHgAK"
  )
  val normalUrlPattern = "%s" +
      "&ascene=3&devicetype=android-25&version=27000334&nettype=WIFI&abtest_cookie=BgABAAgACgALABIAEwAVAAYAnoYeACOXHgBWmR4AxpkeAPmZHgAJmh4AAAA%%3D&lang=zh_CN&pass_ticket=" +
      "%s" +
      "&wx_header=1"
  val urlPattern = "%s" +
      "&clicktime=" +
      "%d" +
      "&ascene=3&devicetype=android-25&version=27000334&nettype=WIFI&abtest_cookie=${prefix}" +
      "%s" +
      "${suffix}&lang=zh_CN&pass_ticket=" +
      "%s" +
      "&wx_header=1"
}