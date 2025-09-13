/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test

import org.junit.jupiter.api.extension.ConditionEvaluationResult
import org.junit.jupiter.api.extension.ExecutionCondition
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FUNCTION

@Target(FUNCTION, CLASS)
@Retention(RUNTIME)
@RequiresCommand(command = "dot")
internal annotation class RequiresGraphviz

@Target(FUNCTION, CLASS)
@Retention(RUNTIME)
@RequiresCommand(command = "ln")
internal annotation class RequiresLn

@Target(FUNCTION, CLASS)
@Retention(RUNTIME)
@RequiresCommand(command = "whereis")
internal annotation class RequiresWhereis

@Target(FUNCTION, CLASS)
@Retention(RUNTIME)
@ExtendWith(RequiresCommandExtension::class)
internal annotation class RequiresCommand(val command: String)

internal class RequiresCommandExtension : ExecutionCondition {
  override fun evaluateExecutionCondition(context: ExtensionContext): ConditionEvaluationResult {
    val element = context.element.orElse(null)
      ?: return ConditionEvaluationResult.enabled("No element found")

    val allCommands = element
      .annotations
      .mapNotNull { a ->
        a.annotationClass.java
          .getAnnotation(RequiresCommand::class.java)
          ?.command
      }

    val missingCommands = allCommands.filter { cmd -> !isCommandAvailable(cmd) }

    return if (missingCommands.isEmpty()) {
      ConditionEvaluationResult.enabled("All required commands are available: ${allCommands.joinToString()}")
    } else {
      val reason = "Missing required commands: ${missingCommands.joinToString()}"
      ConditionEvaluationResult.disabled(reason)
    }
  }

  private fun isCommandAvailable(command: String): Boolean = try {
    ProcessBuilder()
      .apply { command(if (isWindows()) "where" else "which", command) }
      .redirectErrorStream(true)
      .start()
      .waitFor() == 0
  } catch (_: Exception) {
    false
  }

  private fun isWindows() = System
    .getProperty("os.name")
    .lowercase()
    .contains("win")
}
