package modular.gradle

import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import dev.detekt.gradle.plugin.DetektPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.withType

class ConventionDetekt : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    pluginsInternal {
      apply(DetektPlugin::class)
    }

    extensions.configure<DetektExtension> {
      config.from(rootProject.file("config/detekt.yml"))
      buildUponDefaultConfig.set(true)
    }

    val detektTasks = tasks.withType<Detekt>()
    val detektCheck by tasks.registering { dependsOn(detektTasks) }
    tasks.named("check").configure { dependsOn(detektCheck) }

    detektTasks.configureEach {
      reports.html.required.set(true)
      exclude { it.file.path.contains("generated") }
    }
  }
}
