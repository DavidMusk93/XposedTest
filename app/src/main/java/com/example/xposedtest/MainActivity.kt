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
import com.example.xposedtest.extension.toast
import com.example.xposedtest.service.DaemonService
import com.example.xposedtest.utility.ModuleHelper
import com.tbruyelle.rxpermissions2.RxPermissions
import com.topjohnwu.superuser.Shell

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
    tv!!.text = "D A V I D\nM U S K"

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

    ModuleHelper.updateList(this)

    quitButton.setOnClickListener {
      ModuleHelper.reboot(this)
    }

    startService(Intent(this, DaemonService::class.java))
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
        "Item 1 Selected".toast(this)
        val isChecked = item.isChecked
        isAppShow = !isChecked
        item.isChecked = !isChecked
        return true
      }
      R.id.item2 -> {
        "Item 2 Selected".toast(this)
        return true
      }
      R.id.item3 -> {
        "Item 3 Selected".toast(this)
        return true
      }
      else -> return super.onOptionsItemSelected(item)
    }
  }
}
