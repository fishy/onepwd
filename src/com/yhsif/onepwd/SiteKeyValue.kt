package com.yhsif.onepwd

sealed class SiteKeyValue {
  public abstract fun getFull(): String
  public abstract fun getKey(): String
}

class SiteKeyEmpty() : SiteKeyValue() {
  public override fun getFull(): String = ""
  public override fun getKey(): String = ""
}

class SiteKeyPackage(val pkg: String) : SiteKeyValue() {
  public override fun getFull(): String = pkg

  public override fun getKey(): String {
    val segments = pkg.split(".")
    if (segments.size > 1) {
      return segments[1]
    }
    return pkg
  }
}

class SiteKeyHost(val host: String) : SiteKeyValue() {
  public override fun getFull(): String = host

  public override fun getKey(): String {
    val segments = host.split(".")
    if (segments.size > 1) {
      return segments[segments.size - 2]
    }
    return host
  }
}
