#!/bin/sh

./gradlew modularGenerate -p samples/sample-d2
./gradlew modularGenerate -p samples/sample-graphviz
./gradlew modularGenerate -p samples/sample-mermaid
