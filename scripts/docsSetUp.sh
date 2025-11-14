#!/bin/sh

SCRIPT_DIR="$(dirname "$0")"
cd "$SCRIPT_DIR/.." || exit

rm -rf .venv
python3 -m venv .venv
. .venv/bin/activate
pip install -r config/docs-requirements.txt
