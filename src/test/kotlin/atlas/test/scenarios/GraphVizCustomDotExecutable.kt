package atlas.test.scenarios

import atlas.test.Scenario

internal object GraphVizCustomDotExecutable : Scenario by GraphvizBasic {
  override val atlasConfig =
    """
    projectTypes {
      kotlinJvm()
      java()
    }

    graphviz {
      pathToDotCommand = settingsDir.resolve("path/to/custom/dot").absolutePath
      fileFormat = FileFormat.Svg
    }
    """
      .trimIndent()
}
