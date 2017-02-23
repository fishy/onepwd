# OneKey

An Android app and Python companion script to calculate unique passwords
for different sites/apps. Compatible with and inspired by Chrome
extension [123 Password](https://chrome.google.com/webstore/detail/pahmlghhaoabdlhnkmmjbkcmdamjccjj)

## Building

### Bazel

The building system used is [Bazel](https://bazel.build).
Please [install Bazel](https://bazel.build/docs/install.html),
then run `bazel build java:onepwd --strategy=AndroidAapt=standalone`
to build the apk.

You might need the Bazel source code to get the `workspace_user.sh` script.

Run `./release.sh` to sign the apk with release key.

To run unit tests, run `bazel test ...`.

### Local files

There are certain files excluded in `.gitignore` file.
You must supply them locally in order to build:

- `WORKSPACE.user.bzl` can be generated from:
  `/path/to/bazel/scripts/workspace_user.sh`
- `release.jks` for `release.sh`,
  [more details](https://developer.android.com/studio/publish/app-signing.html#signing-manually)

## License

BSD 3-Clause, refer to LICENSE file for more details.
