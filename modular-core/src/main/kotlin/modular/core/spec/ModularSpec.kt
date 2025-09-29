/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.spec

import org.gradle.api.provider.Property

/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
interface ModularSpec {
  val name: String
  val fileExtension: Property<String>
}
