fastlane documentation
----

# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```sh
xcode-select --install
```

For _fastlane_ installation instructions, see [Installing _fastlane_](https://docs.fastlane.tools/#installing-fastlane)

# Available Actions

## Android

### android build_and_upload

```sh
[bundle exec] fastlane android build_and_upload
```

Runs linting, tests, and builds APK and AAB before uploading to Firebase

### android lint

```sh
[bundle exec] fastlane android lint
```

Runs linting

### android test

```sh
[bundle exec] fastlane android test
```

Runs all the tests

### android build_apk

```sh
[bundle exec] fastlane android build_apk
```

Builds APK

### android build_aab

```sh
[bundle exec] fastlane android build_aab
```

Builds AAB

### android upload_to_firebase

```sh
[bundle exec] fastlane android upload_to_firebase
```

Uploads APK to Firebase App Distribution

----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.

More information about _fastlane_ can be found on [fastlane.tools](https://fastlane.tools).

The documentation of _fastlane_ can be found on [docs.fastlane.tools](https://docs.fastlane.tools).