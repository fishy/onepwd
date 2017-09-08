workspace(name = "onepwd")

android_sdk_repository(
    name = "androidsdk",
    api_level = 25,
)

git_repository(
    name = "org_pubref_rules_kotlin",
    # TODO: change to upstream after
    # https://github.com/pubref/rules_kotlin/pull/32
    # is merged.
    remote = "https://github.com/fishy/rules_kotlin.git",
    commit = "d8efadbd024b321fd881035d207fd2bdc7f30602",
)

load("@org_pubref_rules_kotlin//kotlin:rules.bzl", "kotlin_repositories")
kotlin_repositories()

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
