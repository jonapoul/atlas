package atlas.mermaid

import assertk.assertThat
import atlas.test.ProjectWithNoLinks
import atlas.test.OneLevelOfSubprojects
import atlas.test.TwoLevelsOfSubprojects
import atlas.test.equalsDiffed
import kotlin.test.Test

internal class MermaidWriterTest {
  @Test
  fun `Base config with no project types`() {
    val writer = mermaidWriter(
      typedProjects = OneLevelOfSubprojects.projects,
      links = OneLevelOfSubprojects.links,
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
  fun `Grouping projects`() {
    val writer = mermaidWriter(
      typedProjects = OneLevelOfSubprojects.projects,
      links = OneLevelOfSubprojects.links,
      groupProjects = true,
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
  fun `Grouping projects with sub-subgraphs`() {
    val writer = mermaidWriter(
      typedProjects = TwoLevelsOfSubprojects.projects,
      links = TwoLevelsOfSubprojects.links,
      groupProjects = true,
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
  fun `Single project with no links`() {
    val writer = mermaidWriter(
      typedProjects = ProjectWithNoLinks.projects,
      links = ProjectWithNoLinks.links,
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
