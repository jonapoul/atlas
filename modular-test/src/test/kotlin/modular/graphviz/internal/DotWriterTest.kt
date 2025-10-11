/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.internal

import assertk.assertThat
import modular.graphviz.dotWriter
import modular.test.ModuleWithNoLinks
import modular.test.OneLevelOfSubmodules
import modular.test.TwoLevelsOfSubmodules
import modular.test.equalsDiffed
import kotlin.test.Test

internal class DotWriterTest {
  @Test
  internal fun `Base config with no module types`() {
    val writer = dotWriter(
      typedModules = OneLevelOfSubmodules.modules,
      links = OneLevelOfSubmodules.links,
    )

    assertThat(writer()).equalsDiffed(
      """
        digraph {
          ":app" [penwidth="3"]
          ":data:a"
          ":data:b"
          ":domain:a"
          ":domain:b"
          ":ui:a"
          ":ui:b"
          ":ui:c"
          ":app" -> ":ui:a"
          ":app" -> ":ui:b"
          ":app" -> ":ui:c"
          ":domain:a" -> ":data:a"
          ":domain:b" -> ":data:a"
          ":domain:b" -> ":data:b"
          ":ui:a" -> ":domain:a"
          ":ui:b" -> ":domain:b"
          ":ui:c" -> ":domain:a"
          ":ui:c" -> ":domain:b"
        }
      """.trimIndent(),
    )
  }

  @Test
  internal fun `Grouping modules`() {
    val writer = dotWriter(
      typedModules = OneLevelOfSubmodules.modules,
      links = OneLevelOfSubmodules.links,
      groupModules = true,
    )

    assertThat(writer()).equalsDiffed(
      """
        digraph {
          ":app" [penwidth="3"]
          subgraph cluster_data {
            label = ":data"
            ":data:a"
            ":data:b"
          }
          subgraph cluster_domain {
            label = ":domain"
            ":domain:a"
            ":domain:b"
          }
          subgraph cluster_ui {
            label = ":ui"
            ":ui:a"
            ":ui:b"
            ":ui:c"
          }
          ":app" -> ":ui:a"
          ":app" -> ":ui:b"
          ":app" -> ":ui:c"
          ":domain:a" -> ":data:a"
          ":domain:b" -> ":data:a"
          ":domain:b" -> ":data:b"
          ":ui:a" -> ":domain:a"
          ":ui:b" -> ":domain:b"
          ":ui:c" -> ":domain:a"
          ":ui:c" -> ":domain:b"
        }
      """.trimIndent(),
    )
  }

  @Test
  internal fun `Grouping modules with sub-subgraphs`() {
    val writer = dotWriter(
      typedModules = TwoLevelsOfSubmodules.modules,
      links = TwoLevelsOfSubmodules.links,
      groupModules = true,
    )

    assertThat(writer()).equalsDiffed(
      """
        digraph {
          ":app" [penwidth="3"]
          subgraph cluster_data {
            label = ":data"
            ":data:a"
            ":data:b"
            subgraph cluster_sub {
              label = ":sub"
              ":data:sub:sub1"
              ":data:sub:sub2"
            }
          }
          subgraph cluster_domain {
            label = ":domain"
            ":domain:a"
            ":domain:b"
          }
          subgraph cluster_ui {
            label = ":ui"
            ":ui:a"
            ":ui:b"
            ":ui:c"
          }
          ":app" -> ":ui:a"
          ":app" -> ":ui:b"
          ":app" -> ":ui:c"
          ":domain:a" -> ":data:a"
          ":domain:a" -> ":data:sub:sub1"
          ":domain:a" -> ":data:sub:sub2"
          ":domain:b" -> ":data:a"
          ":domain:b" -> ":data:b"
          ":ui:a" -> ":domain:a"
          ":ui:b" -> ":domain:b"
          ":ui:c" -> ":domain:a"
          ":ui:c" -> ":domain:b"
        }
      """.trimIndent(),
    )
  }

  @Test
  internal fun `Single module with no links`() {
    val writer = dotWriter(
      typedModules = ModuleWithNoLinks.modules,
      links = ModuleWithNoLinks.links,
    )

    assertThat(writer()).equalsDiffed(
      """
        digraph {
          ":app" [penwidth="3",fillcolor="red"]
        }
      """.trimIndent(),
    )
  }
}
