/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2.internal

import assertk.assertThat
import assertk.assertions.contains
import modular.d2.d2Writer
import modular.test.ModuleWithNoLinks
import modular.test.OneLevelOfSubmodules
import modular.test.TwoLevelsOfSubmodules
import kotlin.test.Test

class D2WriterTest {
  @Test
  fun `Base config with no module types`() {
    val writer = d2Writer(
      typedModules = OneLevelOfSubmodules.modules,
      links = OneLevelOfSubmodules.links,
      groupModules = false,
    )

    assertThat(writer()).contains(
      """
        app: :app
        data_a: :data:a
        data_b: :data:b
        domain_a: :domain:a
        domain_b: :domain:b
        ui_a: :ui:a
        ui_b: :ui:b
        ui_c: :ui:c
        app -> ui_a
        app -> ui_b
        app -> ui_c
        domain_a -> data_a
        domain_b -> data_a
        domain_b -> data_b
        ui_a -> domain_a
        ui_b -> domain_b
        ui_c -> domain_a
        ui_c -> domain_b
      """.trimIndent(),
    )
  }

  @Test
  fun `Grouping modules`() {
    val writer = d2Writer(
      typedModules = OneLevelOfSubmodules.modules,
      links = OneLevelOfSubmodules.links,
      groupModules = true,
    )

    assertThat(writer()).contains(
      """
        app: :app
        data: data {
          a: :data:a
          b: :data:b
        }
        domain: domain {
          a: :domain:a
          b: :domain:b
        }
        ui: ui {
          a: :ui:a
          b: :ui:b
          c: :ui:c
        }
        app -> ui.a
        app -> ui.b
        app -> ui.c
        domain.a -> data.a
        domain.b -> data.a
        domain.b -> data.b
        ui.a -> domain.a
        ui.b -> domain.b
        ui.c -> domain.a
        ui.c -> domain.b
      """.trimIndent(),
    )
  }

  @Test
  fun `Grouping modules with sub-subgraphs`() {
    val writer = d2Writer(
      typedModules = TwoLevelsOfSubmodules.modules,
      links = TwoLevelsOfSubmodules.links,
      groupModules = true,
    )

    assertThat(writer()).contains(
      """
        app: :app
        data: data {
          a: :data:a
          b: :data:b
          sub: sub {
            sub1: :data:sub:sub1
            sub2: :data:sub:sub2
          }
        }
        domain: domain {
          a: :domain:a
          b: :domain:b
        }
        ui: ui {
          a: :ui:a
          b: :ui:b
          c: :ui:c
        }
        app -> ui.a
        app -> ui.b
        app -> ui.c
        domain.a -> data.a
        domain.a -> data.sub.sub1
        domain.a -> data.sub.sub2
        domain.b -> data.a
        domain.b -> data.b
        ui.a -> domain.a
        ui.b -> domain.b
        ui.c -> domain.a
        ui.c -> domain.b
      """.trimIndent(),
    )
  }

  @Test
  fun `Single module with no links`() {
    val writer = d2Writer(
      typedModules = ModuleWithNoLinks.modules,
      links = ModuleWithNoLinks.links,
    )

    assertThat(writer()).contains(
      """
        app: :app
      """.trimIndent(),
    )
  }
}
