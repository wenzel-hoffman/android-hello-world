# 2023 Android Java Hello World (with Nix configuration)

Minimal Android Hello World that builds successfully on [NixOS 22.11][NixOS].

## Dependencies

The dependencies are provided by the [Nix configuration](default.nix).
See the configuration and the environment variables that are set there.

- Gradle (a build tool, configs are written in Groovy)
- Java JDK like OpenJDK (for the actual application code)
- Android SDK (see [default.nix] for the example of environment variables you
  have to set)

## How to build

Run from the root of the project:

``` sh
./gradlew build
```

And then you can find the built APK by this path:
`app/build/outputs/apk/debug/app-debug.apk`

## Building using Nix

The build is pretty native to how it’s done on other OSes.
There’s no Nix derivation for the Hello World APK here.
Nix only provides the dependencies
(a shell environment with the dependencies required to build the APKs).

Note that Gradle wants to modify the files inside the Android SDK directory.
So if you set the `ANDROID_SDK_ROOT` pointing somewhere to the Nix store the
build will fail as Nix store is immutable. In order to make a successful build
you have to enter the nix-shell first and then evaluate [bootstrap-nix-shell.sh]
script in your shell that will copy the contents of `ANDROID_SDK_ROOT` directory
to `local-android-sdk` and override `ANDROID_SDK_ROOT` to the full path of that
new local directory. The script will skip the copying part if the directory
already exists.

So always enter the nix-shell like this:

``` sh
nix-shell --run ". bootstrap-nix-shell.sh && ${SHELL@Q}"
```

Or just run the [enter-nix-shell.sh] script that will do exactly that for you:

``` sh
./enter-nix-shell.sh
```

N.B. Mind that the Android SDK is not a free package. In the
[Nix configuration][default.nix] there’s
`config.android_sdk.accept_license = true;` option set for the [nixpkgs].
But you also have to set `NIXPKGS_ALLOW_UNFREE=1` when entering the
nix-shell (if you don’t nix-shell will tell you what the options are).
So you can do it like this:

``` sh
NIXPKGS_ALLOW_UNFREE=1 ./enter-nix-shell.sh
```

Being inside a prepared nix-shell you can run as usual:

``` sh
./gradlew build
```

And then take the APK build by this path:
`app/build/outputs/apk/debug/app-debug.apk`

### nixpkgs pin

[nixpkgs] are pinned using [Niv]. You don’t need Niv in order to work with the
project ([auto-generated boilerplate][Niv boilerplate] does all the job).
While it’s more convenient to use Niv for updating the pin you can still do it
manually in [nix/sources.json](nix/sources.json) file.

## This is a fork

This is a fork of [hello-world-android made by 100mslive][hello-world-android].
Just a random fork I found in the internet that I was able to build
successfully. I removed all the extras from it, replaced Koltin with Java,
and added Nix configuration.

## License

[MIT](LICENSE.md)

[hello-world-android]: https://github.com/100mslive/hello-world-android

[NixOS]: https://nixos.org
[nixpkgs]: https://github.com/NixOS/nixpkgs
[Niv]: https://github.com/nmattia/niv

[bootstrap-nix-shell.sh]: bootstrap-nix-shell.sh
[enter-nix-shell.sh]: enter-nix-shell.sh
[Niv boilerplate]: nix/sources.nix
[default.nix]: default.nix
