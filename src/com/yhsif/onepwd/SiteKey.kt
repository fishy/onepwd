package com.yhsif.onepwd

sealed class SiteKey {
    abstract fun getFull(): String
    abstract fun getKey(): String

    object Empty : SiteKey() {
        override fun getFull(): String = ""
        override fun getKey(): String = ""
    }

    data class Package(val pkg: String) : SiteKey() {
        override fun getFull(): String = pkg

        override fun getKey(): String {
            val segments = pkg.split(".")
            if (segments.size > 1) {
                return segments[1]
            }
            return pkg
        }
    }

    data class Host(val host: String) : SiteKey() {
        override fun getFull(): String = host

        override fun getKey(): String {
            val segments = host.split(".")
            if (segments.size > 1) {
                return segments[segments.size - 2]
            }
            return host
        }
    }
}
