load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_android_library", "kt_jvm_library")
load("@gmaven_rules//:defs.bzl", "gmaven_artifact")

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

        gmaven_artifact("androidx.appcompat:appcompat:aar:1.0.0"),
        gmaven_artifact("androidx.preference:preference:aar:1.0.0"),
        gmaven_artifact("androidx.sqlite:sqlite:aar:2.0.0"),
        gmaven_artifact("androidx.sqlite:sqlite-framework:aar:2.0.0"),

        # indirect deps:
        gmaven_artifact("androidx.core:core:aar:1.0.0"),
        gmaven_artifact("androidx.drawerlayout:drawerlayout:aar:1.0.0"),
        gmaven_artifact("androidx.fragment:fragment:aar:1.0.0"),
        gmaven_artifact("androidx.lifecycle:lifecycle-common:jar:2.0.0"),
        gmaven_artifact("androidx.lifecycle:lifecycle-viewmodel:aar:2.0.0"),
    ],
)
