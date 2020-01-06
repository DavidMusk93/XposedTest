package com.example.xposedtest

import android.Manifest
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.xposedtest.extension.toByteArray
import com.example.xposedtest.extension.toast
import com.example.xposedtest.utility.FsUtil.Companion.pathLazy
import com.example.xposedtest.utility.basename
import com.example.xposedtest.utility.getField
import com.example.xposedtest.xposed.UpdateModule
import com.example.xposedtest.xposed.gLog
import com.izuiyou.network.NetCrypto
import com.tbruyelle.rxpermissions2.RxPermissions
import com.topjohnwu.superuser.Shell
import com.xiaomeng.workphone.Mp3Converter
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.zip.ZipFile

class MainActivity : AppCompatActivity() {

  init {
    /* Shell.Config methods shall be called before any shell is created
     * This is the why in this example we call it in a static block
     * The followings are some examples, check Javadoc for more details */
    Shell.Config.setFlags(Shell.FLAG_REDIRECT_STDERR)
    Shell.Config.verboseLogging(BuildConfig.DEBUG)
    Shell.Config.setTimeout(10)
  }

  private var tv: TextView? = null

  private val quitButton: Button by lazy { findViewById<Button>(R.id.btn_quit) }

  private val component: ComponentName
    get() = ComponentName(this, MainActivity::class.java)

  private var isAppShow: Boolean
    get() {
      val state = this.packageManager.getComponentEnabledSetting(component)
      return state == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT || state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
    }
    set(isHide) = this.packageManager.setComponentEnabledSetting(component,
        if (isHide)
          PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        else
          PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
        PackageManager.DONT_KILL_APP)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    tv = findViewById(R.id.tv)
    tv!!.text = "davidmusk"

    val quitButton = findViewById<Button>(R.id.btn_quit)
    quitButton.setOnClickListener { }

    // if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {
    //   ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.PROCESS_OUTGOING_CALLS), 1)
    // }

    RxPermissions(this).
        request(
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE)
        .subscribe {
          if (!it) {
            "Request permissions failed!".toast(this@MainActivity)
            Process.killProcess(Process.myPid())
          }
        }.dispose()

    UpdateModule.update(applicationInfo.sourceDir)

    quitButton.setOnClickListener {
      UpdateModule.reboot(this)
    }

    // runCatching {
    //   checkZipEntry("/sdcard/com.one.apk")
    // }.onFailure { gLog("@CheckZipEntryError", "${it.message}") }

    checkClassLoader()

    // dumpSystemInfo("${Environment.getExternalStorageDirectory()}/systemInfo.cfg")
    Thread {
      val mp3Output by pathLazy("/sdcard/xposedtest/mp3")
      runCatching {
        gLog("@VoiceDecode", "launch sh")
        val sh = Runtime.getRuntime().exec("sh")
        val os = sh.outputStream
        os.write("find /sdcard/tencent/MicroMsg/e452060fb0744ce80bb1dc300aa114b5/ -name \"*.amr\"".toByteArray())
        os.write('\n'.toInt())
        os.flush()
        sh.inputStream.bufferedReader().apply {
          var amr: String
          var mp3 = ""
          while (!readLine().also { amr = it }.isNullOrEmpty()) {
            mp3 = mp3Output+"/"+amr.basename()?.replace(".amr", ".mp3")
            File(mp3).let {
              if (it.exists()) {
                gLog("@AmrToMp3", "$mp3 already exists")
                return@let
              }
              gLog("@AmrToMp3", "$amr to $mp3")
              // val ret = JNI.decode(amr, mp3, mp3+"_")
              val ret = Mp3Converter.decode(amr, mp3)
              if (ret != 0) {
                gLog("@VoiceDecode", "decode failed")
              }
            }
          }
          if (mp3.isEmpty())
            gLog("@VoiceDecode", "can not find any amr files")
        }
      }.onFailure { gLog("@VoiceDecode", "convert failed: ${it.message}") }
      // Anything below is jumped
    }.start()

