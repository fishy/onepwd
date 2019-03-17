load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_test")
load("@rules_jvm_external//:defs.bzl", "artifact")

kt_jvm_test(
    name = "hmac_md5_test",
    size = "small",
    srcs = ["com/yhsif/onepwd/HmacMd5Test.kt"],
    test_class = "com.yhsif.onepwd.HmacMd5Test",
    deps = [
        "//:hmac_md5",

        artifact("com.google.truth:truth"),
        artifact("junit:junit"),
    ],
)
