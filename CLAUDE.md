# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Atlas is a Gradle plugin for generating diagrams of modular project dependencies. It supports three rendering frameworks: D2, Graphviz, and Mermaid. The plugin is built with Kotlin and targets Gradle 9+ with full configuration cache support.

**Key Characteristics:**
- A single published module (`atlas-plugin`), plus convention plugins in `build-logic/`
- One plugin id, `dev.jonpoulton.atlas`. Users pick frameworks by configuring `d2 { }`, `graphviz { }` and/or `mermaid { }` in the `atlas` extension - any combination is allowed
- Java 21 minimum (see the root `.java-version` file)
- Uses [Blueprint](https://github.com/jonapoul/blueprint) (`dev.jonpoulton.blueprint:core`) for Gradle DSL shortcuts, in both `build-logic` and `atlas-plugin`
- Uses Gradle configuration cache and parallel execution
- Published to Maven Central under `dev.jonpoulton.atlas`

## Commands

### Building and Testing
```bash
./gradlew build                    # Build everything
./gradlew test                     # Run all tests
./gradlew check                    # Run all checks including detekt, licensee and the ABI dump
```

### Single Test Execution
Tests use JUnit 6. To run a single test class:
```bash
./gradlew :atlas-plugin:test --tests "atlas.core.WriteProjectTreeTest"
```

To run a specific test method:
```bash
./gradlew :atlas-plugin:test --tests "atlas.core.WriteProjectTreeTest.Single links for diamond"
```

### Code Quality
```bash
./gradlew detektCheck              # Run static analysis
./gradlew licensee                 # Validate dependency licenses

# Formatting, using ktfmt with Google style
./scripts/ktfmt.sh                 # format files changed since main
./scripts/ktfmt.sh check           # check-only
./scripts/ktfmt.sh --force         # all files
```

### Documentation
This task will take a while, so don't run unless really necessary:
```bash
./gradlew dokkaGeneratePublicationHtml --rerun-tasks --no-build-cache --no-configuration-cache       # Generate API docs to docs/api/
```

### Plugin Development
The plugin is tested from its own test source set (`atlas-plugin/src/test`), so tests can see `internal` declarations. Tests use Gradle TestKit to verify plugin behavior in realistic project structures.

When the public API changes, refresh the ABI dump with `./gradlew :atlas-plugin:updateKotlinAbi`, otherwise `check` fails.

## Architecture

### Module Structure

1. **atlas-plugin**: the whole plugin, published as `dev.jonpoulton.atlas:atlas-plugin`
   - `atlas.core`: `AtlasPlugin` (the single entry point), `AtlasExtension`, the shared
     `ProjectTypeSpec`/`LinkTypeSpec`, and the framework-agnostic tasks `CollateProjectTypes`,
     `CollateProjectLinks`, `WriteProjectType`, `WriteProjectLinks`, `WriteProjectTree`,
     `WriteReadme`, `CheckFileDiff`
   - `atlas.d2`: `D2Spec` config, `D2Tasks` registration, tasks `WriteD2Chart`, `WriteD2Classes`,
     `ExecD2`, `SvgToPng`
   - `atlas.graphviz`: `GraphvizSpec` config, `GraphvizTasks` registration, tasks
     `WriteGraphvizChart`, `WriteGraphvizLegend`, `ExecGraphviz`
   - `atlas.mermaid`: `MermaidSpec` config, `MermaidTasks` registration, tasks `WriteMermaidChart`,
     `WriteMarkdownLegend`
   - Anything not part of the user-facing API is Kotlin `internal` - there's no opt-in annotation
   - Tests live in `src/test`, with `ScenarioTest` + `atlas.test.scenarios.*` driving TestKit builds

2. **build-logic**: Convention plugins for the build itself
   - Located in `build-logic/src/main/kotlin/atlas/gradle/`
   - Convention plugins: `ConventionKotlin`, `ConventionPublish`, `ConventionDetekt`, etc.
   - Applied via `atlas.convention.*` plugin IDs

### Shared Project and Link Types

`projectTypes` and `linkTypes` are declared once and rendered by every configured framework. Each
spec carries the union of all three frameworks' style properties: shared meanings get one property
mapping to several framework keys (`fontColor` -> D2 `style.font-color`, Graphviz `fontcolor`,
Mermaid `color`), and only genuine clashes are prefixed (`d2Shape`, `graphvizShape`). `StyleProperties`
holds one attribute map per framework and records which DSL properties were set, so `AtlasPlugin`
can warn about properties no configured framework will read.

### Plugin Application Pattern

The plugin is applied to the root project and automatically propagates to all subprojects.

`AtlasPlugin` applies in two phases:
1. **Root project**: creates the extension and registers `CollateProjectTypes`/`CollateProjectLinks`, which aggregate data from all subprojects
2. **Subprojects**: auto-applied via `subprojects {}`, registers `WriteProjectType`, `WriteProjectLinks`, `WriteProjectTree`

Framework tasks are registered later, in the root project's `afterEvaluate`, because which frameworks are switched on isn't known until the root build script has run. Root is always evaluated before its children, so subproject tasks still exist by the time anything runs.

Each subproject writes its local project information to `build/atlas/*.json`, then root tasks collate these into project-wide diagrams.

### Task Execution Flow

1. Each subproject runs `WriteProjectType` → outputs project type classification to JSON
2. Each subproject runs `WriteProjectLinks` → outputs direct dependencies to JSON
3. Root runs `CollateProjectTypes` → aggregates all project types
4. Root runs `CollateProjectLinks` → aggregates all project links
5. Each subproject runs `WriteProjectTree` → consumes collated links to compute full dependency tree
6. Framework-specific tasks (D2/Graphviz/Mermaid) generate diagram files from the aggregated data
7. Each subproject runs `WriteReadme` → injects every configured framework's diagram into its README

### Gradle Isolated Projects Support

The plugin will one day be updated to support Gradle's isolated projects feature (see issue #307). This requires careful handling of project references and task dependencies to avoid cross-project configuration. For now though, this isn't a top priority.

### Testing Approach

Tests use a scenario-based pattern:
- Each scenario (e.g., `DiamondGraph`, `TriangleGraph`) defines a complete multi-module project structure
- `ScenarioTest.runScenario()` creates a temporary Gradle project with that structure
- Tests invoke Gradle tasks via TestKit and verify generated outputs
- Scenarios are reused across multiple test classes for different plugin variants

Example: `DiamondGraph` creates a 4-module project (top → mid-a/mid-b → bottom) to test transitive dependency tracking.

## Key Concepts

### Project Types
Projects are classified by `ProjectTypeSpec` using matchers:
- `pathContains`: substring match on project path
- `pathMatches`: regex match on project path
- `hasPluginId`: detect applied Gradle plugins

Quick-access helpers (`androidApp()`, `kotlinJvm()`, `useDefaults()`, ...) live in `org.gradle.kotlin.dsl.ProjectTypeDsl`.

### Link Types
Project dependencies are classified by `LinkTypeSpec`:
- Based on Gradle configuration name (e.g., "api", "implementation")
- `style` uses the shared `atlas.core.LinkStyle`; frameworks which can't draw a given style fall back to the closest one and warn at configuration time

### Path Transforms
The `PathTransformSpec` allows regex-based transformations of project paths in generated diagrams (e.g., removing common prefixes).

## File Locations

- Generated diagrams: `atlas/<framework>/` in each project, e.g. `atlas/d2/chart.d2`. Legends only in the root project
- Per-project data: `build/atlas/*.json` in each subproject
- Test fixtures: `atlas-plugin/src/test/kotlin/atlas/test/scenarios/`
- Documentation: `docs/` (MkDocs-based, deployed to GitHub Pages)

## Important Properties

The Java version is read from the root `.java-version` file (via Blueprint's `javaVersion()`/`jvmTarget()`), and is also what CI's `setup-java` steps and the `gradle:*-jdk*` docker images pin.

From `gradle.properties`:
- `atlas.minimumGradleVersion`: Minimum Gradle version
- `org.gradle.configuration-cache`: Configuration cache is enabled
- `org.gradle.parallel`: Parallel execution is enabled
