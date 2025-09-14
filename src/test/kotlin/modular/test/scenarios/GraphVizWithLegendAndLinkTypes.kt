package modular.test.scenarios

import modular.test.Scenario

object GraphVizWithLegendAndLinkTypes : Scenario by GraphVizBasic {
  override val rootBuildFile = """
    import modular.graphviz.spec.LinkStyle

    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular.trunk")
    }

    modular {
      moduleTypes {
        kotlinJvm()
        java()
      }

      graphViz {
        legend()

        linkTypes {
          "jvmMainImplementation"(style = LinkStyle.Bold, color = "orange")
          api()
          implementation(LinkStyle.Dotted)
        }
      }
    }
  """.trimIndent()
}
