load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_android_library", "kt_jvm_library")
load("@rules_jvm_external//:defs.bzl", "artifact")

PACKAGE = "com.yhsif.onepwd"
MANIFEST = "AndroidManifest.xml"
MANIFEST_VALUES = {"PACKAGE": PACKAGE}

HMAC_SRCS = ["src/com/yhsif/onepwd/HmacMd5.kt"]

kt_jvm_library(
    name = "hmac_md5",
    srcs = HMAC_SRCS,
    visibility = [
        "//tests:__pkg__",
    ],
)

android_binary(
    name = "app",
    custom_package = PACKAGE,
    manifest = MANIFEST,
    manifest_values = MANIFEST_VALUES,
    deps = [
        ":onepwd",
    ],
)

kt_android_library(
    name = "onepwd",
    srcs = glob(
        ["src/**/*.kt"],
        exclude = HMAC_SRCS,
    ),
    custom_package = PACKAGE,
    manifest = MANIFEST,
    resource_files = glob(["res/**/*"]),
    deps = [
        ":hmac_md5",

        artifact("androidx.appcompat:appcompat"),
        artifact("androidx.biometric:biometric"),
        artifact("androidx.cardview:cardview"),
        artifact("androidx.preference:preference"),
        artifact("androidx.recyclerview:recyclerview"),
        artifact("androidx.sqlite:sqlite"),
        artifact("androidx.sqlite:sqlite-framework"),

        # indirect deps:
        artifact("androidx.activity:activity"),
        artifact("androidx.core:core"),
        artifact("androidx.drawerlayout:drawerlayout"),
        artifact("androidx.fragment:fragment"),
        artifact("androidx.lifecycle:lifecycle-common"),
        artifact("androidx.lifecycle:lifecycle-viewmodel"),
        artifact("androidx.savedstate:savedstate"),
    ],
)
