---
title: D2
description: Configuration steps for the D2 Atlas Gradle plugin
---

# D2 Usage

## Overview

See [here for the official D2 docs](https://d2lang.com/tour/intro/) or [here for an online playground](https://play.d2lang.com/). Using and executing the D2 plugin requires an existing installation of the `d2` executable on the system PATH. If it's not on the PATH, you can use the [`pathToD2Command`](#pathtod2command) config option.

[See here for D2 installation steps](https://d2lang.com/tour/install/).

D2-specific configuration is performed from the `d2 { }` block within the base `atlas` extension function:

```kotlin
plugins {
  id("dev.jonpoulton.atlas.d2") version "x.y.z"
}

atlas {
  // other Atlas config - see common usage docs

  d2 {
    animateInterval = 10
    animateLinks = true
    center = true
    direction = Direction.Down
    fileFormat = FileFormat.Svg
    groupLabelLocation = Location.Inside
    groupLabelPosition = Position.TopCenter
    layoutEngine = LayoutEngine.Dagre
    pad = 5
    pathToD2Command = "/path/to/d2"
    sketch = true
    theme = Theme.ColorblindClear
    themeDark = Theme.DarkMauve

    rootStyle {
      // ...
    }

    globalProps {
      // ...
    }

    layoutEngine {
      // ...
    }
  }
}
```

## Generated files

The D2 plugin will generate a `classes.d2` file in the root project's `atlas` folder. This contains any shared styling, used between all child charts. It will also generate a `chart.d2` and a `chart.XXX` file for each submodule, the latter's file extension depending on [fileFormat](#fileformat).

## Properties

### animateInterval

```kotlin
atlas {
  d2 {
    animateInterval = 100
  }
}
```

Only used if [fileFormat](#fileformat) is set to `FileFormat.Gif`.

### animateLinks

```kotlin
atlas {
  d2 {
    animateLinks = true
  }
}
```

When enabled, links between all module nodes will be animated, as long as they aren't solidly-styled. Disabled by default.

!!! warning

    This will only work for "animatable" [output formats](#fileformat): either SVG or GIF. If you choose a different output, you'll get a Gradle warning to tell you about it when syncing the IDE.

<div class="side-by-side">
  <figure>
    <img src="../img/d2-animateLinks-disabled.svg" alt="Disabled">
    <figcaption>Disabled</figcaption>
  </figure>

  <figure>
    <img src="../img/d2-animateLinks-enabled.svg" alt="Enabled">
    <figcaption>Enabled</figcaption>
  </figure>
</div>

### center

```kotlin
atlas {
  d2 {
    center = true
  }
}
```

This flag centers the SVG within the containing viewbox. Doesn't really give an obvious change in me experience, but ¯\_(ツ)_/¯. [See here](https://d2lang.com/tour/vars/#configuration-variables).

### direction

```kotlin
atlas {
  d2 {
    direction = Direction.Down
  }
}
```

Sets the flow direction of the dependency chart. Defaults to `Direction.Down`.

<div class="svg-grid">
  <figure class="svg-item">
    <img src="../img/d2-direction-up.svg" alt="Up">
    <figcaption>Direction.Up</figcaption>
  </figure>

  <figure class="svg-item">
    <img src="../img/d2-direction-down.svg" alt="Down">
    <figcaption>Direction.Down</figcaption>
  </figure>

  <figure class="svg-item">
    <img src="../img/d2-direction-left.svg" alt="Left">
    <figcaption>Direction.Left</figcaption>
  </figure>

  <figure class="svg-item">
    <img src="../img/d2-direction-right.svg" alt="Right">
    <figcaption>Direction.Right</figcaption>
  </figure>
</div>

### fileFormat

```kotlin
atlas {
  d2 {
    fileFormat = FileFormat.Svg
  }
}
```

Defaults to SVG. Available options:

```kotlin
FileFormat.Svg
FileFormat.Png
FileFormat.Pdf
FileFormat.Pptx
FileFormat.Gif
FileFormat.Ascii
```

!!! warning

    Only SVG and ASCII outputs can be run on all (known?) machines without further supporting software. On my dev machine at least, trying to run PNG, PDR, PPTX or GIF output causes an automatic download attempt of Chromium. This is because D2 depends on [PlayWright](https://playwright.dev/docs/intro) to do the conversion. Annoyingly, D2 will try to start the download/install without asking first... [See here for other people complaining about this](https://github.com/terrastruct/d2/issues/2502#issuecomment-3305144085).

For reference, an ASCII chart looks like below. It (hopefully obviously) doesn't support more complex features like coloring, animation, etc. It is pretty cool though!

```
      ┌─────────────┐
      │:android:app │
      │             │
      └─────────────┘
           │   │
      ┌────┘   └───────┐
      │                │
      │                ▼
      │          ┌────────────┐
      │          │:kotlin:kmp │
      │          │            │
      │          └────────────┘
      │             │  │   │
      │   ┌─────────┘  │   └───┐
      │   │            │       │
      ▼   ▼            ▼       │
 ┌─────────────┐ ┌──────┐      │
 │:android:lib │ │:java │      │
 │             │ │      │      │
 └─────────────┘ └──────┘      │
      │   │          │         │
      │   └──────────│─────┐   │
      │              │     │   │
      └─────┐  ┌─────┘     │   │
            │  │           │   │
            ▼  ▼           ▼   ▼
         ┌───────┐     ┌────────────┐
         │:other │     │:kotlin:jvm │
         │       │     │            │
         └───────┘     └────────────┘
```

### groupLabelLocation & groupLabelPosition

```kotlin
atlas {
  groupModules = true

  d2 {
    groupLabelLocation = Location.Outside
    groupLabelPosition = Position.TopCenter
  }
}
```

Only does anything if `atlas.groupModules = true`.

<div class="svg-grid">
  <figure class="svg-item">
    <img src="../img/d2-groupLabel-border-left.svg" alt="Border center left">
    <figcaption>Border center left</figcaption>
  </figure>

  <figure class="svg-item">
    <img src="../img/d2-groupLabel-inside-bottomright.svg" alt="Inside bottom right">
    <figcaption>Inside bottom right</figcaption>
  </figure>

  <figure class="svg-item">
    <img src="../img/d2-groupLabel-outside-bottomcenter.svg" alt="Outside bottom center">
    <figcaption>Outside bottom center</figcaption>
  </figure>

  <figure class="svg-item">

  </figure>
</div>

### pad

```kotlin
atlas {
  d2 {
    pad = 100
  }
}
```

Should probably be called `padding` for clarity, but kept as `pad` for consistency with the underlying D2 config parlance. Units are in pixels, and default value is 100.

<div class="side-by-side">
  <figure>
    <img src="../img/d2-pad-0.svg" alt="0">
    <figcaption>0 padding</figcaption>
  </figure>

  <figure>
    <img src="../img/d2-pad-200.svg" alt="200">
    <figcaption>200 padding</figcaption>
  </figure>
</div>

### pathToD2Command

```kotlin
atlas {
  d2 {
    pathToD2Command = "/custom/path/to/d2"
  }
}
```

By default, Atlas will try to call `d2` from the system path. Use this to call from a custom installation directory instead.

### sketch

```kotlin
atlas {
  d2 {
    sketch = true
  }
}
```

Draws the chart in an excalidraw-like format, with pseudo-handwritten font and shaded backgrounds on shapes. Defaults to false.

!!! warning

    For awareness: enabling this property will inflate the size of the generated SVG file by a factor of ~4.5: the example below is 71kB, and disabling the flag drops it down to 15kB. It does look pretty nice, though.

![D2 sketch](img/d2-sketch.svg)

### theme & themeDark

```kotlin
atlas {
  d2 {
    theme = Theme.ShirleyTemple
    themeDark = Theme.DarkMauve
  }
}
```

D2 comes with a suite of lovely built-in color schemes which you can apply to your charts. If you generate an SVG, you can also apply a separate dark theme to allow the file to support browsers in both dark and light screen display modes. Alternatively, you can specify one of the two dark themes in the `theme` parameter to force an always-dark theme, regardless of website/browser settings.

[See here for the full list of themes](https://d2lang.com/tour/themes/) in the D2 documentation. Some examples are shown below, where the bottom-right one will toggle between light and dark based on the browser theme. Try switching the theme from the site toolbar (or change in your browser's settings) to see!

!!! info

    If you set a specific color for a module type using the `moduleTypes` API, it will overwrite the theme color for that node. Same for link colors.


!!! tip

    Remember also that you can override the chart's background color using [rootStyle's](#rootstyle) `fill` property.

<div class="svg-grid">
  <figure class="svg-item">
    <img src="../img/d2-theme-default.svg" alt="Default">
    <figcaption>Default</figcaption>
  </figure>
  <figure class="svg-item">
    <img src="../img/d2-theme-shirley.svg" alt="Shirley Temple">
    <figcaption>Shirley Temple</figcaption>
  </figure>
  <figure class="svg-item">
    <img src="../img/d2-theme-darkmauve.svg" alt="Dark Mauve">
    <figcaption>Dark Mauve</figcaption>
  </figure>
  <figure class="svg-item">
    <img src="../img/d2-theme-both.svg" alt="Light/Dark">
    <figcaption>Aubergine / Dark Flagship Terrastruct</figcaption>
  </figure>
</div>

## Functions

### layoutEngine

```kotlin
atlas {
  d2 {
    layoutEngine {
      // only call one of the below config functions!
      // if you call multiple, only the last one will be used.
      dagre {
        nodeSep = 60
        edgeSep = 20
      }

      elk {
        algorithm = ElkAlgorithm.Layered
        edgeNodeBetweenLayers = 40
        nodeNodeBetweenLayers = 70
        nodeSelfLoop = 50
      }

      tala {
        // nothing yet!
      }
    }
  }
}
```

Defines the underlying engine used by D2 to organise the module nodes in each chart. [See this link in the D2 docs for more detailed information](https://d2lang.com/tour/layouts/). The available options are:

- **Dagre**: default option.
- **Elk**: Framework from Eclipse for diagram generation - also supported by [Mermaid](./usage-mermaid.md).
- **Tala**: Technically supported but it's closed source, so you need an installation of this engine on your machine from somewhere other than the public D2 installation. Only included here because it's in the D2 docs ¯\_(ツ)_/¯. Since it's a private engine, I've no idea how it's supposed to look and I don't have an example screenshot for you. Sorry-not-sorry!

Screenshots below are with all default settings.

<div class="side-by-side">
  <figure>
    <figcaption>Dagre</figcaption>
    <img src="../img/d2-layoutEngine-dagre.svg" alt="Dagre">
  </figure>

  <figure>
    <figcaption>Elk</figcaption>
    <img src="../img/d2-layoutEngine-elk.svg" alt="Elk">
  </figure>
</div>

### rootStyle

```kotlin
atlas {
  d2 {
    rootStyle {
      fill = "white"
      fillPattern = FillPattern.Grain
      stroke = "firebrick"
      strokeWidth = 5
      strokeDash = 3
      doubleBorder = true
    }
  }
}
```

A set of style properties to be applied to the chart itself. The most common one in my experience is `fill`, which you can set to `transparent` or any other CSS color or hex string.

[Check the D2 docs for the possible values of these properties](https://d2lang.com/tour/style/#root). All are optional.

### globalProps

```kotlin
atlas {
  d2 {
    globalProps {
      arrowType = ArrowType.Circle
      fillArrowHeads = false
      font = Font.Mono
      fontSize = 15
    }
  }
}
```

Style properties to be applied to all nodes (module shapes) and links by default, unless overridden by `linkTypes` or `moduleTypes` config.

!!! note

    Be aware that `font` can only take the value `Mono`. If you want the default font, leave it alone or set to null.

!!! tip

    As a bonus, in `globalProps` you can also make use of D2's wonderfully-complicated "globs" feature to apply some style to all matching nodes/links in the chart. An example from the sample-d2 project in this repo, which sets all text on link labels to black:

    ```kotlin
    atlas {
      d2 {
        globalProps {
          put("(** -> **)[*].style.font-color", "black")
        }
      }
    }
    ```

    I'm not going to document all this in here, but [take a look at the D2 docs if you're interested](https://d2lang.com/tour/globs/). If you want to add them to your chart, `globalProps` is probably(?) the best place for it. This will add the glob property to the global `classes.d2` file, which gets auto-imported into all module chart diagrams.
