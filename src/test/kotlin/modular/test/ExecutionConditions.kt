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

    val annotations = element.getAnnotationsByType(RequiresCommand::class.java)
    val missingCommands = mutableListOf<String>()

    for (a in annotations) {
      val cmd = a.annotationClass.java.getAnnotation(RequiresCommand::class.java)
      if (cmd != null) {
        if (!isCommandAvailable(cmd.command)) {
          val message = "Command '${cmd.command}' is not available (required by @${a.annotationClass.simpleName})"
          missingCommands.add(message)
        }
      }
    }

    return if (missingCommands.isEmpty()) {
      val allCommands = annotations.joinToString { it.command }
      ConditionEvaluationResult.enabled("All required commands are available: $allCommands")
    } else {
      val reason = "Missing required commands: ${missingCommands.joinToString()}"
      ConditionEvaluationResult.disabled(reason)
    }
  }

  private fun isCommandAvailable(command: String): Boolean = try {
    println("isCommandAvailable $command")
    ProcessBuilder()
      .apply { command(if (isWindows()) "where" else "which", command) }
      .redirectErrorStream(true)
      .start()
      .waitFor() == 0
  } catch (_: Exception) {
    println("isCommandAvailable $command failed!")
    false
  }

  private fun isWindows() = System
    .getProperty("os.name")
    .lowercase()
    .contains("win")
}
