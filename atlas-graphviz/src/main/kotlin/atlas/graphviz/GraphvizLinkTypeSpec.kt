package atlas.graphviz

import atlas.core.LinkTypeSpec

/**
 * Style specs from https://d2lang.com/tour/style, applied to the link between two modules.
 *
 * See [EdgeAttributes] for the available options, shared between links and modules.
 */
public interface GraphvizLinkTypeSpec : LinkTypeSpec, EdgeAttributes
