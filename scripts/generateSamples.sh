#!/bin/sh

SCRIPT_DIR="$(dirname "$0")"
cd "$SCRIPT_DIR/.." || exit
./gradlew atlasGenerate -p samples/sample-d2
./gradlew atlasGenerate -p samples/sample-graphviz
./gradlew atlasGenerate -p samples/sample-mermaid
