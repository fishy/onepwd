workspace(name = "onepwd")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")


STARDOC_TAG = "0.4.0"
STARDOC_SHA = "36b8d6c2260068b9ff82faea2f7add164bf3436eac9ba3ec14809f335346d66a"

http_archive(
    name = "io_bazel_stardoc",
    strip_prefix = "stardoc-%s" % STARDOC_TAG,
    urls = [
        "https://github.com/bazelbuild/stardoc/archive/%s.zip" % STARDOC_TAG,
    ],
    sha256 = STARDOC_SHA,
)

load("@io_bazel_stardoc//:setup.bzl", "stardoc_repositories")
stardoc_repositories()


RULES_PROTO_VERSION = "97d8af4dc474595af3900dd85cb3a29ad28cc313"
RULES_PROTO_SHA = "602e7161d9195e50246177e7c55b2f39950a9cf7366f74ed5f22fd45750cd208"

http_archive(
    name = "rules_proto",
    strip_prefix = "rules_proto-%s" % RULES_PROTO_VERSION,
    urls = [
        "https://mirror.bazel.build/github.com/bazelbuild/rules_proto/archive/%s.tar.gz" % RULES_PROTO_VERSION,
        "https://github.com/bazelbuild/rules_proto/archive/%s.tar.gz" % RULES_PROTO_VERSION,
    ],
    sha256 = RULES_PROTO_SHA,
)

load("@rules_proto//proto:repositories.bzl", "rules_proto_dependencies", "rules_proto_toolchains")
rules_proto_dependencies()
rules_proto_toolchains()


RULES_PKG_TAG = "0.5.0"
RULES_PKG_SHA = "353b20e8b093d42dd16889c7f918750fb8701c485ac6cceb69a5236500507c27"

http_archive(
    name = "rules_pkg",
    urls = [
        "https://mirror.bazel.build/github.com/bazelbuild/rules_pkg/releases/download/%s/rules_pkg-%s.tar.gz" % (RULES_PKG_TAG, RULES_PKG_TAG),
        "https://github.com/bazelbuild/rules_pkg/releases/download/%s/rules_pkg-%s.tar.gz" % (RULES_PKG_TAG, RULES_PKG_TAG),
    ],
    sha256 = RULES_PKG_SHA,
)

load("@rules_pkg//:deps.bzl", "rules_pkg_dependencies")
rules_pkg_dependencies()


RULES_JVM_EXTERNAL_TAG = "4.1"
RULES_JVM_EXTERNAL_SHA = "f36441aa876c4f6427bfb2d1f2d723b48e9d930b62662bf723ddfb8fc80f0140"

http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    urls = [
        "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
    ],
    sha256 = RULES_JVM_EXTERNAL_SHA,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")
maven_install(
    artifacts = [
        "com.google.truth:truth:1.1.3",
        "junit:junit:4.13.2",
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3",

        "androidx.activity:activity:1.2.4",
        "androidx.appcompat:appcompat:1.3.1",
        "androidx.biometric:biometric:1.1.0",
        "androidx.cardview:cardview:1.0.0",
        "androidx.core:core-ktx:1.6.0",
        "androidx.fragment:fragment-ktx:1.3.6",
        "androidx.preference:preference:1.1.1",
        "androidx.recyclerview:recyclerview:1.2.1",
        "androidx.room:room-compiler:2.2.6",
        "androidx.room:room-runtime:2.2.6",
        "com.google.android.material:material:1.4.0",
    ],
    repositories = [
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
    ],
    version_conflict_policy = "pinned",
    maven_install_json = "@onepwd//:maven_install.json",
)


load("@maven//:defs.bzl", "pinned_maven_install")
pinned_maven_install()


RULES_ANDROID_TAG = "v0.1.1"
RULES_ANDROID_SHA = "cd06d15dd8bb59926e4d65f9003bfc20f9da4b2519985c27e190cddc8b7a7806"

http_archive(
    name = "rules_android",
    urls = [
        "https://github.com/bazelbuild/rules_android/archive/%s.zip" % RULES_ANDROID_TAG,
    ],
    sha256 = RULES_ANDROID_SHA,
    strip_prefix = "rules_android-%s" % RULES_ANDROID_TAG[1:],
)

load("@rules_android//android:rules.bzl", "android_sdk_repository")
android_sdk_repository(
    name = "androidsdk",
    api_level = 31,
    # This is the latest version with dx.jar
    build_tools_version = "30.0.3",
)


RULES_KOTLIN_TAG = "v1.5.0-beta-2"
RULES_KOTLIN_SHA = "e4185409c787c18f332ae83a73827aab6e77058a48ffee0cac01123408cbc89a"

http_archive(
    name = "io_bazel_rules_kotlin",
    urls = [
        "https://github.com/bazelbuild/rules_kotlin/releases/download/%s/rules_kotlin_release.tgz" % RULES_KOTLIN_TAG,
    ],
    sha256 = RULES_KOTLIN_SHA,
)

load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kotlin_repositories", "kt_register_toolchains")
kotlin_repositories()
register_toolchains("//:kotlin_toolchain")
