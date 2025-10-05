/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2.internal

import assertk.assertThat
import modular.d2.D2Config
import modular.d2.Direction
import modular.d2.Location
import modular.d2.Position
import modular.d2.d2Writer
import modular.test.Abc
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
      layout = OneLevelOfSubmodules,
      groupModules = true,
    )

    assertThat(writer()).equalsDiffed(
      """
        app: :app
        data: :data {
          a: :a
          b: :b
        }
        domain: :domain {
          a: :a
          b: :b
        }
        ui: :ui {
          a: :a
          b: :b
          c: :c
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
      layout = TwoLevelsOfSubmodules,
      groupModules = true,
    )

    assertThat(writer()).equalsDiffed(
      """
        app: :app
        data: :data {
          a: :a
          b: :b
          sub: :sub {
            sub1: :sub1
            sub2: :sub2
          }
        }
        domain: :domain {
          a: :a
          b: :b
        }
        ui: :ui {
          a: :a
          b: :b
          c: :c
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
    val writer = d2Writer(layout = ModuleWithNoLinks)

    assertThat(writer()).equalsDiffed(
      """
        app: :app
      """.trimIndent(),
    )
  }

  @Test
  fun `Simple graph with direction`() {
    val writer = d2Writer(
      layout = Abc,
      config = D2Config(direction = Direction.Right),
    )

    assertThat(writer()).equalsDiffed(
      """
        direction: right
        a: :a
        b: :b
        c: :c
        a -> b
        a -> c
      """.trimIndent(),
    )
  }

  @Test
  fun `Simple graph with root style`() {
    val writer = d2Writer(
      layout = Abc,
      config = D2Config(
        rootStyle = mapOf(
          "fill" to "LightBlue",
          "stroke" to "FireBrick",
          "stroke-width" to "15",
        ),
      ),
    )

    assertThat(writer()).equalsDiffed(
      """
        style: {
          fill: "LightBlue"
          stroke: "FireBrick"
          stroke-width: "15"
        }
        a: :a
        b: :b
        c: :c
        a -> b
        a -> c
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
        a: :a
        b: :b
        c: :c
        a -> b: {
          style.stroke: "orange"
          style.stroke-dash: "4"
        }
        a -> c: {
          style.stroke-width: "3"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Specify group label position`() {
    val writer = d2Writer(
      layout = TwoLevelsOfSubmodules,
      groupModules = true,
      config = D2Config(groupLabelPosition = Position.TopRight),
    )

    assertThat(writer()).equalsDiffed(
      """
        app: :app
        data: :data {
          label.near: top-right
          a: :a
          b: :b
          sub: :sub {
            label.near: top-right
            sub1: :sub1
            sub2: :sub2
          }
        }
        domain: :domain {
          label.near: top-right
          a: :a
          b: :b
        }
        ui: :ui {
          label.near: top-right
          a: :a
          b: :b
          c: :c
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
  fun `Specify group label position and location`() {
    val writer = d2Writer(
      layout = TwoLevelsOfSubmodules,
      groupModules = true,
      config = D2Config(
        groupLabelPosition = Position.CenterLeft,
        groupLabelLocation = Location.Border,
      ),
    )

    assertThat(writer()).equalsDiffed(
      """
        app: :app
        data: :data {
          label.near: border-left-center
          a: :a
          b: :b
          sub: :sub {
            label.near: border-left-center
            sub1: :sub1
            sub2: :sub2
          }
        }
        domain: :domain {
          label.near: border-left-center
          a: :a
          b: :b
        }
        ui: :ui {
          label.near: border-left-center
          a: :a
          b: :b
          c: :c
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
}
