#!/bin/sh

bazel clean && \
  bazel build java:onepwd -c opt && \
  zipalign -p 4 bazel-bin/java/onepwd_unsigned.apk onepwd-tmp.apk && \
  apksigner sign --ks release.jks --out onepwd.apk onepwd-tmp.apk && \
  rm onepwd-tmp.apk
