package com.example.xposedtest.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class HookClass(val pkg: String)