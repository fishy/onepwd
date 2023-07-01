# OneKey

An Android app and Python companion script to calculate unique passwords
for different sites/apps. Compatible with and inspired by Chrome
extension [123 Password](https://chrome.google.com/webstore/detail/pahmlghhaoabdlhnkmmjbkcmdamjccjj)

[![Get it on Google Play](https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png)](https://play.google.com/store/apps/details?id=com.yhsif.onepwd)

## Building

### Bazel

The building system used is [Bazel](https://bazel.build).
Please [install Bazel](https://bazel.build/docs/install.html),
then run `bazel build :app`
to build the apk.

The Bazel rules depends on the `ANDROID_HOME` environment variable.

Run [`tools/release.sh`](tools/release.sh) to sign the apk with a release key.
[More details](https://developer.android.com/studio/publish/app-signing.html#signing-manually).

To run unit tests, run `bazel test ...`.

## License

BSD 3-Clause, refer to [LICENSE file](LICENSE) for more details.

Google Play and the Google Play logo are trademarks of Google LLC.
