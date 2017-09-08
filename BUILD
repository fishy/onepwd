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
        "@androidsdk//com.android.support:appcompat-v7-25.3.1",
        "@androidsdk//com.android.support:preference-v7-25.3.1",
        "@androidsdk//com.android.support:support-v4-25.3.1",
    ],
)
