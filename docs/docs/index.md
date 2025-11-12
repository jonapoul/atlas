---
title: Overview
description: Overview of the Atlas Gradle Plugin's functionality and basic setup
icon: lucide/info
---

# Atlas Gradle Plugin

Atlas is a Gradle plugin for generating, configuring and curating diagrams to illustrate your project's project structure:

   1. Fully supports Gradle 9 and its configuration caching.
   2. Supports three separate frameworks as outputs:
      - **D2**: [Official docs here](https://d2lang.org/)
      - **Graphviz**: [Official docs here](https://graphviz.org/)
      - **Mermaid**: [Official docs here](https://mermaid.js.org/)
   3. Offers wide-ranging APIs for customizing your charts
   4. Supports `gradle check`-ing your generated diagrams, to validate that they match the current state of your project
   5. Supports attaching the chart generation to IntelliJ's sync button, so you don't even need to run it manually.

!!! info "Inspiration"

    This project was built as a spiritial successor to [com.vanniktech.dependency.graph.generator](https://github.com/vanniktech/gradle-dependency-graph-generator-plugin) project - but with more configurability and targeting modern Gradle releases.
