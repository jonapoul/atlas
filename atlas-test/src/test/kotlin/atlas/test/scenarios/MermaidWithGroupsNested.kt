package atlas.test.scenarios

import atlas.test.Scenario
import atlas.test.javaBuildScript
import atlas.test.kotlinJvmBuildScript

internal object MermaidWithGroupsNested : Scenario by MermaidWithGroupsNotNested {
  override val subprojectBuildFiles = mapOf(
    "a" to """
      $kotlinJvmBuildScript
      dependencies {
        api(project(":b:b1"))
        implementation(project(":b:b2"))
      }
    """.trimIndent(),

    "b:b1" to """
      $javaBuildScript
      dependencies {
        implementation(project(":c:inner:c1"))
        implementation(project(":c:inner:c2"))
      }
    """.trimIndent(),

    "b:b2" to """
      $javaBuildScript
      dependencies {
        implementation(project(":c:inner:c2"))
        implementation(project(":c:c3"))
      }
    """.trimIndent(),

    "c:inner:c1" to javaBuildScript,

    "c:inner:c2" to javaBuildScript,

    "c:c3" to javaBuildScript,
  )
}
