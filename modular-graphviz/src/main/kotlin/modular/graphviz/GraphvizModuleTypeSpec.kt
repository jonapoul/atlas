/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz

import modular.core.ModuleTypeSpec

/**
 * See https://graphviz.org/docs/nodes/ for custom attributes, which you can set using [put] from this interface.
 * Some of the more common options are included as vars below.
 *
 * Not all properties from the above link will be valid, you specifically want the ones for nodes. The plugin won't
 * validate them, but graphviz will (probably?) fail compilation when you generate the chart file.
 */
interface GraphvizModuleTypeSpec : ModuleTypeSpec {
  override fun put(key: String, value: Any)
  var shape: Shape?
}
