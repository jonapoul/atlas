package atlas.gradle

import app.cash.licensee.LicenseeExtension
import app.cash.licensee.LicenseePlugin
import app.cash.licensee.UnusedAction.IGNORE
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class ConventionLicensee : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    pluginsInternal {
      apply(LicenseePlugin::class)
    }

    extensions.configure<LicenseeExtension> {
      unusedAction(IGNORE)

      // Most stuff
      listOf("Apache-2.0", "MIT").forEach(::allow)

      // Junit 5
      allowUrl("https://www.eclipse.org/legal/epl-v20.html")
    }
  }
}
