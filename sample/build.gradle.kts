plugins {
  alias(libs.plugins.agp.app) apply false
  alias(libs.plugins.agp.lib) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.multiplatform) apply false
  id("dev.jonpoulton.modular")
}

modular {
  moduleTypes {
    builtIns()
  }
}
