load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_test")

kt_jvm_test(
    name = "hmac_md5_test",
    size = "small",
    srcs = ["com/yhsif/onepwd/HmacMd5Test.kt"],
    test_class = "com.yhsif.onepwd.HmacMd5Test",
    deps = [
        "//:hmac_md5",

        "@com_google_guava_guava//jar",
        "@com_google_truth_truth//jar",
        "@junit_junit//jar",
    ],
)
