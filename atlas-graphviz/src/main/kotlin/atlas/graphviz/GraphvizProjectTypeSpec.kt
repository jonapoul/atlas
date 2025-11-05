package atlas.graphviz

import atlas.core.ProjectTypeSpec

/**
 * See https://graphviz.org/docs/nodes/ and [NodeAttributes] for custom attributes, which you can set using various
 * Kotlin vars.
 */
public interface GraphvizProjectTypeSpec : ProjectTypeSpec, NodeAttributes
