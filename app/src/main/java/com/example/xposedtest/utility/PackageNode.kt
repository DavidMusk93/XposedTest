package com.example.xposedtest.utility

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