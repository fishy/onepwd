PACKAGE = "com.yhsif.onepwd"
MANIFEST = "AndroidManifest.xml"
MANIFEST_VALUES = {"PACKAGE": PACKAGE}

HMAC_SRCS = ["src/com/yhsif/onepwd/HmacMd5.java"]

java_library(
    name = "hmac-md5",
    srcs = HMAC_SRCS,
    visibility = [
        "//tests:__pkg__",
    ],
)

android_binary(
    name = "app",
    srcs = glob(
        ["src/**/*.java"],
        exclude = HMAC_SRCS,
    ),
    custom_package = PACKAGE,
    manifest = MANIFEST,
    manifest_values = MANIFEST_VALUES,
    resource_files = glob(["res/**/*"]),
    deps = [
        ":hmac-md5",
        "@androidsdk//com.android.support:appcompat-v7-25.3.1",
        "@androidsdk//com.android.support:preference-v7-25.3.1",
        "@androidsdk//com.android.support:support-v4-25.3.1",
    ],
)

py_binary(
    name = "onepwd",
    srcs = ["onepwd.py"],
)
