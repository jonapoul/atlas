package atlas.test

import kotlin.test.fail
import org.junit.jupiter.api.extension.ConditionEvaluationResult
import org.junit.jupiter.api.extension.ExecutionCondition
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext

@RequiresCommand(command = "ln") annotation class RequiresLn

@RequiresCommand(command = "whereis") annotation class RequiresWhereis

@RequiresCommand(command = "convert") annotation class RequiresImageMagick6

@ExtendWith(RequiresCommandExtension::class) annotation class RequiresCommand(val command: String)

internal class RequiresCommandExtension : ExecutionCondition {
  override fun evaluateExecutionCondition(context: ExtensionContext): ConditionEvaluationResult {
    val allCommands =
      context.element
        .orElse(null)
        ?.annotations
        ?.mapNotNull { a -> a.commandOrNull() }
        .orEmpty()
        .ifEmpty {
          return ConditionEvaluationResult.enabled("No element found")
        }

    val missingCommands = allCommands.filter { cmd -> !isCommandAvailable(cmd) }

    return if (missingCommands.isEmpty()) {
      ConditionEvaluationResult.enabled(
        "All required commands are available: ${allCommands.joinToString()}"
      )
    } else {
      val reason = "Missing required commands: ${missingCommands.joinToString()}"

      if (System.getenv("CI").toBoolean()) {
        fail(reason)
      } else {
        System.err.println("WARNING: $reason - skipping test")
        ConditionEvaluationResult.disabled(reason)
      }
    }
  }

  private fun Annotation.commandOrNull() =
    (this as? RequiresCommand)?.command
      ?: annotationClass.java.getAnnotation(RequiresCommand::class.java)?.command

  private fun isCommandAvailable(command: String): Boolean =
    try {
      ProcessBuilder()
        .command(if (isWindows()) "where" else "which", command)
        .redirectErrorStream(true)
        .start()
        .waitFor() == 0
    } catch (_: Exception) {
      false
    }

  private fun isWindows() = System.getProperty("os.name").lowercase().contains("win")
}
