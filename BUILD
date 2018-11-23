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

        "@android_arch_lifecycle_common_1_1_1//jar",
        "@android_arch_lifecycle_viewmodel_1_1_1//aar",
        "@com_android_support_appcompat_v7_28_0_0//aar",
        "@com_android_support_drawerlayout_28_0_0//aar",
        "@com_android_support_preference_v7_28_0_0//aar",
        "@com_android_support_support_annotations_28_0_0//jar",
        "@com_android_support_support_compat_28_0_0//aar",
        "@com_android_support_support_core_utils_28_0_0//aar",
        "@com_android_support_support_core_ui_28_0_0//aar",
        "@com_android_support_support_fragment_28_0_0//aar",
    ],
)
