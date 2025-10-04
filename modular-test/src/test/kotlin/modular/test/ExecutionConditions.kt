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
@RequiresCommand(command = "ln")
annotation class RequiresLn

@Target(FUNCTION, CLASS)
@Retention(RUNTIME)
@RequiresCommand(command = "whereis")
annotation class RequiresWhereis

@Target(FUNCTION, CLASS)
@Retention(RUNTIME)
@ExtendWith(RequiresCommandExtension::class)
annotation class RequiresCommand(val command: String)

class RequiresCommandExtension : ExecutionCondition {
  override fun evaluateExecutionCondition(context: ExtensionContext): ConditionEvaluationResult {
    val allCommands = context
      .element
      .orElse(null)
      ?.annotations
      ?.mapNotNull { a -> a.commandOrNull() }
      .orEmpty()
      .ifEmpty { return ConditionEvaluationResult.enabled("No element found") }

    val missingCommands = allCommands.filter { cmd -> !isCommandAvailable(cmd) }

    return if (missingCommands.isEmpty()) {
      ConditionEvaluationResult.enabled("All required commands are available: ${allCommands.joinToString()}")
    } else {
      val reason = "Missing required commands: ${missingCommands.joinToString()}"
      ConditionEvaluationResult.disabled(reason)
    }
  }

  private fun Annotation.commandOrNull() =
    annotationClass.java
      .getAnnotation(RequiresCommand::class.java)
      ?.command

  private fun isCommandAvailable(command: String): Boolean = try {
    ProcessBuilder()
      .command(if (isWindows()) "where" else "which", command)
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
