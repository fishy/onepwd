workspace(name = "onepwd")

android_sdk_repository(
    name = "androidsdk",
    api_level = 28,
)

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

RULES_KOTLIN_VERSION = "990fcc53689c8b58b3229c7f628f843a60cb9f5c"
http_archive(
    name = "io_bazel_rules_kotlin",
    strip_prefix = "rules_kotlin-%s" % RULES_KOTLIN_VERSION,
    url = "https://github.com/bazelbuild/rules_kotlin/archive/%s.tar.gz" % RULES_KOTLIN_VERSION,
    sha256 = "4e6d316bc227b293675284c85d655f8c4ccebd406499b6d25cf16ff55e6cd7fb",
)
load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kotlin_repositories", "kt_register_toolchains")

kotlin_repositories()
kt_register_toolchains()

RULES_JVM_EXTERNAL_TAG = "2.2"
http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
    sha256 = "f1203ce04e232ab6fdd81897cf0ff76f2c04c0741424d192f28e65ae752ce2d6",
)
load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        "com.google.truth:truth:0.43",
        "junit:junit:4.12",

        "androidx.appcompat:appcompat:1.0.2",
        "androidx.biometric:biometric:1.0.0-alpha04",
        "androidx.cardview:cardview:1.0.0",
        "androidx.preference:preference:1.0.0",
        "androidx.recyclerview:recyclerview:1.0.0",
        "androidx.sqlite:sqlite:2.0.1",
        "androidx.sqlite:sqlite-framework:2.0.1",
    ],
    repositories = [
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
    ],
)
