package modular.test

interface Scenario {
  val settingsFile: String
  val rootBuildFile: String
  val submoduleBuildFiles: Map<String, String>
}
