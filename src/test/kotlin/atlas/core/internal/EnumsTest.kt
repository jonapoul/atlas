package atlas.core.internal

import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import atlas.d2.Theme
import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class EnumsTest {
  @Test
  fun `Parse an int enum from its number`() {
    assertThat(parseIntEnum<Theme>("201")).isEqualTo(DarkFlagshipTerrastruct)
  }

  @Test
  fun `Parse an int enum from its name`() {
    // the DSL only ever names these, so a gradle property has to accept the name too
    assertThat(parseIntEnum<Theme>("DarkFlagshipTerrastruct")).isEqualTo(DarkFlagshipTerrastruct)
    assertThat(parseIntEnum<Theme>("darkflagshipterrastruct")).isEqualTo(DarkFlagshipTerrastruct)
  }

  @Test
  fun `Say what the options were when an int enum doesn't match`() {
    val error = assertFailsWith<IllegalStateException> { parseIntEnum<Theme>("Nonsense") }
    assertThat(error)
      .hasMessage("No Theme matching 'Nonsense'. Expected one of ${intOptionsOf<Theme>()}.")
  }
}
