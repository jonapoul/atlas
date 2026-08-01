#!/bin/sh

SCRIPT_DIR="$(dirname "$0")"
cd "$SCRIPT_DIR/.." || exit

rm -rf .venv
python3 -m venv .venv
# shellcheck source=/dev/null
. .venv/bin/activate
cd docs || exit
rm -rf site .cache
pip install -r requirements.txt
