workspace(name = "onepwd")

android_sdk_repository(
    name = "androidsdk",
    api_level = 31,
    # This is the latest version with dx.jar
    build_tools_version = "30.0.3",
)

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

RULES_JVM_EXTERNAL_TAG = "4.2"

RULES_JVM_EXTERNAL_SHA = "cd1a77b7b02e8e008439ca76fd34f5b07aecb8c752961f9640dea15e9e5ba1ca"

http_archive(
    name = "rules_jvm_external",
    sha256 = RULES_JVM_EXTERNAL_SHA,
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    urls = [
        "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
    ],
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        "androidx.activity:activity:1.2.4",
        "androidx.appcompat:appcompat:1.3.1",
        "androidx.biometric:biometric:1.1.0",
        "androidx.cardview:cardview:1.0.0",
        "androidx.core:core-ktx:1.6.0",
        "androidx.fragment:fragment-ktx:1.3.6",
        "androidx.preference:preference:1.1.1",
        "androidx.recyclerview:recyclerview:1.2.1",
        "androidx.sqlite:sqlite:2.2.0",
        "androidx.sqlite:sqlite-framework:2.2.0",
        "com.google.android.material:material:1.4.0",
        "com.google.truth:truth:1.1.3",
        "junit:junit:4.13.2",
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3",
    ],
    maven_install_json = "@onepwd//:maven_install.json",
    repositories = [
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
    ],
    version_conflict_policy = "pinned",
)

load("@maven//:defs.bzl", "pinned_maven_install")

pinned_maven_install()

RULES_KOTLIN_TAG = "1.6.0-RC-1"

RULES_KOTLIN_SHA = "f1a4053eae0ea381147f5056bb51e396c5c494c7f8d50d0dee4cc2f9d5c701b0"

http_archive(
    name = "io_bazel_rules_kotlin",
    sha256 = RULES_KOTLIN_SHA,
    urls = [
        "https://github.com/bazelbuild/rules_kotlin/releases/download/%s/rules_kotlin_release.tgz" % RULES_KOTLIN_TAG,
    ],
)

load("@io_bazel_rules_kotlin//kotlin:repositories.bzl", "kotlin_repositories")

kotlin_repositories()

register_toolchains("//:kotlin_toolchain")
