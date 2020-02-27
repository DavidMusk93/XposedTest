package com.example.xposedtest

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Process
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.xposedtest.extension.toast
import com.example.xposedtest.service.CancelNoticeService
import com.example.xposedtest.xposed.UpdateModule
import com.tbruyelle.rxpermissions2.RxPermissions
import com.topjohnwu.superuser.Shell
import java.lang.reflect.Field
import java.lang.reflect.Method

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

    RxPermissions(this).request(
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

    startService(Intent(this, CancelNoticeService::class.java))
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

  companion object {
    val TAG = "XposedDetection@sun"
  }
}
