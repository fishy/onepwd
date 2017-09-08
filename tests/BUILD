load("@org_pubref_rules_kotlin//kotlin:rules.bzl", "kotlin_test")

kotlin_test(
    name = "hmac-md5-test",
    size = "small",
    srcs = ["com/yhsif/onepwd/HmacMd5Test.kt"],
    test_class = "com.yhsif.onepwd.HmacMd5Test",
    deps = [
        "//:hmac-md5",
    ],
    java_deps = [
        "@com_google_guava_guava//jar",
        "@com_google_truth_truth//jar",
        "@junit_junit//jar",
        "@org_hamcrest_hamcrest_core//jar",
    ],
)
