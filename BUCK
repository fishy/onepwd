android_resource(
  name = 'res',
  res = 'res',
  package = 'com.yhsif.onepwd',
)

android_library(
  name = 'src',
  srcs = glob(['src/**/*.java']),
  deps = [
    ':res',
    '//libs:android-support-v4',
    '//libs:android-support-v7-appcompat',
    '//libs:android-support-v7-appcompat-res',
  ],
  visibility = [
    '//tests:',
  ],
)

keystore(
  name = "releasekey",
  store = "release.keystore",
  properties = "release.properties",
)

keystore(
  name = "debugkey",
  store = "debug.keystore",
  properties = "debug.properties",
)

android_binary(
  name = 'release',
  manifest = 'AndroidManifest.xml',
  keystore = ':releasekey',
  package_type = 'release',
  proguard_config = 'proguard.cfg',
  deps = [
    ':res',
    ':src',
  ],
)

android_binary(
  name = 'debug',
  manifest = 'AndroidManifest.xml',
  keystore = ':debugkey',
  package_type = 'debug',
  proguard_config = 'proguard.cfg',
  deps = [
    ':res',
    ':src',
  ],
)

python_binary(
  name = 'onepwd',
  main = 'onepwd.py',
)
