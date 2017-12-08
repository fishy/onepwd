workspace(name = "onepwd")

android_sdk_repository(
    name = "androidsdk",
    api_level = 26,
)

git_repository(
    name = "org_pubref_rules_kotlin",
    remote = "https://github.com/pubref/rules_kotlin.git",
    commit = "cfee5aabd0bec50f8debfa49952a368b60c4565c",
)
load("@org_pubref_rules_kotlin//kotlin:rules.bzl", "kotlin_repositories")
kotlin_repositories()

git_repository(
    name = "gmaven_rules",
    remote = "https://github.com/aj-michael/gmaven_rules",
    commit = "ccf6e13ba9357e6845179fe90e78b0fa24bd9f2b",
)
load("@gmaven_rules//:gmaven.bzl", "gmaven_rules")
gmaven_rules()

maven_jar(
    name = "com_google_truth_truth",
    artifact = "com.google.truth:truth:0.28",
    sha1 = "0a388c7877c845ff4b8e19689dda5ac9d34622c4",
)

maven_jar(
    name = "com_google_guava_guava",
    artifact = "com.google.guava:guava:19.0",
    sha1 = "6ce200f6b23222af3d8abb6b6459e6c44f4bb0e9",
)

maven_jar(
    name = "junit_junit",
    artifact = "junit:junit:4.12",
    sha1 = "2973d150c0dc1fefe998f834810d68f278ea58ec",
)

maven_jar(
    name = "org_hamcrest_hamcrest_core",
    artifact = "org.hamcrest:hamcrest-core:1.3",
    sha1 = "42a25dc3219429f0e5d060061f71acb49bf010a0",
)
