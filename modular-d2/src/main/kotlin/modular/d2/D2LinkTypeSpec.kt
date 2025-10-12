/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2

import modular.core.LinkTypeSpec

/**
 * Style specs from https://d2lang.com/tour/style, applied to the link between two modules.
 *
 * See [D2PropertiesSpec] for the available options, shared between links and modules.
 */
public interface D2LinkTypeSpec : LinkTypeSpec, D2PropertiesSpec
