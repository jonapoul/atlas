/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.spec

import org.gradle.api.provider.Property

interface Spec {
  // allows us store to store Spec in a NamedDomainObjectContainer
  val name: String
  val fileExtension: Property<String>
}
