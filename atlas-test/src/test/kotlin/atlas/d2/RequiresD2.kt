package atlas.d2

import atlas.test.RequiresCommand
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FUNCTION

@Target(FUNCTION, CLASS)
@Retention(RUNTIME)
@RequiresCommand(command = "d2")
internal annotation class RequiresD2
