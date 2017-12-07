load("@org_pubref_rules_kotlin//kotlin:rules.bzl", "kotlin_android_library", "kotlin_library")

PACKAGE = "com.yhsif.onepwd"
MANIFEST = "AndroidManifest.xml"
MANIFEST_VALUES = {"PACKAGE": PACKAGE}

HMAC_SRCS = ["src/com/yhsif/onepwd/HmacMd5.kt"]

kotlin_library(
    name = "hmac-md5",
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

kotlin_android_library(
    name = "onepwd",
    srcs = glob(
        ["src/**/*.kt"],
        exclude = HMAC_SRCS,
    ),
    custom_package = PACKAGE,
    manifest = MANIFEST,
    resource_files = glob(["res/**/*"]),
    deps = [
        ":hmac-md5",
    ],
    aar_deps = [
        "@android_arch_core_common_1_0_0//jar",
        "@android_arch_lifecycle_common_1_0_3//jar",
        "@android_arch_lifecycle_runtime_1_0_3//aar",
        "@com_android_support_appcompat_v7_27_0_1//aar",
        "@com_android_support_preference_v7_27_0_1//aar",
        "@com_android_support_support_compat_27_0_1//aar",
        "@com_android_support_support_core_utils_27_0_1//aar",
        "@com_android_support_support_core_ui_27_0_1//aar",
        "@com_android_support_support_fragment_27_0_1//aar",
        "@com_android_support_support_vector_drawable_27_0_1//aar",
    ],
)
