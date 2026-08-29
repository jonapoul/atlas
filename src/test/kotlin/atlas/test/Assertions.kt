package atlas.test

import assertk.Assert
import assertk.assertions.isEqualTo
import blueprint.test.Scenario as RunningScenario
import java.io.File

internal fun RunningScenario.resolve(path: String): File = rootDir.resolve(path)

internal fun <T> Assert<Set<T>>.isEqualToSet(vararg expected: T) = isEqualTo(expected.toSet())
