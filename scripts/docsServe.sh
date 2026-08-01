#!/bin/sh

SCRIPT_DIR="$(dirname "$0")"
cd "$SCRIPT_DIR/.." || exit

# shellcheck source=/dev/null
. .venv/bin/activate
cd docs || exit
zensical serve -f zensical.toml
