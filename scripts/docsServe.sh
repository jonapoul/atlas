#!/bin/sh

SCRIPT_DIR="$(dirname "$0")"
cd "$SCRIPT_DIR/.." || exit

. .venv/bin/activate
cd docs
zensical serve -f mkdocs.yml
