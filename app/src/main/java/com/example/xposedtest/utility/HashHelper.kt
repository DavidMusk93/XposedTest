package com.example.xposedtest.utility

import java.security.MessageDigest

object HashHelper {

  fun getDigest(type: String): MessageDigest {
    val s = type.toUpperCase()
    when (s) {
      "MD5" ->
        return MessageDigest.getInstance(s)
      "SHA-256" ->
        return MessageDigest.getInstance(s)
      else ->
        throw IllegalArgumentException("Known digest type")
    }
  }

}