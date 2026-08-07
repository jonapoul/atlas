package atlas.test.scenarios

import atlas.core.Framework
import atlas.test.Scenario
import atlas.test.javaBuildScript

internal object NoRootBuildFile : Scenario {
  override val frameworks = Framework.entries.toSet()

  override val atlasConfig =
    """
    projectTypes {
      java()
    }
    """
      .trimIndent()

  override val subprojectBuildFiles =
    mapOf(
      "a" to
        """
      $javaBuildScript
      dependencies {
        implementation(project(":b"))
      }
    """
          .trimIndent(),
      "b" to javaBuildScript,
    )
}
