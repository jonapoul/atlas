/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid

import assertk.assertThat
import modular.test.ModuleWithNoLinks
import modular.test.OneLevelOfSubmodules
import modular.test.TwoLevelsOfSubmodules
import modular.test.equalsDiffed
import kotlin.test.Test

internal class MermaidWriterTest {
  @Test
  fun `Base config with no module types`() {
    val writer = mermaidWriter(
      typedModules = OneLevelOfSubmodules.modules,
      links = OneLevelOfSubmodules.links,
    )

    assertThat(writer()).equalsDiffed(
      """
      graph TD
        _app[":app"]
        _data_a[":data:a"]
        _data_b[":data:b"]
        _domain_a[":domain:a"]
        _domain_b[":domain:b"]
        _ui_a[":ui:a"]
        _ui_b[":ui:b"]
        _ui_c[":ui:c"]
        _app --> _ui_a
        _app --> _ui_b
        _app --> _ui_c
        _domain_a --> _data_a
        _domain_b --> _data_a
        _domain_b --> _data_b
        _ui_a --> _domain_a
        _ui_b --> _domain_b
        _ui_c --> _domain_a
        _ui_c --> _domain_b
      """.trimIndent(),
    )
  }

  @Test
  fun `Grouping modules`() {
    val writer = mermaidWriter(
      typedModules = OneLevelOfSubmodules.modules,
      links = OneLevelOfSubmodules.links,
      groupModules = true,
    )

    assertThat(writer()).equalsDiffed(
      """
        graph TD
          _app[":app"]
          subgraph data[":data"]
            _data_a[":data:a"]
            _data_b[":data:b"]
          end
          subgraph domain[":domain"]
            _domain_a[":domain:a"]
            _domain_b[":domain:b"]
          end
          subgraph ui[":ui"]
            _ui_a[":ui:a"]
            _ui_b[":ui:b"]
            _ui_c[":ui:c"]
          end
          _app --> _ui_a
          _app --> _ui_b
          _app --> _ui_c
          _domain_a --> _data_a
          _domain_b --> _data_a
          _domain_b --> _data_b
          _ui_a --> _domain_a
          _ui_b --> _domain_b
          _ui_c --> _domain_a
          _ui_c --> _domain_b
      """.trimIndent(),
    )
  }

  @Test
  fun `Grouping modules with sub-subgraphs`() {
    val writer = mermaidWriter(
      typedModules = TwoLevelsOfSubmodules.modules,
      links = TwoLevelsOfSubmodules.links,
      groupModules = true,
    )

    assertThat(writer()).equalsDiffed(
      """
        graph TD
          _app[":app"]
          subgraph data[":data"]
            _data_a[":data:a"]
            _data_b[":data:b"]
            subgraph sub[":sub"]
              _data_sub_sub1[":data:sub:sub1"]
              _data_sub_sub2[":data:sub:sub2"]
            end
          end
          subgraph domain[":domain"]
            _domain_a[":domain:a"]
            _domain_b[":domain:b"]
          end
          subgraph ui[":ui"]
            _ui_a[":ui:a"]
            _ui_b[":ui:b"]
            _ui_c[":ui:c"]
          end
          _app --> _ui_a
          _app --> _ui_b
          _app --> _ui_c
          _domain_a --> _data_a
          _domain_a --> _data_sub_sub1
          _domain_a --> _data_sub_sub2
          _domain_b --> _data_a
          _domain_b --> _data_b
          _ui_a --> _domain_a
          _ui_b --> _domain_b
          _ui_c --> _domain_a
          _ui_c --> _domain_b
      """.trimIndent(),
    )
  }

  @Test
  fun `Single module with no links`() {
    val writer = mermaidWriter(
      typedModules = ModuleWithNoLinks.modules,
      links = ModuleWithNoLinks.links,
    )

    assertThat(writer()).equalsDiffed(
      """
        graph TD
          _app[":app"]
          style _app fill:red,fillcolor:red
      """.trimIndent(),
    )
  }
}
