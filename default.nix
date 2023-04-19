{ pkgs ? import <nixpkgs> { config.android_sdk.accept_license = true; } }:
let
  androidSdk = pkgs.androidenv.androidPkgs_9_0.androidsdk;
  buildToolsVersion = (builtins.elemAt pkgs.androidenv.androidPkgs_9_0.build-tools 0).version;
in
pkgs.mkShell rec {
  buildInputs = [
    androidSdk
    pkgs.glibc
    pkgs.jdk
    pkgs.gradle
  ];

  # The build won’t work because Gradle wants the SDK to be mutable.
  # Run “source bootstrap-nix-shell.sh” after entering nix-shell.
  # It will copy the SDK locally and override the variables below in your nix-shell session.
  # Mind that you have to run it every time you enter nix-shell.

  ANDROID_SDK_ROOT = "${androidSdk}/libexec/android-sdk";

  # For debugging
  __GRADLE_AAPT2_PATH = "${ANDROID_SDK_ROOT}/build-tools/${buildToolsVersion}/aapt2";

  GRADLE_OPTS = "-Dorg.gradle.project.android.aapt2FromMavenOverride=${__GRADLE_AAPT2_PATH}";

  # There’s no such directory
  # ANDROID_NDK_ROOT = "${ANDROID_SDK_ROOT}/ndk-bundle";
}
