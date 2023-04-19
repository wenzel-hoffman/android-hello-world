#! /usr/bin/env bash

set -o errexit || exit
set -o nounset

nix-shell --run ". bootstrap-nix-shell.sh && ${SHELL@Q}"
