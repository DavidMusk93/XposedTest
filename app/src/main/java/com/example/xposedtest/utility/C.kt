package com.example.xposedtest.utility

import android.content.DialogInterface
import android.content.pm.PackageInstaller

object C {
  val Int = Int::class.java
  val Long = Long::class.java
  val Float = Float::class.java
  val Boolean = kotlin.Boolean::class.java

  val Object = Any::class.java
  val String = String::class.java
  val ByteArray = kotlin.ByteArray::class.java
  val View = android.view.View::class.java
  val Bundle = android.os.Bundle::class.java
  val Context = android.content.Context::class.java
  val ViewGroup = android.view.ViewGroup::class.java
  val MotionEvent = android.view.MotionEvent::class.java
  val FragmentActivity = android.support.v4.app.FragmentActivity::class.java

  val Intent = android.content.Intent::class.java
  val ListView = android.widget.ListView::class.java
  val Activity = android.app.Activity::class.java
  val AdapterView = android.widget.AdapterView::class.java

  val OnClickListener = DialogInterface.OnClickListener::class.java
  val OnCancelListener = DialogInterface.OnCancelListener::class.java

  val ArrayList = java.util.ArrayList::class.java
  val StringArray = kotlin.Array<String>::class.java
  val List = kotlin.collections.List::class.java
  val LinkedList = java.util.LinkedList::class.java
  val Map = kotlin.collections.Map::class.java

  val ContentValues = android.content.ContentValues::class.java

  val MenuItem = android.view.MenuItem::class.java

  val Parcel = android.os.Parcel::class.java
  val Parcelable = android.os.Parcelable::class.java

  val IBinder = android.os.IBinder::class.java
  val KeyEvent = android.view.KeyEvent::class.java
  val ValueCallback = android.webkit.ValueCallback::class.java

  val InputStream = java.io.InputStream::class.java

  val Uri = android.net.Uri::class.java

  val DialogInterface = android.content.DialogInterface::class.java
  val ContextWrapper by lazy { android.content.ContextWrapper::class.java }
  val JSONObject by lazy { org.json.JSONObject::class.java }

  val PreferenceScreen by lazy { android.preference.PreferenceScreen::class.java }
  val Preference by lazy { android.preference.Preference::class.java }
  val Header by lazy { android.preference.PreferenceActivity.Header::class.java }

  val PackageInstaller by lazy { PackageInstaller::class.java }

  val ClassLoader by lazy { ClassLoader::class.java }
}