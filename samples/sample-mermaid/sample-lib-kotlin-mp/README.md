# sample-lib-kotlin-mp

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
    fontSize: 30px
    defaultLinkColor: #5555FF
---
graph TD
  lib_android["lib android"]
  lib_java["lib java"]
  lib_kotlin_jvm["lib kotlin jvm"]
  lib_kotlin_mp["lib kotlin mp"]
  style lib_android fill:lightgreen,color:black
  style lib_java fill:orange,color:black
  style lib_kotlin_jvm fill:mediumorchid,color:black
  style lib_kotlin_mp fill:mediumslateblue,color:black,font-weight:bold,stroke:black,stroke-width:2px
  lib_android -.-> lib_kotlin_jvm
  lib_kotlin_mp --> lib_android
  lib_kotlin_mp ==> lib_java
  linkStyle 2 stroke:orange
  lib_kotlin_mp --> lib_kotlin_jvm
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