    Thread {
      runCatching {
        val url = "https://dmapi.izuiyou.com/danmaku/list?sign=v2-969fcaaf0e37f8011d00d88f5c63d346"
        listOf(0xBE, 0x04, 0x0D, 0x1C, 0x1C, 0xAC, 0xAC, 0xBB, 0xBB, 0xCB, 0xDA, 0xEA, 0xEA, 0xF9, 0xF9, 0x0A, 0x87, 0x98, 0xCE, 0xA1, 0x23, 0x7D, 0x6E, 0x6C, 0x9B, 0x92, 0x17, 0xC4, 0x3F, 0x80, 0x80, 0x46, 0x1D, 0x1F, 0x7C, 0xAC, 0x4A, 0xAE, 0x06, 0xFE, 0x51, 0x44, 0x22, 0x1C, 0xF4, 0xE3, 0xBF, 0xAA, 0x91, 0xFC, 0x57, 0xD4, 0xC8, 0x8B, 0xAE, 0xBB, 0x44, 0x18, 0xE2, 0x2B, 0xDA, 0x38, 0x8E, 0x23, 0x99, 0x11, 0x8E, 0xB9, 0xB9, 0xA5, 0x40, 0x4B, 0xC9, 0xCF, 0x45, 0xFA, 0x99, 0xB2, 0x5C, 0xF1, 0x0B, 0xE4, 0x71, 0x12, 0x28, 0x28, 0x0B, 0x9C, 0xF7, 0xAE, 0x12, 0xC7, 0xD1, 0xB2, 0x8D, 0xF8, 0x99, 0xA6, 0xC9, 0x2F, 0x86, 0x95, 0x10, 0xEB, 0xF8, 0x7B, 0x76, 0x33, 0x7E, 0xA4, 0x45, 0x8E, 0xF8, 0x4B, 0x53, 0xEE, 0x5C, 0x35, 0x87, 0xA3, 0xE7, 0x53, 0x90, 0x97, 0x4F, 0x99, 0xB1, 0x38, 0xA0, 0x1F, 0xCB, 0xCB, 0x4E, 0xFE, 0xBA, 0xF1, 0xD8, 0xD6, 0xAE, 0x62, 0x90, 0x8A, 0xD8, 0xA5, 0xE8, 0xD8, 0xA3, 0xE0, 0xB8, 0x81, 0x1C, 0x7B, 0x22, 0x10, 0x71, 0xAB, 0x4E, 0x17, 0x1A, 0x12, 0x79, 0x0C, 0x42, 0x4E, 0x80, 0xB1, 0xB0, 0xEC, 0xA6, 0xF6, 0x64, 0xA2, 0x9C, 0x6A, 0xE7, 0x0A, 0xDE, 0xB8, 0xA3, 0xA4, 0x9B, 0x8C, 0x80, 0x1D, 0x3E, 0x58, 0xEB, 0x54, 0xBF, 0x2C, 0x5E, 0xCB, 0x76, 0x25, 0xCE, 0x1E, 0x7B, 0xFA, 0x65, 0xE7, 0x7D, 0x63, 0x16, 0x15, 0x36, 0x42, 0xFC, 0xDE, 0x70, 0x2D, 0x50, 0x8A, 0x77, 0xC7, 0x49, 0x99, 0xBA, 0x94, 0x75, 0x44, 0xA8, 0x8C, 0xE1, 0x32, 0xE4, 0x7D, 0x79, 0xE1, 0x10, 0x0D, 0x3F, 0x04, 0xBB, 0xE9, 0x5D, 0x06, 0xDD, 0xF0, 0xC9, 0xA8, 0xC1, 0xF5, 0x3E, 0x47, 0x61, 0x07, 0xE9, 0x46, 0x81, 0xD1, 0x49, 0xB9, 0xC1, 0x38, 0x8F, 0x34, 0xA8, 0x12, 0xEF, 0x50, 0xA9, 0x3D, 0x8A, 0x27, 0xE6, 0x30, 0x80, 0x3D, 0xCE, 0x9C, 0xBF, 0xAA, 0x1C, 0xDF, 0xC2, 0x0C, 0x15, 0xB5, 0x90, 0xE3, 0x92, 0x68, 0xD0, 0x1D, 0x69, 0xF2, 0x50, 0xEC, 0xAE, 0x64, 0x9A, 0x84, 0x51, 0x84, 0x76, 0x5C, 0xFA, 0x42, 0x80, 0xE2, 0x7D, 0xC2, 0x57, 0x3D, 0xC3, 0x52, 0x55, 0x7C, 0xD1, 0x3E, 0x82, 0xC9, 0xBD, 0xEC, 0x19, 0x1C, 0xCA, 0x45, 0xAA, 0x29, 0x75, 0x01, 0x71, 0xFE, 0x7D, 0x86, 0x16, 0x38, 0xE9, 0x6E, 0x68, 0x0A, 0xFE, 0xA3, 0xD3, 0xC2, 0x9D, 0x70, 0x0D, 0xF8, 0x3A, 0xE8, 0xE7, 0x8C, 0xEF, 0x15, 0x84, 0x68, 0x52, 0x2D, 0x96, 0xAE)
            .toByteArray().apply {
              gLog("@sign", NetCrypto.a(url.substringBefore('?'), this))
            }
      }.onFailure { gLog("@sign", "sign failed: ${it.message}") }
    }.start()
  }

  private fun checkZipEntry(apk: String) {
    val zipFile = ZipFile(apk)
    val entries = zipFile.entries()
    gLog("@ZipEntry", "@@@ S T A R T @@@")
    while (entries.hasMoreElements()) {
      val zipEntry = entries.nextElement()
      gLog("@ZipEntry", zipEntry.name)
    }
    gLog("@ZipEntry", "@@@@@ E N D @@@@@")
  }

  private fun checkClassLoader() {
    fun log(vararg msg: Any?) = gLog("@ClassLoader", *msg)
    log(getField(classLoader, "pathList"))
  }

  private fun getField(obj: Any, name: String): Field? {
    var clz: Class<*>? = obj::class.java
    while (clz != null) {
      kotlin.runCatching {
        obj::class.java.getDeclaredField(name).let {
          if (!it.isAccessible)
            it.isAccessible = true
          return it
        }
      }.onFailure { clz = clz?.superclass }
    }
    return null
  }

  private fun getMethod(obj: Any, name: String, vararg parameterTyeps: Class<*>): Method? {
    var clz: Class<*>? = obj::class.java
    while (clz != null) {
      kotlin.runCatching {
        obj::class.java.getDeclaredMethod(name, *parameterTyeps).let {
          if (!it.isAccessible)
            it.isAccessible = true
          return it
        }
      }.onFailure { clz = clz?.superclass }
    }
    return null
  }

  // @Throws(IOException::class)
  // fun loadApkPath() {
  //   val path by pathLazy("/data/data/${BuildConfig.APPLICATION_ID}/conf")
  //   val writer = File("$path/modules.list").printWriter()
  //   packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
  //       .forEach {
  //         it.applicationInfo
  //             .let { info ->
  //               // Filter by meta data
  //               if (info.metaData != null && info.metaData.containsKey("xposedmodule")) {
  //                 writer.println(info.sourceDir)
  //                 Log.d(TAG, info.sourceDir)
  //               }
  //             }
  //       }
  //   writer.close()
  // }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    val id = item.itemId
    when (id) {
      R.id.item1 -> {
        Toast.makeText(applicationContext, "Item 1 Selected", Toast.LENGTH_SHORT).show()
        val isChecked = item.isChecked
        isAppShow = !isChecked
        item.isChecked = !isChecked
        return true
      }
      R.id.item2 -> {
        Toast.makeText(applicationContext, "Item 2 Selected", Toast.LENGTH_SHORT).show()
        return true
      }
      R.id.item3 -> {
        Toast.makeText(applicationContext, "Item 3 Selected", Toast.LENGTH_SHORT).show()
        return true
      }
      else -> return super.onOptionsItemSelected(item)
    }
  }

  private fun checkStack() {
    try {
      throw Exception("XposedDetection")
    } catch (e: Exception) {
      Log.d(TAG, "checkStack: ######start")
      for (stackTraceElement in e.stackTrace) {
        Log.d(TAG, "checkStack: #" + stackTraceElement.className + "-->" + stackTraceElement.methodName)
      }
      Log.d(TAG, "checkStack: ######end")
    }

  }

  companion object {
    val TAG = "XposedDetection@sun"
  }
}
