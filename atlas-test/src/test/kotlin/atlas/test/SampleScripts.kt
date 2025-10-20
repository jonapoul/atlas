package atlas.test

import org.intellij.lang.annotations.Language

@Language("kotlin")
const val REPOSITORIES_GRADLE_KTS = """
pluginManagement {
  repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
  }
}
"""

@Language("groovy")
const val REPOSITORIES_GRADLE_GROOVY = """
pluginManagement {
  repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
  }
}
"""
