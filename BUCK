# vim:ft=python:noet:ts=8:sw=8
android_resource(
	name = 'res',
	res = 'res',
	package = 'com.yhsif.onepwd',
)

android_library(
	name = 'src',
	srcs = glob(['src/**/*.java']),
	deps = [ ':res' ],
)

keystore(
	name = "release",
	store = "release.keystore",
	properties = "release.properties",
)

keystore(
	name = "debugkey",
	store = "debug.keystore",
	properties = "debug.properties",
)

android_binary(
	name = 'onepwd',
	manifest = 'AndroidManifest.xml',
	target = 'Google Inc.:Google APIs:17',
	keystore = ':release',
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
	target = 'Google Inc.:Google APIs:17',
	keystore = ':debugkey',
	package_type = 'debug',
	proguard_config = 'proguard.cfg',
	deps = [
		':res',
		':src',
	],
)
