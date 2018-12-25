workspace(name = "onepwd")

android_sdk_repository(
    name = "androidsdk",
    api_level = 28,
)

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

RULES_KOTLIN_VERSION = "cab5eaffc2012dfe46260c03d6419c0d2fa10be0"
http_archive(
    name = "io_bazel_rules_kotlin",
    strip_prefix = "rules_kotlin-%s" % RULES_KOTLIN_VERSION,
    url = "https://github.com/bazelbuild/rules_kotlin/archive/%s.tar.gz" % RULES_KOTLIN_VERSION,
    sha256 = "3ecce7e3292c07a258e8c6ea542fc62760468c210d7c88e0439209c7ea361891",
)
load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kotlin_repositories", "kt_register_toolchains")
kotlin_repositories()
kt_register_toolchains()

GMAVEN_TAG = "20181212-2"
http_archive(
    name = "gmaven_rules",
    strip_prefix = "gmaven_rules-%s" % GMAVEN_TAG,
    url = "https://github.com/bazelbuild/gmaven_rules/archive/%s.tar.gz" % GMAVEN_TAG,
    sha256 = "33027de68db6a49a352f83808fa9898c4930d39aa6fb0edc6bb3d3eec6e2bc7d",
)
load("@gmaven_rules//:gmaven.bzl", "gmaven_rules")
gmaven_rules()

maven_jar(
    name = "com_google_truth_truth",
    artifact = "com.google.truth:truth:0.42",
    sha1 = "b5768f644b114e6cf5c3962c2ebcb072f788dcbb",
)

maven_jar(
    name = "com_google_guava_guava",
    artifact = "com.google.guava:guava:27.0.1-android",
    sha1 = "b7e1c37f66ef193796ccd7ea6e80c2b05426182d",
)

maven_jar(
    name = "junit_junit",
    artifact = "junit:junit:4.12",
    sha1 = "2973d150c0dc1fefe998f834810d68f278ea58ec",
)
