# OneKey

An Android app and Python companion script to calculate unique passwords
for different sites/apps. Compatible with and inspired by Chrome
extension [123 Password](https://chrome.google.com/webstore/detail/pahmlghhaoabdlhnkmmjbkcmdamjccjj)

## Building

### Buck

The building system used is [Buck](https://github.com/facebook/buck).
Please [install Buck manually](https://buckbuild.com/setup/install.html#manual-build),
then run `buck build release` or `buck build debug` to build the apk.

To run unit tests, run `buck fetch test` once to get third party
dependencies, then run `buck test test` to run the tests.

### Local files

There are certain files excluded in `.gitignore` file.
You must supply them locally in order to build:

- `debug.keystore` and `debug.properties` for `debug` target
- `release.keystore` and `release.properties` for `release` target
- `ANDROID_HOME` environment variable to specify Android SDK location

## License

BSD 3-Clause, refer to LICENSE file for more details.
