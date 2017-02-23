#!/bin/sh

bazel build java:onepwd --strategy=AndroidAapt=standalone
zipalign -v -p 4 bazel-bin/java/onepwd_unsigned.apk onepwd-unsigned-aligned.apk
apksigner sign --ks release.jks --out onepwd.apk onepwd-unsigned-aligned.apk
rm onepwd-unsigned-aligned.apk
