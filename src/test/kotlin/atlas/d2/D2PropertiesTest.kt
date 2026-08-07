package atlas.d2

import assertk.assertThat
import atlas.test.ScenarioTest
import atlas.test.contentContains
import atlas.test.resolve
import atlas.test.scenarios.D2ConfiguredByProperties
import blueprint.test.runTask
import blueprint.test.taskSucceeded
import kotlin.test.Test

internal class D2PropertiesTest : ScenarioTest() {
  @Test
  fun `Configure D2 entirely through gradle properties`() =
    runScenario(D2ConfiguredByProperties) {
      // when
      val result = runTask("writeD2Classes").build()

      // then
      assertThat(result).taskSucceeded(":writeD2Classes")

      val classes = resolve("atlas/d2/classes.d2")
      assertThat(classes)
        // the theme is an int enum, but the DSL names it, so the property takes the name too
        .contentContains("theme-id: 201")
        // groupLabelLocation used to read a mis-keyed property, so it was silently dropped
        .contentContains("label.near: border-bottom-center")
        .contentContains("center: true")
        .contentContains("pad: 5")
        .contentContains("direction: down")
    }
}
