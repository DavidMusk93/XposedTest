package com.example.xposedtest.module.wx

class PackageNode(val parent: PackageNode?, val name: String) {

  private val children = mutableMapOf<String, PackageNode>()

  override fun toString(): String {
    if (parent == null)
      return name
    return "$parent.$name"
  }

  operator fun get(k: String): PackageNode {
    return children[k] ?: PackageNode(null, "")
  }

  operator fun set(k: String, node: PackageNode) {
    children[k] = node
  }

  fun Get(k: String): PackageNode? {
    return children[k]
  }

  fun Set(k: String) {
    children[k] = PackageNode(this, k)
  }

}

object mm {

  private val tencent_ = PackageNode(null, "com.tencent")
  private val mm_ = PackageNode(tencent_, "mm")

  object tinker {
    private val tinker_ = PackageNode(tencent_, "tinker")
    val TinkerApplication: String by lazy { PackageNode(tinker_, "loader.app.TinkerApplication").toString() }
    val TinkerInstaller: String by lazy { PackageNode(tinker_, "lib.e.c").toString() }
  }

  object kernel {
    private val kernel_ = PackageNode(mm_, "kernel")
    val g by lazy { PackageNode(kernel_, "g").toString() }
    val a by lazy { PackageNode(kernel_, "a").toString() }

    object c {
      private val c_ = PackageNode(kernel_, "c")
      val a: String by lazy { PackageNode(c_, "a").toString() }
      val c: String by lazy { PackageNode(c_, "c").toString() }
    }
  }

  object model {
    private val model_ = PackageNode(mm_, "model")
    val ay by lazy { PackageNode(model_, "ay").toString() }
  }

  object storage {
    private val storage_ = PackageNode(mm_, "storage")
    // userinfo & userinfo2
    val z by lazy { PackageNode(storage_, "z").toString() }
  }

  object wcdb {
    private val wcdb_ = PackageNode(tencent_, "wcdb")
    val SQLiteDatabase: String by lazy { PackageNode(wcdb_, "database.SQLiteDatabase").toString() }
  }

  object am {
    private val am_ = PackageNode(mm_, "am")
    val a: String by lazy { PackageNode(am_, "a").toString() }
    val b: String by lazy { PackageNode(am_, "b").toString() }
  }

  object compatible {
    private val compatible_ = PackageNode(mm_, "compatible")

    object util {
      private val util_ = PackageNode(compatible_, "util")
      val k: String by lazy { PackageNode(util_, "k").toString() }
    }
  }

}
