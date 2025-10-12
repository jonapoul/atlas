# sample-app-android

<!--region chart-->
```mermaid
---
config:
  layout: elk
  look: handDrawn
  theme: forest
  elk:
    mergeEdges: true
    forceNodeModelOrder: true
    nodePlacementStrategy: LINEAR_SEGMENTS
    cycleBreakingStrategy: INTERACTIVE
    considerModelOrder: PREFER_EDGES
  themeVariables:
    background: #FFF
    fontFamily: arial
    lineColor: #55FF55
    primaryBorderColor: #FF5555
    primaryColor: #ABC123
    darkMode: true
    fontSize: 20px
    defaultLinkColor: #5555FF
---
graph TD
  app_android["app android"]
  lib_android["lib android"]
  lib_java["lib java"]
  lib_kotlin_jvm["lib kotlin jvm"]
  lib_kotlin_mp["lib kotlin mp"]
  style app_android fill:limegreen,fillcolor:limegreen
  style lib_android fill:lightgreen,fillcolor:lightgreen
  style lib_java fill:orange,fillcolor:orange
  style lib_kotlin_jvm fill:mediumorchid,fillcolor:mediumorchid
  style lib_kotlin_mp fill:mediumslateblue,color:white,stroke-dasharray:4 3 2 1,font-size:20px,fillcolor:mediumslateblue
  app_android -.implementation.-> lib_android
  linkStyle 0 stroke:aqua
  app_android -.implementation.-> lib_kotlin_mp
  linkStyle 1 stroke:aqua
  lib_android -.implementation.-> lib_kotlin_jvm
  linkStyle 2 stroke:aqua
  lib_kotlin_mp --api--> lib_android
  linkStyle 3 stroke-width:5px
  lib_kotlin_mp ==jvmMainImplementation==> lib_java
  linkStyle 4 stroke:orange
  lib_kotlin_mp --api--> lib_kotlin_jvm
  linkStyle 5 stroke-width:5px
```

| Module Types | Color |
|:--:|:--:|
| Android App | <img src="https://img.shields.io/badge/-%20-limegreen?style=flat-square" height="30" width="100"> |
| Kotlin Multiplatform | <img src="https://img.shields.io/badge/-%20-mediumslateblue?style=flat-square" height="30" width="100"> |
| Android Library | <img src="https://img.shields.io/badge/-%20-lightgreen?style=flat-square" height="30" width="100"> |
| Kotlin JVM | <img src="https://img.shields.io/badge/-%20-mediumorchid?style=flat-square" height="30" width="100"> |
| Java | <img src="https://img.shields.io/badge/-%20-orange?style=flat-square" height="30" width="100"> |
| Other | <img src="https://img.shields.io/badge/-%20-gainsboro?style=flat-square" height="30" width="100"> |

| Link Types | Style |
|:--:|:--:|
| jvmMainImplementation | Orange Bold |
| api | Basic |
| implementation | Dashed |
<!--endregion-->
