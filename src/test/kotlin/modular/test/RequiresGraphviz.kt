/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test

import org.junit.jupiter.api.extension.ConditionEvaluationResult
import org.junit.jupiter.api.extension.ExecutionCondition
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import java.lang.reflect.AnnotatedElement

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ExtendWith(RequiresGraphvizExtension::class)
internal annotation class RequiresGraphviz

internal class RequiresGraphvizExtension : ExecutionCondition {
  override fun evaluateExecutionCondition(context: ExtensionContext): ConditionEvaluationResult {
    val element = context.element.orElse(null)
    val annotation = findAnnotation(element)
    return if (annotation != null) {
      if (isGraphvizAvailable()) {
        ConditionEvaluationResult.enabled("dot is available")
      } else {
        ConditionEvaluationResult.disabled("dot is not available")
      }
    } else {
      ConditionEvaluationResult.enabled("No @RequiresCommand annotation")
    }
  }

  private fun findAnnotation(element: AnnotatedElement?): RequiresGraphviz? =
    element?.getAnnotation(RequiresGraphviz::class.java)

  private fun isGraphvizAvailable(): Boolean = try {
    val process = ProcessBuilder("dot", "-?")
      .redirectErrorStream(true)
      .start()
    process.waitFor() == 0
  } catch (_: Exception) {
    false
  }
}
