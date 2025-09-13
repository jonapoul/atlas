/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.spec

import org.gradle.api.provider.Property

/**
 * Configures the layout and spacing of the generated legend, which will contain all configured module types with
 * their allocated names and colours. They will be listed in the order you declare them, which is descending priority
 * order.
 */
interface GraphVizLegendSpec {
  val cellBorder: Property<Int>
  val cellPadding: Property<Int>
  val cellSpacing: Property<Int>
  val tableBorder: Property<Int>
}
