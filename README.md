# 2023 Android Java Hello World (with Nix configuration)

Minimal Android Hello World that builds successfully on [NixOS 22.11][NixOS].

## Dependencies

The dependencies are provided by the [Nix configuration](default.nix).

- Gradle (a build tool, configs are written in Groovy)
- Java (for the actual application code)

## Building using Nix

The build is pretty native to how it’s done on other OSes.
There’s no Nix derivation for the Hello World APK here.
Nix only provides the dependencies
(a shell environment with the dependencies required to build the APKs).

Note that Gradle wants to modify the files inside the Android SDK directory.
So if you set the `ANDROID_SDK_ROOT` pointing somewhere to the Nix store the
build will fail as Nix store is immutable. In order to make a successful build
you have to enter the nix-shell first and the evaluate [bootstrap-nix-shell.sh]
script in your shell that will copy the contents of `ANDROID_SDK_ROOT` directory
to `local-android-sdk` and override `ANDROID_SDK_ROOT` to the full path of that
new local directory. The script will skip the copying part if the directory
exists.

So always enter the nix-shell like this:

``` sh
nix-shell --run ". bootstrap-nix-shell.sh && ${SHELL@Q}"
```

Or just run the [enter-nix-shell.sh] script that will do exactly that for you:

``` sh
./enter-nix-shell.sh
```

N.B. Mind that the Android SDK is not a free package. In the Nix configuration
there’s `config.android_sdk.accept_license = true;` option set for the
[nixpkgs]. But you also have to set `NIXPKGS_ALLOW_UNFREE=1` when entering the
nix-shell (if you don’t nix-shell will tell you what the options are).
So you can do it like this:

``` sh
NIXPKGS_ALLOW_UNFREE=1 ./enter-nix-shell.sh
```

## This is a fork

This is a fork of [hello-world-android made by 100mslive][Fork of].
Just a random fork I found in the internet that I was able to build
successfully. I removed all the extras from it and added Nix configuration.

## License

[MIT](LICENSE)

[hello-world-android]: https://github.com/100mslive/hello-world-android
[NixOS]: https://nixos.org
[nixpkgs]: https://github.com/NixOS/nixpkgs
[bootstrap-nix-shell.sh]: bootstrap-nix-shell.sh
[enter-nix-shell.sh]: enter-nix-shell.sh
