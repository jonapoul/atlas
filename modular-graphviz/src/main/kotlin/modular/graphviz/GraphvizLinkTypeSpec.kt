/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz

import modular.core.LinkTypeSpec

/**
 * Style specs from https://d2lang.com/tour/style, applied to the link between two modules.
 *
 * See [EdgeAttributes] for the available options, shared between links and modules.
 */
public interface GraphvizLinkTypeSpec : LinkTypeSpec, EdgeAttributes
