package atlas.test.scenarios

import atlas.test.GraphvizScenario
import atlas.test.kotlinJvmBuildScript

internal object GraphvizNestedProject : GraphvizScenario {
  override val rootBuildFile =
    """
    plugins {
      kotlin("jvm") apply false
    }
    """
      .trimIndent()

  override val atlasConfig = "projectTypes.kotlinJvm()"

  override val subprojectBuildFiles =
    mapOf(
      "app" to
        kotlinJvmBuildScript +
          """
          dependencies {
            implementation(project(":ui:a"))
            implementation(project(":ui:b"))
            implementation(project(":ui:c"))
          }
          """
            .trimIndent(),
      "ui:a" to
        kotlinJvmBuildScript +
          """
          dependencies {
            implementation(project(":domain:a"))
          }
          """
            .trimIndent(),
      "ui:b" to
        kotlinJvmBuildScript +
          """
          dependencies {
            implementation(project(":domain:b"))
          }
          """
            .trimIndent(),
      "ui:c" to
        kotlinJvmBuildScript +
          """
          dependencies {
            implementation(project(":domain:a"))
            implementation(project(":domain:b"))
          }
          """
            .trimIndent(),
      "domain:a" to
        kotlinJvmBuildScript +
          """
          dependencies {
            implementation(project(":data:a"))
          }
          """
            .trimIndent(),
      "domain:b" to
        kotlinJvmBuildScript +
          """
          dependencies {
            implementation(project(":data:a"))
            implementation(project(":data:b"))
          }
          """
            .trimIndent(),
      "data:a" to kotlinJvmBuildScript,
      "data:b" to kotlinJvmBuildScript,
    )
}
