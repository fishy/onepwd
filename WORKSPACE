workspace(name = "onepwd")

android_sdk_repository(
    name = "androidsdk",
    api_level = 29,
)

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

RULES_KOTLIN_TAG = "legacy-1.3.0-rc3"
RULES_KOTLIN_SHA = "54678552125753d9fc0a37736d140f1d2e69778d3e52cf454df41a913b964ede"

http_archive(
    name = "io_bazel_rules_kotlin",
    strip_prefix = "rules_kotlin-%s" % RULES_KOTLIN_TAG,
    url = "https://github.com/bazelbuild/rules_kotlin/archive/%s.zip" % RULES_KOTLIN_TAG,
    sha256 = RULES_KOTLIN_SHA,
)

load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kotlin_repositories", "kt_register_toolchains")

kotlin_repositories()
kt_register_toolchains()


RULES_JVM_EXTERNAL_TAG = "3.0"
RULES_JVM_EXTERNAL_SHA = "62133c125bf4109dfd9d2af64830208356ce4ef8b165a6ef15bbff7460b35c3a"

http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
    sha256 = RULES_JVM_EXTERNAL_SHA,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        "com.google.truth:truth:1.0",
        "junit:junit:4.12",

        "androidx.appcompat:appcompat:1.1.0",
        "androidx.biometric:biometric:1.0.1",
        "androidx.cardview:cardview:1.0.0",
        "androidx.preference:preference:1.1.0",
        "androidx.recyclerview:recyclerview:1.1.0",
        "androidx.sqlite:sqlite:2.0.1",
        "androidx.sqlite:sqlite-framework:2.0.1",
    ],
    repositories = [
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
    ],
)
