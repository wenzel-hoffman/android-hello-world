#! /usr/bin/env bash

# Read comments in “default.nix” for details.
#
# You are supposed to source this file being inside nix-shell like this:
#   source bootstrap-nix-shell.sh

LOCAL_SDK_DIR_NAME=local-android-sdk

SCRIPT_DIR=$(dirname -- "${BASH_SOURCE[0]}") || exit
ABSOLUTE_SCRIPT_DIR=$(cd -- "$SCRIPT_DIR" && pwd) || exit

# An absolute path to a full copy of Android SDK.
# This directory might not exist yet.
# Will be created below if it’s not existing yet.
LOCAL_SDK_DIR=${ABSOLUTE_SCRIPT_DIR}/${LOCAL_SDK_DIR_NAME}

# Verifying that we are in nix-shell with all dependencies provided
(
    set -o xtrace || exit
    [[ -v ANDROID_SDK_ROOT && -d $ANDROID_SDK_ROOT ]] || exit
    [[ -v __GRADLE_AAPT2_PATH && -f $__GRADLE_AAPT2_PATH ]] || exit
    [[ -v GRADLE_OPTS && -n $GRADLE_OPTS ]] || exit
) || exit

# Checking if the copy is already done
if [[ ! -d $LOCAL_SDK_DIR ]]; then
    (
        set -o xtrace || exit

        # Copy the whole SDK dir followign the symlinks recursively in order to
        # be able to change the permissions for everything inside.
        # Gradle wants to write to it.
        cp -rL -- "$ANDROID_SDK_ROOT" "$LOCAL_SDK_DIR" || exit

        ls -lah "$LOCAL_SDK_DIR" || exit
        chmod +w -R -- "$LOCAL_SDK_DIR" || exit # Make it writable
    ) || exit
fi

OLD_ANDROID_SDK_ROOT=$ANDROID_SDK_ROOT

export ANDROID_SDK_ROOT=${LOCAL_SDK_DIR}
export GRADLE_OPTS=${GRADLE_OPTS//${OLD_ANDROID_SDK_ROOT}/${ANDROID_SDK_ROOT}}

(
    # Verifying that everything is ready
    set -o xtrace || exit
    [[ -v ANDROID_SDK_ROOT && -d $ANDROID_SDK_ROOT ]] || exit
    [[ -v GRADLE_OPTS && -n $GRADLE_OPTS ]] || exit
) || exit
