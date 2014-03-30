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

# Download it yourself at:
# http://search.maven.org/#artifactdetails%7Corg.hamcrest%7Chamcrest-core%7C1.3%7Cjar
prebuilt_jar(
	name = 'hamcrest-core',
	binary_jar = 'tests/libs/hamcrest-core-1.3.jar',
)

# Download it yourself at:
# http://search.maven.org/#artifactdetails%7Cjunit%7Cjunit%7C4.11%7Cjar
prebuilt_jar(
	name = 'junit',
	binary_jar = 'tests/libs/junit-4.11.jar',
	deps = [
		":hamcrest-core",
	],
)

java_test(
	name = 'test',
	srcs = glob(['tests/src/**/*.java']),
	deps = [
		":src",
		":junit",
	],
)
