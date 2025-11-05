package atlas.d2.internal

import assertk.assertThat
import atlas.core.Replacement
import atlas.d2.LinkStyle
import atlas.d2.d2Writer
import atlas.test.Abc
import atlas.test.LowestLevelOfSubprojects
import atlas.test.ProjectWithNoLinks
import atlas.test.OneLevelOfSubprojects
import atlas.test.OneLevelOfSubprojectsWithReplacements
import atlas.test.ProjectLayout
import atlas.test.SingleNestedProjectWithNoLinks
import atlas.test.TwoLevelsOfSubprojects
import atlas.test.equalsDiffed
import atlas.test.projectLink
import kotlin.test.Test

internal class D2WriterTest {
  @Test
  fun `Base config with no project types`() {
    val writer = d2Writer(
      layout = OneLevelOfSubprojects,
      groupProjects = false,
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
        vars: {
          d2-legend: {
            project-dummy1.class: hidden
            project-dummy2.class: hidden
            project-dummy1 -> project-dummy2: implementation { class: link-implementation }
          }
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Lowest level of a multi-level hierarchy`() {
    val writer = d2Writer(
      layout = LowestLevelOfSubprojects,
      thisPath = ":ui:c",
    )

    // then the single project is written to the chart on its lonesome
    assertThat(writer()).equalsDiffed(
      """
        ...@../classes.d2
        ui_c: :ui:c
      """.trimIndent(),
    )
  }

  @Test
  fun `Grouping projects`() {
    val writer = d2Writer(
      layout = OneLevelOfSubprojects,
      groupProjects = true,
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
        vars: {
          d2-legend: {
            project-dummy1.class: hidden
            project-dummy2.class: hidden
            project-dummy1 -> project-dummy2: implementation { class: link-implementation }
          }
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Grouping projects with replacements`() {
    val writer = d2Writer(
      layout = OneLevelOfSubprojectsWithReplacements,
      groupProjects = true,
      replacements = setOf(
        Replacement(pattern = "^:projects:".toRegex(), replacement = ":"),
      ),
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
        vars: {
          d2-legend: {
            project-dummy1.class: hidden
            project-dummy2.class: hidden
            project-dummy1 -> project-dummy2: implementation { class: link-implementation }
          }
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Grouping projects with sub-subgraphs`() {
    val writer = d2Writer(
      layout = TwoLevelsOfSubprojects,
      groupProjects = true,
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
        vars: {
          d2-legend: {
            project-dummy1.class: hidden
            project-dummy2.class: hidden
            project-dummy1 -> project-dummy2: implementation { class: link-implementation }
          }
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Single project with no links`() {
    val writer = d2Writer(layout = ProjectWithNoLinks)

    assertThat(writer()).equalsDiffed(
      """
        ...@../classes.d2
        app: :app { class: project-red }
        vars: {
          d2-legend: {
            project-red: red { class: project-red }
          }
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Single nested project with no links`() {
    val writer = d2Writer(
      layout = SingleNestedProjectWithNoLinks,
      groupProjects = true,
      thisPath = ":a:b",
    )

    assertThat(writer()).equalsDiffed(
      """
        ...@../classes.d2
        a: :a {
          class: container
          b: :b { class: project-red }
        }
        vars: {
          d2-legend: {
            project-red: red { class: project-red }
          }
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `Graph with link styles and colors`() {
    val writer = d2Writer(
      layout = D2AbcWithLinkStyles,
      thisPath = ":a",
    )

    assertThat(writer()).equalsDiffed(
      """
        ...@../classes.d2
        a: :a
        b: :b
        c: :c
        a -> b { class: link-implementation }
        a -> c { class: link-implementation }
        vars: {
          d2-legend: {
            project-dummy1.class: hidden
            project-dummy2.class: hidden
            project-dummy1 -> project-dummy2: implementation { class: link-implementation }
            project-dummy1 -> project-dummy2: implementation { class: link-implementation }
          }
        }
      """.trimIndent(),
    )
  }

  private object D2AbcWithLinkStyles : ProjectLayout by Abc {
    override val links = setOf(
      projectLink(fromPath = ":a", toPath = ":b", style = LinkStyle.Dashed, color = "orange"),
      projectLink(fromPath = ":a", toPath = ":c", style = LinkStyle.Bold),
    )
  }
}
