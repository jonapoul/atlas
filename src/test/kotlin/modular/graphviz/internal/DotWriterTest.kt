/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.internal

import assertk.assertThat
import assertk.assertions.contains
import modular.test.dotWriter
import kotlin.test.Test

class DotWriterTest {
  @Test
  fun `Base config with no module types`() {
    val writer = dotWriter(
      typedModules = OneLevelOfSubmodules.modules,
      links = OneLevelOfSubmodules.links,
    )

    assertThat(writer()).contains(
      """
        digraph {
          node ["style"="filled"]
          ":app" ["penwidth"="3","shape"="box"]
          ":data:a" ["shape"="none"]
          ":data:b" ["shape"="none"]
          ":domain:a" ["shape"="none"]
          ":domain:b" ["shape"="none"]
          ":ui:a" ["shape"="none"]
          ":ui:b" ["shape"="none"]
          ":ui:c" ["shape"="none"]
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
  fun `Grouping modules`() {
    val writer = dotWriter(
      typedModules = OneLevelOfSubmodules.modules,
      links = OneLevelOfSubmodules.links,
      groupModules = true,
    )

    assertThat(writer()).contains(
      """
        digraph {
          node ["style"="filled"]
          ":app" ["penwidth"="3","shape"="box"]
          subgraph cluster_data {
            label = ":data"
            ":data:a" ["shape"="none"]
            ":data:b" ["shape"="none"]
          }
          subgraph cluster_domain {
            label = ":domain"
            ":domain:a" ["shape"="none"]
            ":domain:b" ["shape"="none"]
          }
          subgraph cluster_ui {
            label = ":ui"
            ":ui:a" ["shape"="none"]
            ":ui:b" ["shape"="none"]
            ":ui:c" ["shape"="none"]
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
  fun `Grouping modules with sub-subgraphs`() {
    val writer = dotWriter(
      typedModules = TwoLevelsOfSubmodules.modules,
      links = TwoLevelsOfSubmodules.links,
      groupModules = true,
    )

    assertThat(writer()).contains(
      """
        digraph {
          node ["style"="filled"]
          ":app" ["penwidth"="3","shape"="box"]
          subgraph cluster_data {
            label = ":data"
            ":data:a" ["shape"="none"]
            ":data:b" ["shape"="none"]
            subgraph cluster_sub {
              label = ":sub"
              ":data:sub:sub1" ["shape"="none"]
              ":data:sub:sub2" ["shape"="none"]
            }
          }
          subgraph cluster_domain {
            label = ":domain"
            ":domain:a" ["shape"="none"]
            ":domain:b" ["shape"="none"]
          }
          subgraph cluster_ui {
            label = ":ui"
            ":ui:a" ["shape"="none"]
            ":ui:b" ["shape"="none"]
            ":ui:c" ["shape"="none"]
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
}
