workspace(name = "onepwd")

android_sdk_repository(
    name = "androidsdk",
    api_level = 29,
)

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

RULES_KOTLIN_VERSION = "7543a917cda483c5856ec79b590343c9669eacd6"
http_archive(
    name = "io_bazel_rules_kotlin",
    strip_prefix = "rules_kotlin-%s" % RULES_KOTLIN_VERSION,
    url = "https://github.com/bazelbuild/rules_kotlin/archive/%s.tar.gz" % RULES_KOTLIN_VERSION,
    sha256 = "d335be363937ed62b5758cfdb33910ee52429835685345ef6b9f009c2bff9a9d",
)
load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kotlin_repositories", "kt_register_toolchains")

kotlin_repositories()
kt_register_toolchains()

RULES_JVM_EXTERNAL_TAG = "2.8"
http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
    sha256 = "79c9850690d7614ecdb72d68394f994fef7534b292c4867ce5e7dec0aa7bdfad",
)
load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        "com.google.truth:truth:0.43",
        "junit:junit:4.12",

        "androidx.activity:activity:1.0.0",
        "androidx.appcompat:appcompat:1.1.0",
        "androidx.biometric:biometric:1.0.0-rc01",
        "androidx.cardview:cardview:1.0.0",
        "androidx.preference:preference:1.1.0",
        "androidx.recyclerview:recyclerview:1.0.0",
        "androidx.savedstate:savedstate:1.0.0",
        "androidx.sqlite:sqlite:2.0.1",
        "androidx.sqlite:sqlite-framework:2.0.1",
    ],
    repositories = [
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
    ],
)
