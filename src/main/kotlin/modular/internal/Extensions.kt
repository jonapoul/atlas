/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.internal

import modular.gradle.Color
import modular.gradle.ModularExtension
import modular.spec.ModuleType
import modular.spec.ModuleTypeModel

internal fun ModularExtension.orderedTypes(): List<ModuleType> =
  (moduleTypes as OrderedNamedContainer<ModuleType>).getInOrder()

internal fun moduleTypeModel(type: ModuleType) = ModuleTypeModel(
  name = type.name,
  color = Color.from(type.color.get()),
)
