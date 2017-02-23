HMAC_SRCS = ["src/com/yhsif/onepwd/HmacMd5.java"]

java_library(
    name = "hmac-md5",
    srcs = HMAC_SRCS,
    visibility = [
        "//javatests:__pkg__",
    ],
)

android_binary(
    name = "onepwd",
    srcs = glob(["src/**/*.java"]),
    manifest = "AndroidManifest.xml",
    resource_files = glob(["res/**/*"]),
    deps = [
        "@androidsdk//com.android.support:appcompat-v7-25.0.0",
        "@androidsdk//com.android.support:preference-v7-25.0.0",
        "@androidsdk//com.android.support:support-v4-25.0.0",
    ],
)
