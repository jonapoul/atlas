package atlas.graphviz

import atlas.test.RequiresCommand
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FUNCTION

@Target(FUNCTION, CLASS)
@Retention(RUNTIME)
@RequiresCommand(command = "dot")
internal annotation class RequiresGraphviz
