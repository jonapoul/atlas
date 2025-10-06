/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2.internal

import assertk.assertThat
import modular.d2.d2Writer
import modular.test.AbcWithLinkStyles
import modular.test.ModuleWithNoLinks
import modular.test.OneLevelOfSubmodules
import modular.test.TwoLevelsOfSubmodules
import modular.test.equalsDiffed
import kotlin.test.Test

class D2WriterTest {
  @Test
  fun `Base config with no module types`() {
    val writer = d2Writer(
      layout = OneLevelOfSubmodules,
      groupModules = false,
    )

    assertThat(writer()).equalsDiffed(
      """
        ...@../classes.d2
        app: :app
        data_a: :data:a
        data_b: :data:b
        domain_a: :domain:a
        domain_b: :domain:b
        ui_a: :ui:a
        ui_b: :ui:b
        ui_c: :ui:c
        app -> ui_a { class: link-implementation }
        app -> ui_b { class: link-implementation }
        app -> ui_c { class: link-implementation }
        domain_a -> data_a { class: link-implementation }
        domain_b -> data_a { class: link-implementation }
        domain_b -> data_b { class: link-implementation }
        ui_a -> domain_a { class: link-implementation }
        ui_b -> domain_b { class: link-implementation }
        ui_c -> domain_a { class: link-implementation }
        ui_c -> domain_b { class: link-implementation }
      """.trimIndent(),
    )
  }

  @Test
  fun `Grouping modules`() {
    val writer = d2Writer(
      layout = OneLevelOfSubmodules,
      groupModules = true,
    )

    assertThat(writer()).equalsDiffed(
      """
        ...@../classes.d2
        app: :app
        data: :data {
          class: container
          a: :a
          b: :b
        }
        domain: :domain {
          class: container
          a: :a
          b: :b
        }
        ui: :ui {
          class: container
          a: :a
          b: :b
          c: :c
        }
        app -> ui.a { class: link-implementation }
        app -> ui.b { class: link-implementation }
        app -> ui.c { class: link-implementation }
        domain.a -> data.a { class: link-implementation }
        domain.b -> data.a { class: link-implementation }
        domain.b -> data.b { class: link-implementation }
        ui.a -> domain.a { class: link-implementation }
        ui.b -> domain.b { class: link-implementation }
        ui.c -> domain.a { class: link-implementation }
        ui.c -> domain.b { class: link-implementation }
      """.trimIndent(),
    )
  }

  @Test
  fun `Grouping modules with sub-subgraphs`() {
    val writer = d2Writer(
      layout = TwoLevelsOfSubmodules,
      groupModules = true,
    )

    assertThat(writer()).equalsDiffed(
      """
        ...@../classes.d2
        app: :app
        data: :data {
          class: container
          a: :a
          b: :b
          sub: :sub {
            class: container
            sub1: :sub1
            sub2: :sub2
          }
        }
        domain: :domain {
          class: container
          a: :a
          b: :b
        }
        ui: :ui {
          class: container
          a: :a
          b: :b
          c: :c
        }
        app -> ui.a { class: link-implementation }
        app -> ui.b { class: link-implementation }
        app -> ui.c { class: link-implementation }
        domain.a -> data.a { class: link-implementation }
        domain.a -> data.sub.sub1 { class: link-implementation }
        domain.a -> data.sub.sub2 { class: link-implementation }
        domain.b -> data.a { class: link-implementation }
        domain.b -> data.b { class: link-implementation }
        ui.a -> domain.a { class: link-implementation }
        ui.b -> domain.b { class: link-implementation }
        ui.c -> domain.a { class: link-implementation }
        ui.c -> domain.b { class: link-implementation }
      """.trimIndent(),
    )
  }

  @Test
  fun `Single module with no links`() {
    val writer = d2Writer(layout = ModuleWithNoLinks)

    assertThat(writer()).equalsDiffed(
      """
        ...@../classes.d2
        app: :app
      """.trimIndent(),
    )
  }

  @Test
  fun `Graph with link styles and colors`() {
    val writer = d2Writer(
      layout = AbcWithLinkStyles,
    )

    assertThat(writer()).equalsDiffed(
      """
        ...@../classes.d2
        a: :a
        b: :b
        c: :c
        a -> b { class: link-implementation }
        a -> c { class: link-implementation }
      """.trimIndent(),
    )
  }
}
