#!/bin/sh
set -eu

# Runs the sample generation itself. Intended to be run inside the docker image (see
# docker/docker-compose.yml), where D2 and Graphviz are pinned to the versions the committed
# samples were generated with. Use scripts/generateSamples.sh to drive it from the host.

SCRIPT_DIR="$(dirname "$0")"
cd "$SCRIPT_DIR/.." || exit 1

GRADLE="${GRADLE_CMD:-gradle -Dorg.gradle.java.home=/opt/java/openjdk/}"

for sample in d2 graphviz mermaid; do
  # shellcheck disable=SC2086 # GRADLE deliberately carries arguments
  $GRADLE atlasGenerate -p "samples/sample-$sample"
done
