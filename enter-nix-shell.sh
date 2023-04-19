#! /usr/bin/env bash

set -o errexit || exit
set -o nounset

# Runs whatever $SHELL you have inside a nix-shell with exported $ANDROID_SDK_ROOT that is pointing
# to a local mutable copy (Gradle wants to write to it).
nix-shell --run ". bootstrap-nix-shell.sh && ${SHELL@Q}"
