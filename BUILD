load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_android_library", "kt_jvm_library")

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

        "@androidx_appcompat_appcompat_1_0_0//aar",
        "@androidx_preference_preference_1_0_0//aar",

        # indirect deps:
        "@androidx_core_core_1_0_0//aar",
        "@androidx_drawerlayout_drawerlayout_1_0_0//aar",
        "@androidx_fragment_fragment_1_0_0//aar",
        "@androidx_lifecycle_lifecycle_common_2_0_0//jar",
        "@androidx_lifecycle_lifecycle_viewmodel_2_0_0//aar",
    ],
)
