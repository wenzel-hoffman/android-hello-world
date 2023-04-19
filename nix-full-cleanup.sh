#! /usr/bin/env bash

# The script cleans up everything.
# Meant to be used with nix-shell only.

set -o errexit || exit
set -o nounset
set -o xtrace

LOCAL_SDK_DIR_NAME=local-android-sdk

SCRIPT_DIR=$(dirname -- "${BASH_SOURCE[0]}")
cd -- "$SCRIPT_DIR"

nix-shell --run "(
set -o errexit || exit
set -o nounset
set -o xtrace

# Need the SDK first to run the Gradle ‘clean’ task
. bootstrap-nix-shell.sh

# Clean all builds
./gradlew clean

# Nuke the SDK copy of the SDK from the Nix store
if [[ -d ${LOCAL_SDK_DIR_NAME@Q} ]]; then
    rm -rf -- ${LOCAL_SDK_DIR_NAME@Q}
fi
)"
